package com.mapbox.automation.steps;

import com.mapbox.automation.pages.MapPage;
import com.mapbox.automation.utils.ConfigReader;
import com.mapbox.automation.utils.DriverManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class MapSteps {
    private WebDriver driver = DriverManager.getDriver();
    private MapPage mapPage = new MapPage(driver);
    private double previousZoomLevel;
    private double[] previousCenter;

    @Given("I am on the Mapbox map page")
    public void iAmOnTheMapboxMapPage() {
        driver.get(ConfigReader.getProperty("base.url"));
    }

    @Then("the map should be visible")
    public void theMapShouldBeVisible() {
        Assert.assertTrue(mapPage.isMapVisible(), "Map is not visible");
    }

    @Then("the zoom controls should be present")
    public void theZoomControlsShouldBePresent() {
        Assert.assertTrue(mapPage.areZoomControlsPresent(), "Zoom controls are not present");
    }

    @When("I click the zoom in button")
    public void iClickTheZoomInButton() {
        previousZoomLevel = mapPage.getCurrentZoomLevel();
        mapPage.clickZoomIn();
    }

    @When("I click the zoom out button")
    public void iClickTheZoomOutButton() {
        previousZoomLevel = mapPage.getCurrentZoomLevel();
        mapPage.clickZoomOut();
    }

    @Then("the map zoom level should increase")
    public void theMapZoomLevelShouldIncrease() {
        double currentZoomLevel = mapPage.getCurrentZoomLevel();
        Assert.assertTrue(currentZoomLevel > previousZoomLevel, 
            "Zoom level did not increase");
    }

    @Then("the map zoom level should decrease")
    public void theMapZoomLevelShouldDecrease() {
        double currentZoomLevel = mapPage.getCurrentZoomLevel();
        Assert.assertTrue(currentZoomLevel < previousZoomLevel, 
            "Zoom level did not decrease");
    }

    @When("I pan the map to the {word}")
    public void iPanTheMap(String direction) {
        previousCenter = mapPage.getMapCenter();
        mapPage.panMap(direction);
    }

    @Then("the map center coordinates should change")
    public void theMapCenterCoordinatesShouldChange() {
        double[] currentCenter = mapPage.getMapCenter();
        Assert.assertNotEquals(currentCenter, previousCenter, 
            "Map center coordinates did not change after panning");
    }

    @When("I switch to {string} style")
    public void iSwitchToStyle(String style) {
        mapPage.switchMapStyle(style);
    }

    @Then("the map style should change to {string}")
    public void theMapStyleShouldChangeTo(String expectedStyle) {
        String currentStyle = mapPage.getCurrentStyle();
        Assert.assertEquals(currentStyle, expectedStyle, 
            "Map style did not change as expected");
    }
}