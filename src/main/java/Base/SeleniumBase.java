package Base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import Utils.Reporter;
import io.github.bonigarcia.wdm.WebDriverManager;

public abstract class SeleniumBase extends Reporter {

	public RemoteWebDriver driver;
	public static Properties prop;

	public void StartApplication(String browser, String url) {

		try {
			if (browser.equalsIgnoreCase("Chrome")) {
				WebDriverManager.chromedriver().setup();
				driver = new ChromeDriver();
			} else if (browser.equalsIgnoreCase("Edge")) {
				WebDriverManager.edgedriver().setup();
				driver = new EdgeDriver();
			} else if (browser.equalsIgnoreCase("Firefox")) {
				WebDriverManager.firefoxdriver().setup();
				driver = new FirefoxDriver();
			}
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
			driver.get(url);
			driver.manage().window().maximize();
			reportsteps("The browser: " + browser + " launched successfully", "PASS");
		} catch (WebDriverException e) {
			reportsteps("The browser: " + browser + " could not be launched", "FAIL");
		}

	}

	public WebElement locateElement(String locator, String locValue) {

		try {
			switch (locator) {
			case "id":
				return driver.findElement(By.id(locValue));
			case "class":
				return driver.findElement(By.className(locValue));
			case "name":
				return driver.findElement(By.name(locValue));
			case "link":
				return driver.findElement(By.linkText(locValue));
			case "partialLink":
				return driver.findElement(By.partialLinkText(locValue));
			case "tagname":
				return driver.findElement(By.tagName(locValue));
			case "xpath":
				return driver.findElement(By.xpath(locValue));
			case "cssSelect":
				return driver.findElement(By.cssSelector(locValue));
			}

		} catch (NoSuchElementException e) {
			reportsteps("The element with locator " + locValue + " not found.", "FAIL");
		} catch (WebDriverException e) {
			reportsteps("Unknown exception occured while finding " + locator + " with the value " + locValue, "FAIL");
		}
		return null;
	}

	public List<WebElement> locateElements(String type, String locValue) {
		try {
			switch (type) {
			case "id":
				return driver.findElements(By.id(locValue));
			case "class":
				return driver.findElements(By.className(locValue));
			case "name":
				return driver.findElements(By.name(locValue));
			case "link":
				return driver.findElements(By.linkText(locValue));
			case "partialLink":
				return driver.findElements(By.partialLinkText(locValue));
			case "tagname":
				return driver.findElements(By.tagName(locValue));
			case "xpath":
				return driver.findElements(By.xpath(locValue));
			case "cssSelect":
				return driver.findElements(By.cssSelector(locValue));
			}
		} catch (NoSuchElementException e) {
			reportsteps("The element with locator " + type + " not found.", "FAIL");
		} catch (WebDriverException e) {
			reportsteps("Unknown exception occured while finding " + type + " with the value " + locValue, "FAIL");
		}
		return null;
	}

	public void loaddata(String filename) {
		prop = new Properties();
		try {
			prop.load(new FileInputStream(new File("./Data/" + filename + ".properties")));
		} catch (FileNotFoundException e) {

			System.out.println("Print");
		} catch (IOException e) {

			System.out.println("Print");
		}
	}

	public void unloaddata() {
		prop = null;
	}

	public long takeSnap() {
		long number = (long) Math.floor(Math.random() * 900000000L) + 10000000L;
		try {
			FileUtils.copyFile(driver.getScreenshotAs(OutputType.FILE), new File("./Screenshots/" + number + ".png"));
		} catch (WebDriverException e) {
			System.out.println("The browser has been closed.");
		} catch (IOException e) {
			System.out.println("The snapshot could not be taken");
		}
		return number;
	}

	public WebElement locateElement(String locValue) {
		return driver.findElement(By.id(locValue));
	}

