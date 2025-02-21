from playwright.sync_api import sync_playwright

pw = sync_playwright().start()

browser = pw.firefox.launch()

page = browser.new_page()

page.goto("https://google.com")

print(page.content())

print(page.title())

page.screenshot(path="agent_screenshot_1.png")

browser.close

