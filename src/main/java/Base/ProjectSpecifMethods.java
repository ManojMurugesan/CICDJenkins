package Base;

import java.io.IOException;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import Utils.DataLibrary;




	public class ProjectSpecifMethods extends SeleniumBase {

		@BeforeSuite
	    public void loaduserdata() {
	        loaddata("data");
	    }

	    @BeforeClass
	    public void Setup() {
	        eachNode = test.createNode(Testcasename);
	        StartApplication("Chrome", prop.getProperty("URL"));
	        
	    }

		
		@AfterClass
		public void teardown() {
			closeBrowser();
			driver.quit();
		}
		 
		@DataProvider(name = "fetchData")
		public Object[][] fetchData() throws IOException {
			return DataLibrary.readExcelData(dataSheetName, number);
		}	
		

	    @AfterSuite
	    public void unloaduserdata() {
	        unloaddata();
	        
	    }
	

}
