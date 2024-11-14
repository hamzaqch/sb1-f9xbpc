package com.mapbox.automation.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import com.mapbox.automation.utils.DriverManager;
import com.mapbox.automation.utils.ExtentReportManager;

@CucumberOptions(
    features = "src/test/resources/features",
    glue = "com.mapbox.automation.steps",
    plugin = {
        "pretty",
        "html:target/cucumber-reports/cucumber-pretty.html",
        "json:target/cucumber-reports/CucumberTestReport.json",
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
    }
)
public class TestRunner extends AbstractTestNGCucumberTests {
    @AfterClass
    public void tearDown() {
        DriverManager.quitDriver();
    }

    @AfterSuite
    public void generateReport() {
        ExtentReportManager.getInstance().flush();
    }
}