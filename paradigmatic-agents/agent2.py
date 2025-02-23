from playwright.sync_api import sync_playwright
import pandas as pd
import time
import os

# create the agent function
def scrape_indeed(playwright):
    # initiate the browser
    user_data_dir = os.path.join(os.getcwd(), "playwright_data")
    os.makedirs(user_data_dir, exist_ok=True)

    browser = playwright.chromium.launch_persistent_context(
        user_data_dir,
        channel="chrome",
        no_viewport=True,
    )

    # initiate the page
    page = browser.new_page()

    # keeping track of page count
    page_count = 0

    jobs = []

    # scraping the list with vacancies (change 2 if you want to scrape more pages)
    while page_count < 2:
        print("scraping list items")
        # setting s delay to avoid sending too many requests
        time.sleep(2)

        page.goto('https://www.indeed.com/jobs?q=python+developer&start=' + str(page_count * 10))

        # getting the list of vacancies
        vacancies = page.locator('cardOutline')

        for vacancy in vacancies.element_handles():
            # extracting the attributes of every vacancy
            item = {}

            item['Title'] = vacancy.query_selector("h2").inner_text()

            item['URL'] = "www.indeed.com/q-Python-Developer-Remote-jobs.html" + vacancy.query_selector("a").get_attribute("href")

            jobs.append(item)
        
        # increment the page count
        page_count += 1

    all_items = []

    # deep scraping 
    for job in jobs:
        print("SCRAPING DETAILS PAGE")

        page.goto(job['URL'])

        time.sleep(2)

        item = {}

        item['Title'] = job['Title']
        itemm['URL'] = job['URL']
        item['CompanyName'] = ""
        item['Location'] = ""
        item['SalaryInfo'] = ""

        # getting the company name
        company_name = page.locator("span.jobsearch-JobInfoHeader-companyNameSimple").inner_text()

        if (company_name.count()) > 0: 
            item['CompanyName'] = company_name.inner_text()

        # getting the company location
        company_location = page.get_by_test_id("job-location")

        if company_location.count() > 0: 
            item['Location'] = company_location.inner_text()

        
        # getting the salary information
        salaryinfo = page.locator("span[class*='js-match-insights-provider']")

        if (salaryinfo.count() > 0):
            item['SalaryInfo'] = salaryinfo.inner_text

        all_items.append(item)
    # closing the browser
    browser.close()

    return all_items


with sync_playwright() as playwright:
    # executing the scrap_indeed() funciton and saving the result in jobs
    jobs = scrape_indeed(playwright)

    # writing to excel file
    df = pd.DataFrame(jobs)
    df.to_excel("jobs.xlsx", index=False)






