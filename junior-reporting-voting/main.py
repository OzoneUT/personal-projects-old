from selenium import webdriver
from selenium.webdriver.firefox.options import Options
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.by import By
from selenium.common.exceptions import TimeoutException
from selenium.webdriver.common.keys import Keys
from time import sleep
import random

EMAIL = 0
FIRST_NAME = 1
LAST_NAME = 2
my_list = []


options = Options()
options.headless = True
driver = webdriver.Firefox(options=options)
count_success = 0
count_already_voted = 0
count_no_confirmation = 0


def cast_vote(index):
    global count_already_voted, count_success, count_no_confirmation
    driver.get("https://wtop.com/contests/2019-junior-reporter-voting/")
    assert driver.title == "2019 Junior Reporter Voting | WTOP"
    button = driver.find_element_by_id("entry-1583660-form-773")
    button.click()

    try:
        first_name_elem = WebDriverWait(driver, 10)\
            .until(EC.presence_of_element_located((By.NAME, 'voter_firstname')))
        last_name_elem = WebDriverWait(driver, 10) \
            .until(EC.presence_of_element_located((By.NAME, 'voter_lastname')))
        email_elem = WebDriverWait(driver, 10) \
            .until(EC.presence_of_element_located((By.NAME, 'voter_email')))
        sleep(0.5)

        first_name_elem.send_keys(my_list[index][FIRST_NAME])
        last_name_elem.send_keys(my_list[index][LAST_NAME])
        email_elem.send_keys(my_list[index][EMAIL])
        email_elem.send_keys(Keys.TAB)
        submit_button_elem = driver.switch_to.active_element
        submit_button_elem.send_keys(Keys.ENTER)

        try:
            error_message = WebDriverWait(driver, 10) \
                .until(EC.visibility_of_element_located((By.CLASS_NAME, 'ajax_error')))
            count_already_voted += 1
            return 'already voted'
        except TimeoutException:
            try:
                success_elem = WebDriverWait(driver, 10)\
                    .until(EC.visibility_of_element_located((By.TAG_NAME, 'h5')))
                count_success += 1
                return 'success'
            except TimeoutException:
                count_no_confirmation += 1
                return ' ?'
    except TimeoutException:
        return 'timeout'


def main():
    file = open("list.txt")
    for line in file:
        my_list.append(line.split())
    random.shuffle(my_list)
    start = 0
    finish = len(my_list)
    for i in range(start, finish):
        message = cast_vote(i)
        print(f'[{i}. {message.upper()}] {my_list[i][EMAIL]} {my_list[i][FIRST_NAME]} {my_list[i][LAST_NAME]}')
        driver.refresh()
    print("quitting...")
    print(f"\nresults: of {finish - start} total, {count_success} successes, {count_already_voted} already voted, and "
          f"{count_no_confirmation} no confirmations. The rest, if any, errored out.")
    driver.quit()


if __name__ == '__main__':
    main()
