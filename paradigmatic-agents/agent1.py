from playwright.sync_api import sync_playwright
from urllib.request import urlretrieve

pw = sync_playwright().start()

user_data_dir = os.path.join(os.getcwd(), "data")
os.makedirs(user_data_dir, exist_ok=True)

browser = pw.firefox.launch(
    user_data_dir,
    headless=False,
    # slow_mo=2000,
)

page = browser.new_page()

page.goto("https://arxiv.org/search")

# Wait for the search box to be visible and interactable
search_box = page.wait_for_selector('input[placeholder="Search term..."]')
search_box.fill("neural networks")
# search_box.press('Enter)


search_button = page.get_by_role("button", name="Search").nth(1)
search_button.click()

pdf_links = page.locator(
    "xpath=//a[contains(@href, 'arxiv.org/pdf')]"
).all()

for pdf_link in pdf_links:
    print(pdf_link.get_attribute("href"))
    url = pdf_link.get_attribute("href")
    urlretrieve(url, "data/" + url[-5:] + ".pdf")

print(page.title())

page.screenshot(path="agent_screenshot_1.png")

browser.close()
pw.stop()

