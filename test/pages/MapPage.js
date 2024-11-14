import { By, until } from 'selenium-webdriver';

class MapPage {
  constructor(driver) {
    this.driver = driver;
    this.mapContainer = By.css('#map');
    this.mapCanvas = By.css('.mapboxgl-canvas');
  }

  async navigateTo(url) {
    await this.driver.get(url);
  }

  async isMapVisible() {
    try {
      const canvas = await this.driver.wait(
        until.elementLocated(this.mapCanvas),
        10000
      );
      return await canvas.isDisplayed();
    } catch (error) {
      return false;
    }
  }

  async getMapContainerSize() {
    const container = await this.driver.findElement(this.mapContainer);
    return {
      width: await container.getCssValue('width'),
      height: await container.getCssValue('height')
    };
  }
}

export default MapPage;