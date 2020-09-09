import requests
import json
import multiprocessing
from bs4 import BeautifulSoup
from collections import ChainMap
from io import FileIO

NUM_SPECIES = 890

URL_KALOS = "https://www.pokemon.com/us/api/pokedex/kalos"
URL_OFFICIAL = "https://www.pokemon.com/us/pokedex/"


def scrape(target):
    data = dict()

    r = requests.get(url=target)
    soup = None
    if (r.status_code == 200):
        soup = BeautifulSoup(r.text, "lxml")
    assert(soup is not None)

    # base_name, id
    identity_div = soup.find(
        attrs={"class": "pokedex-pokemon-pagination-title"})
    name_id_list = identity_div.div.text.split('#')
    name = name_id_list[0].strip()
    id_str = name_id_list[1].strip()
    id = int(id_str)
    data[str(id)] = {"base_name": name}

    # form_names and form_img_url
    profile_image_div = soup.find(attrs={"class": "profile-images"})
    for img_elem in profile_image_div.find_all('img'):
        # print(img_elem["alt"] + ": " + img_elem["src"])
        if "forms" in data[str(id)]:
            pass
        else:
            data[str(id)]["forms"] = list()
        data[str(id)]["forms"].append(
            {"form_name": img_elem["alt"], "img_url": img_elem["src"]})

    # types per form; assuming listed types are in the same order as form names
    index = 0
    pokemon_attribute_lists = soup.find_all(attrs={"class": "dtm-type"})
    for dtm_type_div in pokemon_attribute_lists:
        type_lists = dtm_type_div.ul.find_all("a")
        type_lists_data = list()
        for type_list in type_lists:
            type_lists_data.append(type_list.text)
        # print(type_lists_data)
        data[str(id)]["forms"][index]["type"] = type_lists_data
        index += 1

    return data


# def main():
    # r = requests.get(url=URL_KALOS)
    # data = r.json()

    # map = dict()

    # for i in range(0, len(data)):
    #     item = data[i]
    #     poke_id = item["id"]
    #     if (map.get(poke_id) != None):
    #         map[poke_id]["bulk_indices"].append(i)
    #     else:
    #         indicies = list()
    #         indicies.append(i)
    #         map[poke_id] = {"bulk_indices": indicies}

    # print(json.dumps(map))

urls_all = [x for x in map(lambda x: URL_OFFICIAL +
                           str(x + 1), range(NUM_SPECIES))]

urls_test = [x for x in map(lambda x: URL_OFFICIAL +
                            str(x + 1), range(50))]

urls_single = [URL_OFFICIAL + "6"]

if __name__ == "__main__":
    with multiprocessing.Pool(multiprocessing.cpu_count()) as pool:
        object_list = pool.map(scrape, urls_all)
        objects_mapped = dict(ChainMap(*object_list))
        json_data = json.dumps(objects_mapped)
        with open('pokemon_data.json', 'w') as f:
            f.write(json_data)
