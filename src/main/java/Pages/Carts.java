package Pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentTest;

import Base.SeleniumBase;

public class Carts extends SeleniumBase {

	public WebDriverWait wait;

	public Carts(RemoteWebDriver driver, ExtentTest eachNode) {
		this.driver = driver;
		this.eachNode = eachNode;

	}

	public Carts Greencart(String product) {

		
		List<WebElement> productList = locateElements("cssSelect", "h4.product-name");

		for (int i = 0; i < productList.size(); i++) {
			String[] veggies = productList.get(i).getText().split("-");
			String veg = veggies[0].trim();

			if (product.contains(veg)) {
				driver.findElements(By.xpath("//div[@class='product-action']/button")).get(i).click();

			}
		}
		WebElement cart = locateElement("xpath", "//img[@alt='Cart']");
		click(cart);
		WebElement checkout = locateElement("xpath", "//button[text()='PROCEED TO CHECKOUT']");
		click(checkout);
		WebElement promo = locateElement("cssSelect", "input.promoCode");
		clearandType(promo, "rahulshettyacademy");

		WebElement promobtn = locateElement("cssSelect", "button.promoBtn");
		click(promobtn);

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(6));
		WebElement Code = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span.promoInfo")));
		getElementText(Code);

		return this;
	}

}