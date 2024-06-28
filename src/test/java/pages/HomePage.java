package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;


public class HomePage {

	WebDriver driver = null;
	Logger logger = LogManager.getLogger(HomePage.class);

	// locating web elements
	By searchBox = By.name("searchValue");

	// constructor
	public HomePage(WebDriver driver) {
		this.driver = driver;

	}
	
	// actions
	public void setTextInSearchBox(String text) throws Exception {
		driver.findElement(searchBox).sendKeys(text);
		logger.info("Setting Text Monitor into search box");
	}
	
	public void clickSearchButton() throws Exception{
		driver.findElement(searchBox).sendKeys(Keys.RETURN);
		logger.info("Searching Monitor...");
	}
	
	

}
