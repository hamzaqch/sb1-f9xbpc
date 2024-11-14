package com.mapbox.automation.utils;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class TestListener implements ITestListener {
    
    @Override
    public void onStart(ITestContext context) {
        ExtentReportManager.getInstance();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = ExtentReportManager.getInstance().createTest(result.getMethod().getMethodName());
        test.info(MarkupHelper.createLabel("Test Started", ExtentColor.BLUE));
        ExtentReportManager.setTest(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTest test = ExtentReportManager.getTest();
        test.log(Status.PASS, MarkupHelper.createLabel("Test Passed", ExtentColor.GREEN));
        test.pass("Test executed successfully");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = ExtentReportManager.getTest();
        test.log(Status.FAIL, MarkupHelper.createLabel("Test Failed", ExtentColor.RED));
        test.fail(result.getThrowable());

        if (Boolean.parseBoolean(ConfigReader.getProperty("take.screenshot.on.failure"))) {
            String base64Screenshot = takeScreenshot(result.getName());
            test.fail("Test Failed - Screenshot",
                MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTest test = ExtentReportManager.getTest();
        test.log(Status.SKIP, MarkupHelper.createLabel("Test Skipped", ExtentColor.YELLOW));
        test.skip(result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReportManager.getInstance().flush();
    }

    private String takeScreenshot(String testName) {
        try {
            TakesScreenshot driver = (TakesScreenshot) DriverManager.getDriver();
            File screenshot = driver.getScreenshotAs(OutputType.FILE);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String screenshotDir = ConfigReader.getProperty("screenshot.dir");
            new File(screenshotDir).mkdirs();
            File destinationFile = new File(screenshotDir + "/" + testName + "_" + timestamp + ".png");
            Files.copy(screenshot.toPath(), destinationFile.toPath());
            
            byte[] fileContent = Files.readAllBytes(destinationFile.toPath());
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}