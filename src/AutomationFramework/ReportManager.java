package AutomationFramework;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

/**
 * @Description This class manages methods to drive the Framework Reporting structure 
 * @author sandipan.singha
 *
 */
public class ReportManager 
{
	static Date date = new Date() ;
	static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss") ;
	static String startTimestamp=dateFormat.format(date);
	
	//Declaration of resource locations
	public static String Path = "/Reporting/"+startTimestamp+"_Execution/AutomationTestReport.html";
	public static String RootPath = System.getProperty("user.dir");
	public static String screenShotFolder = "/Reporting/"+startTimestamp+"_Execution/TestScreenShots/";
	public static String testRunPath="/Reporting/"+startTimestamp+"_Execution/";
	public static String zipName="/Reporting/"+startTimestamp+"_Execution";
	
	//Declaration of Report instances
	public static ExtentReports extent;
	public static ExtentTest test;
	public static Logger logger;
	public static FileHandler fh;
	
/**
 * @Description This method loads the Extent Reports configuration xml file
 * which contains the report name, report settings, etc.
 * @param extent
 * @return
 */
	public static ExtentReports SetReport(ExtentReports extent)
	{
		//Instantiating ExtentReports object and setting path to Report archive
		extent = new ExtentReports("."+Path, true);
		
		//Loading the ExtentReports configuration xml
		extent.loadConfig(new File("ExtentReportConfig.xml"));
		
		return extent;
	}
/**
 * @Description This method captures screenshots
 * and returns back the path to the saved screenshots folder.
 * @param driver
 * @param imagename
 * @return
 */
	public static String CaptureScreen(WebDriver driver, String imagename)
	{
		//Selenium method to capture screenshot
		TakesScreenshot oScn = (TakesScreenshot) driver;
		File oScnShot = oScn.getScreenshotAs(OutputType.FILE);
		
		//Saving the screenshot file to mentioned path
		String imgFullPath = RootPath+"/"+screenShotFolder+imagename+ ".jpg";
		File oDest = new File(imgFullPath);
		try {
			FileUtils.copyFile(oScnShot, oDest);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			test.log(LogStatus.ERROR, "Screen Capture", 
					" - Screen Capture failed due to:"+Util.textWrap(e.toString(), 
					"darkredbold")+
					"</br>&nbsp&nbspClass Name: "+Thread.currentThread().getStackTrace()[1].getClassName().toString()+
					"</br>&nbsp&nbspMethod Name: "+Thread.currentThread().getStackTrace()[1].getMethodName().toString());
		}
		return imgFullPath;
		
	}
/**
 * @Description This method dumps the final report into the earlier mentioned filepath.
 * @param extent
 */
	public static void reportGen(ExtentReports extent)
	{
		//CLosing the method
		extent.flush();		
		extent.close();
	}
/**
 * @Description Returns the Test Report filepath
 * @return
 */
	public static  String getPath()
	{
		return Path;
	}
/**
 * @Description Returns the Root Path for the project.
 * @return
 */
	public static String getRootPath()
	{
		return RootPath;
	}
/**
 * @Description This method generates and maintains a generic system log of the framework.
 * @return logger instance
 */
	public static Logger systemLog()
	{
		logger = Logger.getLogger("TestRunLog");	      
	    try 
	    {  
	    	//Creates new report directory
	    	new File(RootPath+testRunPath).mkdirs();
	        // This block configure the logger with handler and formatter  
	        fh = new FileHandler(RootPath+testRunPath+startTimestamp+"_SysLogFile.log");  
	        logger.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);	        
	    } catch (SecurityException e) {  
	        logger.info("\n"+e.toString()+"\n");  
	    } catch (IOException e) {  
	    	logger.info("\n"+e.toString()+"\n"); 
	    }
	    return logger;
	}
}