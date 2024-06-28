package pages;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class FinalResultPage {

	WebDriver driver = null;
	Logger logger = LogManager.getLogger(FinalResultPage.class);

	// locating web elements
	By productElement = By.xpath("//div[@data-item-index='0']");
	By titleElement = By.xpath(".//h2[@class='product-title']");
	By descriptionElement = By.xpath(".//p[@class='description']");
	By priceElement = By.xpath(".//span[@class='regularPrice']");
	By conditionElement = By.xpath(".//span[@class='condition']");
	By timeElement = By.xpath(".//div[@class='locationAndTime']/span[@class='time']");
	By sellerElement = By.xpath(".//div[@class='username']/span[@class='username-fullname']");

	FileWriter fileWriter = null;
	PrintWriter printWriter = null;

	// constructor
	public FinalResultPage(WebDriver driver) {
		this.driver = driver;
	}

	public void extractReport(int numberOfRecords) {
		logger.info("Extracting the reports in the CSV");

		try {

			// Delete the existing CSV file if it exists
			File csvFile = new File("Search_Result.csv");
			if (csvFile.exists()) {
				csvFile.delete();
			}

			fileWriter = new FileWriter("Search_Result.csv");
			printWriter = new PrintWriter(fileWriter);
			printWriter.println(
					"Title of the product,Description of the product,Price of the product,Condition of the product,Ad posted date,Name of the seller");

			for (int i = 0; i < numberOfRecords; i++) {
				String xpath = String.format("//div[@data-item-index='%d']", i);
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
				List<WebElement> productDivs = driver.findElements(By.xpath(xpath));

				if (productDivs.isEmpty()) {
					((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300);");

					try {
						wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
						logger.info("Waiting for element to appear");
					} catch (TimeoutException e) {
						logger.warn("New elements did not load in time.");
					}

					productDivs = driver.findElements(By.xpath(xpath));
				}

				if (!productDivs.isEmpty()) {
					WebElement productDiv = productDivs.get(0); // Assuming there is only one element at each index

					String title = sanitize(getProductTitle(productDiv));
					String description = sanitize(getProductDescription(productDiv));
					String price = sanitize(getProductPrice(productDiv));
					String condition = sanitize(getProductCondition(productDiv));
					String postedDate = sanitize(getProductPostedDate(productDiv));
					String sellerName = sanitize(getSellerName(productDiv));
//					System.out.println(i + " : " + title);
					logger.info(i + " : " + title);

					// Write to CSV
					printWriter.printf("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"%n", title, description, price,
							condition, postedDate, sellerName);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (printWriter != null) {
				printWriter.close();
			}
			try {
				if (fileWriter != null) {
					fileWriter.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		// Display the CSV file in a tabular format
		displayCSVFile("Search_Result.csv");
	}

	// Title
	public String getProductTitle(WebElement product) {
		return product.findElement(titleElement).getText().trim();
	}

	// Description
	public String getProductDescription(WebElement product) {
		return product.findElement(descriptionElement).getText().trim();
	}

	// Product Price
	public String getProductPrice(WebElement product) {
		return product.findElement(priceElement).getText().trim();
	}

	// Product Condition
	public String getProductCondition(WebElement product) {
		return product.findElement(conditionElement).getText().replace("| ", "").trim();
	}

	// Product Posted date
	public String getProductPostedDate(WebElement product) {
		return product.findElement(timeElement).getText().trim();
	}

	// Seller Name
	public String getSellerName(WebElement product) {
		return product.findElement(sellerElement).getText().trim();
	}

	// Method to sanitize data by removing newlines and extra commas
	private String sanitize(String data) {
		if (data == null) {
			return "";
		}
		return data.replaceAll("[\r\n,\"]", " ").trim();
	}

	// Method to display CSV file in tabular format
	private void displayCSVFile(String filePath) {
		logger.info("Displaying the CSV file");
		CSVReader reader = null;
		try {
			reader = new CSVReader(new FileReader(filePath));
			List<String[]> lines = reader.readAll();

			// Find the maximum width for each column
			int[] maxWidths = new int[lines.get(0).length];
			for (String[] line : lines) {
				for (int i = 0; i < line.length; i++) {
					if (line[i].length() > maxWidths[i]) {
						maxWidths[i] = line[i].length();
					}
				}
			}

			// Print the content with padding to align columns
			for (String[] line : lines) {
				for (int i = 0; i < line.length; i++) {
					System.out.printf("%-" + (maxWidths[i] + 2) + "s", line[i]);
				}
				System.out.println();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CsvException e) {
			e.printStackTrace();
		} finally {
		}
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