	public void click(WebElement ele) {
		String text = "";
		try {
			text = ele.getText();
			ele.click();
			reportsteps("The element " + text + " is clicked", "PASS");
		} catch (InvalidElementStateException e) {
			reportsteps("The element: " + text + " could not be clicked", "FAIL");
		} catch (WebDriverException e) {
			reportsteps("Unknown exception occured while clicking in the field :", "FAIL");
		}
	}

	public void click(WebElement ele, String elementname) {

		try {

			ele.click();
			reportsteps("The element " + elementname + " is clicked", "PASS");
		} catch (InvalidElementStateException e) {
			reportsteps("The element: " + elementname + " could not be clicked", "FAIL");
		} catch (WebDriverException e) {
			reportsteps("Unknown exception occured while clicking in the field :", "FAIL");
		}
	}

	public String getTitle() {
		String bReturn = "";
		try {
			bReturn = driver.getTitle();
			reportsteps("The title of the application is " + bReturn + "", "PASS");
		} catch (WebDriverException e) {
			reportsteps("Unknown Exception Occured While fetching Title", "FAIL");
		}
		return bReturn;
	}

	public boolean verifyTitle(String title) {
		boolean bReturn = false;
		try {
			if (getTitle().equals(title)) {
				reportsteps("The title of the page matches with the value :" + title, "PASS");
				bReturn = true;
			} else {
				reportsteps("The title of the page:" + driver.getTitle() + " did not match with the value :" + title,
						"FAIL");
			}
		} catch (WebDriverException e) {
			reportsteps("Unknown exception occured while verifying the title", "FAIL");
		}
		return bReturn;
	}

	public void clearandType(WebElement emailid, String key) {

		try {
			emailid.sendKeys(key);
			reportsteps("The value :" + key + " is enetred in the text box", "PASS");
		} catch (StaleElementReferenceException e) {
			reportsteps("The element" + emailid + " is not interactable", "FAIL");
		}

	}

	public boolean verifyDisplayed(WebElement ele, String elementname) {
		try {
			if (ele.isDisplayed()) {
				reportsteps("The element " + elementname + " is visible", "PASS");
				return true;
			} else {
				reportsteps("The element " + elementname + " is not visible", "FAIL");
			}
		} catch (WebDriverException e) {
			reportsteps("WebDriverException : " + e.getMessage(), "FAIL");
		}
		return false;
	}

	public void verifyDisplayed(WebElement ele) {
		String text = "";
		try {
			text = ele.getText();
			if (ele.isDisplayed()) {
				reportsteps("The element " + text + " is visible", "PASS");

			} else {
				reportsteps("The element " + text + " is not visible", "FAIL");
			}
		} catch (WebDriverException e) {
			reportsteps("WebDriverException : " + e.getMessage(), "FAIL");
		}

	}

