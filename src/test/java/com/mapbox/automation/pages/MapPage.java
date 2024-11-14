package com.mapbox.automation.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;

public class MapPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;

    @FindBy(css = ".mapboxgl-map")
    private WebElement mapContainer;

    @FindBy(css = ".mapboxgl-ctrl-zoom-in")
    private WebElement zoomInButton;

    @FindBy(css = ".mapboxgl-ctrl-zoom-out")
    private WebElement zoomOutButton;

    @FindBy(css = ".mapboxgl-style-switcher")
    private WebElement styleSwitcher;

    public MapPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(
            Integer.parseInt(ConfigReader.getProperty("explicit.wait"))));
        this.actions = new Actions(driver);
        PageFactory.initElements(driver, this);
    }

    public boolean isMapVisible() {
        wait.until(ExpectedConditions.visibilityOf(mapContainer));
        return mapContainer.isDisplayed();
    }

    public boolean areZoomControlsPresent() {
        return zoomInButton.isDisplayed() && zoomOutButton.isDisplayed();
    }

    public void clickZoomIn() {
        wait.until(ExpectedConditions.elementToBeClickable(zoomInButton)).click();
        // Wait for zoom animation to complete
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void clickZoomOut() {
        wait.until(ExpectedConditions.elementToBeClickable(zoomOutButton)).click();
        // Wait for zoom animation to complete
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public double getCurrentZoomLevel() {
        return Double.parseDouble(executeMapboxJS("return map.getZoom()"));
    }

    public void panMap(String direction) {
        int offsetX = direction.equals("right") ? 200 : -200;
        actions.moveToElement(mapContainer)
               .clickAndHold()
               .moveByOffset(offsetX, 0)
               .release()
               .perform();
    }

    public double[] getMapCenter() {
        String centerStr = executeMapboxJS("return map.getCenter().toArray()");
        String[] coords = centerStr.replace("[", "").replace("]", "").split(",");
        return new double[] {
            Double.parseDouble(coords[0]),
            Double.parseDouble(coords[1])
        };
    }

    public void switchMapStyle(String style) {
        executeMapboxJS("map.setStyle('mapbox://styles/mapbox/" + style + "')");
        // Wait for style to load
        wait.until(webDriver -> executeMapboxJS("return map.isStyleLoaded()").equals("true"));
    }

    public String getCurrentStyle() {
        return executeMapboxJS("return map.getStyle().name");
    }

    private String executeMapboxJS(String script) {
        return ((JavascriptExecutor) driver).executeScript(script).toString();
    }
}