import { Builder } from 'selenium-webdriver';
import chrome from 'selenium-webdriver/chrome';
import config from '../config/config.js';

class DriverManager {
  static async createDriver() {
    const options = new chrome.Options();
    options.addArguments('--headless');

    const driver = await new Builder()
      .forBrowser('chrome')
      .setChromeOptions(options)
      .build();

    await driver.manage().window().maximize();
    await driver.manage().setTimeouts({
      implicit: config.timeouts.implicit,
      pageLoad: config.timeouts.pageLoad
    });

    return driver;
  }
}

export default DriverManager;