	public void scrolldown() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0, 500)", "");
	}

	public void scrolltoview(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);

	}

	public void movetoelement(WebElement element) {
		Actions hover = new Actions(driver);
		hover.moveToElement(element).build().perform();
	}

	public void movetoelementandclick(WebElement element) {

		String text = "";
		try {
			text = element.getText();
			Actions hover = new Actions(driver);
			hover.moveToElement(element).click().build().perform();
			reportsteps("The element " + text + " is clicked", "PASS");
		} catch (InvalidElementStateException e) {
			reportsteps("The element: " + text + " could not be clicked", "FAIL");
		} catch (WebDriverException e) {
			reportsteps("Unknown exception occured while clicking in the field :", "FAIL");
		}

	}

	public void movetoelementandclick(WebElement ele, String elementname) {

		try {
			Actions hover = new Actions(driver);
			hover.moveToElement(ele).click().build().perform();
			reportsteps("The element " + elementname + " is clicked", "PASS");
		} catch (InvalidElementStateException e) {
			reportsteps("The element: " + elementname + " could not be clicked", "FAIL");
		} catch (WebDriverException e) {
			reportsteps("Unknown exception occured while clicking in the field :", "FAIL");
		}
	}

	public String getElementText(WebElement ele, String elementname) {
		String text = "";
		try {
			text = ele.getText();
			reportsteps("The element text is" + elementname + "", "PASS");
		} catch (WebDriverException e) {
			reportsteps("The element text : " + elementname + " could not be found.", "FAIL");
		}
		return text;
	}

	public String getElementText(WebElement ele) {
		String text = "";
		try {
			text = ele.getText();
			reportsteps("The element " + text + " is displayed", "PASS");
		} catch (WebDriverException e) {
			reportsteps("The element: " + ele + " could not be found.", "FAIL");
		}
		return text;
	}

	/*
	 * public void clickanelementwithmatchingtext(List<WebElement> ele, String
	 * elementname) {
	 * 
	 * try { ele.stream().parallel().filter(e ->
	 * e.getText().equalsIgnoreCase(elementname)).findFirst()
	 * .ifPresent(WebElement::click); reportsteps("The element " + elementname +
	 * " is displayed and cliked", "PASS"); } catch (WebDriverException e) {
	 * reportsteps("The element: " + elementname + " could not be found.", "FAIL");
	 * }
	 * 
	 * }
	 * 
	 * public void clickanelementwithmatchingtextcontains(List<WebElement> ele,
	 * String elementname) {
	 * 
	 * try { ele.stream().parallel().filter(e ->
	 * e.getText().contains(elementname)).findFirst() .ifPresent(WebElement::click);
	 * 
	 * reportsteps("The element " + elementname + " is displayed and cliked",
	 * "PASS"); } catch (WebDriverException e) { reportsteps("The element: " +
	 * elementname + " could not be found.", "FAIL"); }
	 * 
	 * }
	 */

	public boolean elementpresentclick(WebElement ele, String elementname) {

		try {
			if (ele.isDisplayed()) {
				ele.click();
				reportsteps("The element " + elementname + " is displayed and clicked", "PASS");
				return true;

			} else {
				reportsteps("The element " + elementname + " is not displayed and", "FAIL");
			}

		} catch (InvalidElementStateException e) {
			reportsteps("The element: " + elementname + " could not be clicked", "FAIL");
		} catch (WebDriverException e) {
			reportsteps("Unknown exception occured while clicking in the field :", "FAIL");
		}
		return false;
	}

	public void waits(int count) {
		for (int i = 1; i <= count; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void selectDropDownUsingText(WebElement ele, String value) {
		try {
			new Select(ele).selectByVisibleText(value);
			reportsteps("The dropdown is selected with text " + value, "PASS");
		} catch (WebDriverException e) {
			reportsteps("The element: " + ele + " could not be found.", "FAIL");
		}
	}

	public void selectDropDownUsingIndex(WebElement ele, int index) {
		try {
			new Select(ele).selectByIndex(index);
			reportsteps("The dropdown is selected with index " + index, "PASS");
		} catch (WebDriverException e) {
			reportsteps("The element: " + ele + " could not be found.", "FAIL");
		}
	}

	public void selectDropDownUsingValue(WebElement ele, String value) {
		try {
			new Select(ele).selectByValue(value);
			reportsteps("The dropdown is selected with text " + value, "PASS");
		} catch (WebDriverException e) {
			reportsteps("The element: " + ele + " could not be found.", "FAIL");
		}
	}

	public boolean stringmatch(String cost1, String cost2) {

		try {
			if (cost1.equals(cost2)) {
				reportsteps("The value " + cost1 + " &  " + cost2
						+ " is matching with eachother", "PASS");
				return true;
			} else {
				reportsteps("The value " + cost1 + " &  " + cost2
						+ " is not matching with eachother", "FAIL");
			}

		} catch (WebDriverException e) {
			reportsteps("The items original cost " + cost1 + " & the total cost " + cost2 + " is not found", "FAIL");
		}
		return false;

	}

	public void verifySelected(WebElement ele) {
		try {
			if (ele.isSelected()) {
				reportsteps("The element " + ele + " is selected", "PASS");
			} else {
				reportsteps("The element " + ele + " is not selected", "FAIL");
			}
		} catch (WebDriverException e) {
			reportsteps("WebDriverException : " + e.getMessage(), "FAIL");
		}
	}

	public void switchToWindow(String title) {
		try {
			Set<String> allWindows = driver.getWindowHandles();
			for (String eachWindow : allWindows) {
				driver.switchTo().window(eachWindow);
				if (driver.getTitle().equals(title)) {
					break;
				}
				reportsteps("The Window With Title :" + title + " is switched", "PASS");
			}
		} catch (NoSuchWindowException e) {
			reportsteps("\"The Window With Title: " + title + " not found", "FAIL");
		} catch (WebDriverException e) {
			reportsteps("WebDriverException : " + e.getMessage(), "FAIL");
		}

	}

	public void switchToWindow(int index) {
		try {
			Set<String> allWindowHandles = driver.getWindowHandles();
			List<String> allHandles = new ArrayList<>();
			allHandles.addAll(allWindowHandles);
			driver.switchTo().window(allHandles.get(index));
		} catch (NoSuchWindowException e) {
			reportsteps("The driver could not move to the given window by index " + index, "PASS");
		} catch (WebDriverException e) {
			reportsteps("WebDriverException : " + e.getMessage(), "FAIL");
		}
	}

	public void driverwait() {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	}

	public void calendar() {

		// Logics need to be developed

	}

	public void printlist(List<WebElement> ele) {

		try {
			for (int i = 0; i < ele.size(); i++) {
				// obtain text
				String s = ele.get(i).getText();
				reportsteps("The list is: " + s + "", "PASS");
			}
		} catch (NoSuchElementException e) {
			reportsteps("The list " + ele + " is not found", "FAIL");
		}

	}

	public void closeBrowser() {
		try {
			driver.close();
			reportsteps("The browser is closed", "PASS", false);
		} catch (Exception e) {
			reportsteps("The browser could not be closed", "FAIL", false);
		}
	}

	public void JsonText(WebElement text) {
		try {
			JSONObject jsonobject = new JSONObject(text);

			System.out.println(jsonobject.getString("text"));
			reportsteps("The JSON value is", "PASS", false);
		} catch (Exception e) {
			reportsteps("Unable to get the JSOn value", "FAIL", false);
		}
	}

	public String GetJsonText(String jSON) {
		String text = "";
		try {
			
			JSONObject jsonobject = new JSONObject(jSON);
			text = jsonobject.getString("jSON");
			//System.out.println(jsonobject.getString("text"));
			
			reportsteps("The JSON value " + text + " is visible", "PASS", false);
		} catch (WebDriverException e) {
			reportsteps("The element: " + text + " could not be found.", "FAIL");
		}
		return text;
	}
	
	public void isImageBroken(WebElement image) {
		try {
			if (image.getAttribute("naturalWidth").equals("0")) {
				image.getAttribute("outerHTML");
				reportsteps("The image is broken", "FAIL");
			} else {

				reportsteps("The image is visible", "PASS", false);
			}

		} catch (Exception e) {

		}

	}
	
	public void webdriverwait(String ele)
	{
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ele)));
	}
	
	public void AssertEquals(String actual, String expected)
	{
		try {
			Assert.assertEquals(actual, expected);
			reportsteps("The "+actual+" and " +expected+ " values are matching", "PASS");
		} catch (Exception e) {
			reportsteps("The "+actual+" and " +expected+ " values not are matching", "FAIL");
		}
	}
	
	public void softassert(String actual, String expected) {
		SoftAssert a = new SoftAssert();
		try {
			
			a.assertEquals(actual, expected);
			reportsteps("The " + actual + " and " + expected + " values are matching", "PASS",false);

		} catch (Exception e) {
			reportsteps("The " + actual + " and " + expected + " values not are matching", "FAIL");
		}
		a.assertAll();
	}

}
