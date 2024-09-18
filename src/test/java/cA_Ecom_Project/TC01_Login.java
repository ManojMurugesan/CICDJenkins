package cA_Ecom_Project;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import Base.ProjectSpecifMethods;
import Pages.Carts;


public class TC01_Login extends ProjectSpecifMethods {
	@BeforeTest
	public void setvalues() {
		Testcasename = "Green Cart Application";
		Testdescription = "Green Cart Application";
		AuthorName = "TESTER";
		Category = "SMOKE";
		Environment = "STAGE";
		dataSheetName = "Products";
		number = 0;
	}

	@Test(dataProvider = "fetchData")
	public void loginapp(String product) {

		
		new Carts(driver, eachNode).Greencart(product);
	

	}

}