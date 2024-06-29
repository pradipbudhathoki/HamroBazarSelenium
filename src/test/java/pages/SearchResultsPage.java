package pages;

import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SearchResultsPage {
	WebDriver driver = null;
	Logger logger = LogManager.getLogger(SearchResultsPage.class);

	// locating web elements
	By locationSearchBox = By.xpath("(//input[@name = \"location\"])[2]");
	By dropdownMenu = By.xpath("//div[contains(@class, 'place--suggestions')]");
	By liElement = By.tagName("li");
	By sliderElement = By.xpath("//div[@class = 'rs-slider']");
	By sliderInput = By.xpath("(//input[@type = 'range'])[2]");
	By btnElement = By.xpath("(//button[@class = 'btn'])[2]");
	By filterElement = By.xpath("(//select[@name = 'sortParam'])[2]");

	// constructor
	public SearchResultsPage(WebDriver driver) {
		this.driver = driver;
	}

	// actions
	public void setTextInLocationBox(String location, String matchingLocation) {
		driver.findElement(locationSearchBox).sendKeys(location);
		logger.info("Setting location to New Road");
		selectFromDropdownMenu(matchingLocation);
		logger.info("Navigating to the location dropdown menu");
	}

	public void selectFromDropdownMenu(String text) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		WebElement dropdownDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownMenu));
		List<WebElement> listItems = dropdownDiv.findElements(liElement);

		for (WebElement listItem : listItems) {
			try {
				if (listItem.getText().contains(text)) {
					listItem.click();
					logger.info("Selecting Naya Sadak New Road Kathmandu from dropdown");
					break;
				}
			} catch (StaleElementReferenceException e) {
				dropdownDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownMenu));
				listItems = dropdownDiv.findElements(liElement);
				logger.error("Not able to chose Naya Sadak New Road Kathmandu from the dropdown menu");
			}
		}

	}

	public void clickFilterButton() {
		logger.info("Clicking the Filter button");
		driver.findElement(btnElement).click();
	}

	public void sliderSelect() {
		logger.info("Setting location radius from the slider");
		WebElement slider = driver.findElement(sliderElement);
		WebElement sInput = driver.findElement(sliderInput);
		
//		System.out.println(slider.getSize());
//		System.out.println(slider.getSize().getWidth());

		int sliderWidth = slider.getSize().getWidth();

		// Initialize Actions class
		Actions actions = new Actions(driver);

		int desiredValue = 251;
		int sliderMin = Integer.parseInt(sInput.getAttribute("aria-valuemin"));
		int sliderMax = Integer.parseInt(sInput.getAttribute("aria-valuemax"));
//		System.out.println(sliderMin);
//		System.out.println(sliderMax);

		double position = (double) (desiredValue - sliderMin) / (sliderMax - sliderMin);
		int xOffset = (int) (position * sliderWidth);
		actions.dragAndDropBy(slider, xOffset, 0).build().perform();
	}
	
	public void sortFilter(String visibleText) {
		logger.info("Choosing sorting filter from the dropdown");
		WebElement selectElement = driver.findElement(filterElement);
		Select selectObject = new Select(selectElement);
		selectObject.selectByVisibleText(visibleText);
	}

}
