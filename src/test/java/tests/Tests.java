package tests;

import org.testng.annotations.Test;
import org.testng.Assert;
import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import io.github.bonigarcia.wdm.WebDriverManager;
import pages.FinalResultPage;
import pages.HomePage;
import pages.SearchResultsPage;

public class Tests {

	ExtentHtmlReporter htmlReporter;
	ExtentReports extent;
	WebDriver driver;
	ExtentTest test;
	String baseUrl = "https://hamrobazaar.com";
	Logger logger = LogManager.getLogger(Tests.class);

	@BeforeSuite
	public void setup() {
		htmlReporter = new ExtentHtmlReporter("extentReport.html");

		// create ExtentReports and attach reporter(s)
		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);

	}

	@BeforeTest
	public void setUpTest() {
		logger.info("Test Start");
		WebDriverManager.chromedriver().setup();
		ChromeOptions chromeOptions = new ChromeOptions();

		// set page load strategy to normal
		chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);

		// set browser to headless chrome
//	    chromeOptions.addArguments("--headless");

		driver = new ChromeDriver(chromeOptions);

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		driver.manage().timeouts().scriptTimeout(Duration.ofMinutes(2));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));

	}

	@Test
	public void Test1() throws Exception {
		// creates a toggle for the given test, adds all log events under it
		test = extent.createTest("HamroBazar Test with TestNG",
				"This is test to validate HamroBazar search and filter test with TestNG");

		test.log(Status.INFO, "Starting Test Case");

		HomePage homePageObj = new HomePage(driver);
		SearchResultsPage searchPageObj = new SearchResultsPage(driver);
		FinalResultPage resultPageObj = new FinalResultPage(driver);

		driver.get(baseUrl);
		logger.info("Navigating to HamroBazar");
		test.pass("Navigated to HamroBazar");

		driver.manage().window().maximize();

		try {

			// Search for Monitor
			homePageObj.setTextInSearchBox("Monitor");
			homePageObj.clickSearchButton();
			test.pass("Searched Monitor");

			// Select location New Road
			searchPageObj.setTextInLocationBox("New Road", "naya sadak newroad, New Road, Kathmandu");
			test.pass("Selected Location New Road");

			// Set location Radius
			searchPageObj.sliderSelect();
			test.pass("Set Location Radius");
			Thread.sleep(1000);

			// Filter the response
			searchPageObj.clickFilterButton();
			test.pass("Filtered the response");
			Thread.sleep(1000);

			// sort the response
			searchPageObj.sortFilter("Low to High (Price)");
			test.pass("Sorted the response by Low to High (Price)");
			
			// extract the csv report and display
			resultPageObj.extractReport(50);
			test.pass("Collected the Report in CSV");
			test.pass("Displayed the CSV result");

		} catch (Exception e) {
			Assert.fail("Test Failed : " + e.getMessage());
			test.addScreenCaptureFromPath("screenshot.png");
			e.printStackTrace();
		}

		

	}

	@AfterSuite
	public void TearDown() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		driver.close();

		driver.quit();
		test.info("Test completed");
		logger.info("Test Completed");
		
		// calling flush
		extent.flush();

	}

}
