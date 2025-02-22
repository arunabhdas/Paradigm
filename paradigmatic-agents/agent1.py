from playwright.sync_api import sync_playwright

pw = sync_playwright().start()

browser = pw.firefox.launch(
    headless=False,
    slow_mo=2000,
)

page = browser.new_page()

page.goto("https://arxiv.org/search")

# Wait for the search box to be visible and interactable
search_box = page.wait_for_selector('input[placeholder="Search term..."]')
search_box.fill("neural network")
# search_box.press('Enter)

page.get_by_role("button", name="Search").nth(1).click()


print(page.title())

page.screenshot(path="agent_screenshot_1.png")

browser.close()
pw.stop()

