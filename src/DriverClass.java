/**
 * 
 */
import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Recordset;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.openqa.selenium.WebDriver;

import java.io.FileInputStream;
import java.sql.ResultSet;
import java.util.Properties;



/**
 * @Description This class is the base class which controls the test flow 
 * and calls methods as per requirement.
 * @author sandipan.singha
 * @Date 29.06.2016
 */
public class DriverClass {

/**
 * Declaration of Webdriver and Configuration instances.
 */
	public static WebDriver driver;
	public static Properties prop;	
	public static ExtentReports extent;
	public static ExtentTest test;
	
	public static ResultSet rs;
	public static Recordset rcs;
	public static Recordset rcs1;
	public static String scenarioName;
	public static String screenFlag;
	public static boolean zipArchiveFlag;
	public static String browserFlag;

	
/**
 * @Description This is the main method that calls drives the functional flow
 * @param args
 */
	public static void main(String[] args) throws Exception {
		// Instantiating Extent Reports class
		extent = ReportManager.SetReport(extent);
		
		//Instantiating Generic System Log		
		ReportManager.systemLog();;
		ReportManager.logger.info("Starting Log");		
		
		//Below is the object instantiation for configuration properties
		prop=new Properties();			
		
		/**
		 * The below section loads the properties file
		 * named Config.properties present in the project directory 
		 */	
		
		//Test Initialization reporting starts below
		test=extent.startTest("<b>Test Execution Start</b>");
		try
		{					
			prop.load(new FileInputStream("Config.properties"));
			test.log(LogStatus.INFO, "Configuration", " - Configuration Property load successfull");
		}catch(Exception e)
		{						
			test.log(LogStatus.FATAL, Util.textWrap(e.toString(), 
				"redbold")+
				"</br>&nbsp&nbspClass Name: "+Thread.currentThread().getStackTrace()[1].getClassName().toString()+
				"</br>&nbsp&nbspMethod Name: "+Thread.currentThread().getStackTrace()[1].getMethodName().toString());
		}		
		
		//Fetch Test flow details from Test Data sheet below
		rcs1=ConnectionClass.filoDataConnect(prop, "AppData");
		rcs=ConnectionClass.filoDataConnect(prop, "Actions");
		/*//Sets browser type based on browser choice provided by user in configuration properties
		driver=Util.setBrowser(driver, prop);*/
		
		
		
		//<-----------Functional flow starts below---------->
		
		
		try {
			rcs1.next();
			browserFlag=rcs1.getField("browser");
			//Sets browser type based on browser choice provided by user in configuration properties
			driver=Util.setBrowser(driver, prop);
		} catch (FilloException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			while(rcs.next())
			{	
				scenarioName=rcs.getField("ScenarioName");
				screenFlag=rcs.getField("ScreenshotFlag");					
				//Test Initialization reporting ends below
				extent.endTest(test);
				switch(scenarioName)
				{
					case "Navigate":
						// [Scenario1]		
						// Navigate to URL and validate whether page has loaded
						test = extent.startTest(scenarioName);		
						Scenarios.navigate(driver, test, prop, screenFlag);
						extent.endTest(test);
						break;
					case "Search":			
						// [Scenario2]
						// Search and check search results page
						test = extent.startTest(scenarioName);
						Scenarios.search(driver, test, prop, screenFlag);
						extent.endTest(test);
						break;
					case "Play":
						// [Scenario3]
						// Play Youtube video till it ends
						test = extent.startTest(scenarioName);
						Scenarios.play(driver, test, prop, screenFlag);
						extent.endTest(test);
						break;
				}
			}
		} catch (Exception e1) {			
			test.log(LogStatus.ERROR, "Test Execution",
			"Functional flow interrupted due to:"+Util.textWrap(e1.toString(),
			"darkredbold")+
			"</br>&nbsp&nbspClass Name: "+Thread.currentThread().getStackTrace()[1].getClassName().toString()+
			"</br>&nbsp&nbspMethod Name: "+Thread.currentThread().getStackTrace()[1].getMethodName().toString());
			//Calls the kill method to close browser and quit driver.
			Util.killMethod(driver,extent,test);
		}	finally {
			//Calls the kill method to close browser and quit driver.
			Util.killMethod(driver,extent,test);
			//Generate the report and release reporting resources
			ReportManager.reportGen(extent);
			//Copy reports to archive path mentioned in config property "ReportArchive"
			Util.archiveReport(prop);
			//Launch Report in browser
			Util.launchReport();
			//Create Report Zip Archive
			zipArchiveFlag=Util.CreateZip(prop);
			//Send email with zip archive of report
			if(zipArchiveFlag==true&&prop.getProperty("sendMail").equalsIgnoreCase("yes"))
				mailSender.email(prop);
			//Instantiating Generic System Log
			ReportManager.logger.info("Ending Log");
			ReportManager.fh.close();
		}
		//<-----------Functional flow ends here---------->

	}

}
