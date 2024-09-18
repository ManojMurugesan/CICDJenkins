package Utils;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Media;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public abstract class Reporter {

    public ExtentSparkReporter spark;
    public static ExtentReports extent;
    public ExtentTest test, eachNode;
    public String Testcasename, Testdescription, AuthorName, Category, Environment, dataSheetName;
    public int number;
    

    @BeforeSuite
    public void StartReport() {
        spark = new ExtentSparkReporter("./Screenshots/results.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);
    }

    @BeforeClass
    public void report() {
        test = extent.createTest(Testcasename, Testdescription);
        test.assignAuthor("Author = " + AuthorName);
        test.assignCategory("Category = " + Category);
        test.assignDevice("Environment = " + Environment);
    }

    public abstract long takeSnap();

    public void reportsteps(String desc, String status, boolean bSnap) {

        
        if (status.equalsIgnoreCase("PASS")) {
            eachNode.pass(desc);
        } else if (status.equalsIgnoreCase("FAIL")) {
        	
        	Media img = null ;
            if (bSnap && !status.equalsIgnoreCase("INFO")) {

                long snapNumber = 100000L;
                snapNumber = takeSnap();
                img = MediaEntityBuilder.createScreenCaptureFromPath("./../Screenshots/" + snapNumber + ".png").build();
            }
            eachNode.fail(desc, img);
            eachNode.log(Status.FAIL, "Usage: BOLD TEXT");
            throw new RuntimeException();
        } else if (status.equalsIgnoreCase("WARNING")) {
            eachNode.warning(desc);
        } else if (status.equalsIgnoreCase("INFO")) {
            eachNode.info(desc);
        }
    }

    public void reportsteps(String description, String status) {

        reportsteps(description, status, true);
    }

    @AfterSuite
    public void stopReport() {
        extent.flush();
    }
}
