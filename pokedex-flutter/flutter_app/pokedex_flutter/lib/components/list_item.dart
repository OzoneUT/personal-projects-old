import 'package:flutter/material.dart';
import 'package:pokedexflutter/models/species_profile.dart';
import 'package:pokedexflutter/models/type.dart';
import 'package:pokedexflutter/utils/utils.dart';
import '../repository.dart';
import 'package:transparent_image/transparent_image.dart';

class ListItem extends StatelessWidget {
  final int pokemonId;
  const ListItem({@required this.pokemonId});

  @override
  Widget build(BuildContext context) {
    SpeciesProfile profile =
        Repository.instance.dataBox.get(pokemonId.toString());
    List<TypeData> types = profile.forms.first.types;
    Color primaryTypeColor = Color(types.first.color);
    return Stack(
      children: <Widget>[
        Container(
          margin: EdgeInsets.symmetric(vertical: 15.0, horizontal: 30.0),
          height: 115.0,
          width: double.infinity,
          decoration: BoxDecoration(
            color: primaryTypeColor,
            borderRadius: BorderRadius.circular(15.0),
            boxShadow: [
              BoxShadow(
                color: primaryTypeColor.withOpacity(0.50),
                blurRadius: 10,
                offset: Offset(0, 10),
              )
            ],
          ),
          child: Container(
            margin: EdgeInsets.all(20.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: <Widget>[
                Text(
                  '#${pokemonId.toString().padLeft(3, '0')}',
                  style: TextStyle(fontWeight: FontWeight.bold),
                ),
                Text(
                  profile.baseName,
                  style: TextStyle(
                      color: Colors.white,
                      fontWeight: FontWeight.bold,
                      fontSize: 26.0),
                ),
                Row(
                  children: types.map((type) {
                    return Container(
                      padding: EdgeInsets.all(4.0),
                      margin: EdgeInsets.only(right: 5.0),
                      child: Text(
                        type.name,
                        style: TextStyle(color: Colors.white),
                      ),
                      decoration: BoxDecoration(
                          color: darken(Color(type.color), 15),
                          borderRadius: BorderRadius.all(
                            Radius.circular(5.0),
                          )),
                    );
                  }).toList(growable: false),
                )
              ],
            ),
          ),
        ),
        Positioned(
          top: 0,
          right: 40.0,
          child: Container(
            height: 130,
            width: 130,
            child: FadeInImage.memoryNetwork(
              fit: BoxFit.fill,
              image: profile.forms.first.imageUrl,
              placeholder: kTransparentImage,
            ),
          ),
        ),
      ],
    );
  }
}
