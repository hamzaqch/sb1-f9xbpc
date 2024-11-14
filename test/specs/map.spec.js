import { expect } from 'chai';
import DriverManager from '../utils/driver.js';
import MapPage from '../pages/MapPage.js';
import config from '../config/config.js';

describe('Mapbox Map Tests', function() {
  let driver;
  let mapPage;

  before(async function() {
    driver = await DriverManager.createDriver();
    mapPage = new MapPage(driver);
  });

  after(async function() {
    if (driver) {
      await driver.quit();
    }
  });

  it('should load map successfully', async function() {
    await mapPage.navigateTo(config.baseUrl);
    const isVisible = await mapPage.isMapVisible();
    expect(isVisible).to.be.true;
  });

  it('should have correct map container size', async function() {
    const size = await mapPage.getMapContainerSize();
    expect(size.width).to.not.equal('0px');
    expect(size.height).to.not.equal('0px');
  });
});