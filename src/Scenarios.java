import java.sql.ResultSet;
import java.util.Properties;

import org.openqa.selenium.WebDriver;

import com.codoid.products.fillo.Recordset;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

/**
 * @Description This class stores the scenario-wise functions for the required business flow
 * @author sandipan.singha
 * @Date 29.06.2016
 */
public class Scenarios {
/**
 * @Description This method navigates to the desired URL
 * @param driver
 * @param test
 * @param prop
 * @param screenFlag
 */
	public static void navigate(WebDriver driver, ExtentTest test, Properties prop, String screenFlag)
	{
		Recordset rs;
		String appUrl=null;
		//Fetch the Application URL
		rs=ConnectionClass.filoDataConnect(prop,"AppData");
		try {
			while(rs.next())	
				appUrl=rs.getField("AppURL");
		} catch (Exception e1)
		{				
			test.log(LogStatus.ERROR, "Application URL", " - Unable to fetch App URL"+Util.textWrap(e1.toString(), "redbold"));
		}
		//Navigate to the Test URL using selected Browser
		driver.navigate().to(appUrl);
		
		//Validate whether navigation is successfull
		if(Validation.validate(PageObjectClass.btnSignIn(driver)))
		{
			test.log(LogStatus.PASS, "Navigate", "Navigate to URL");
			if(screenFlag.equalsIgnoreCase("pass")||screenFlag.equalsIgnoreCase("all"))
				test.log(LogStatus.INFO, "Home Screen",
				test.addScreenCapture(ReportManager.
					CaptureScreen(driver,
					Thread.currentThread().getStackTrace()[1].getMethodName().toString())));
		}
		else
		{
			test.log(LogStatus.FAIL, "Search", "Search with a String");
			if(screenFlag.equalsIgnoreCase("fail")||screenFlag.equalsIgnoreCase("all"))
				test.log(LogStatus.INFO, "Home Screen",
				test.addScreenCapture(ReportManager.
					CaptureScreen(driver,
					Thread.currentThread().getStackTrace()[1].getMethodName().toString())));
		}
	}	
/**
 * @Description This method Searches with a search string
 * @param driver
 * @param test
 * @param prop
 */
	public static void search(WebDriver driver, ExtentTest test, Properties prop, String screenFlag)
	{
		PageObjectClass.txtSearch(driver).sendKeys("Test Automation");
		PageObjectClass.btnSearch(driver).submit();
		
		if(Validation.validate(PageObjectClass.lblResultStat(driver),"results","contains"))
		{
			test.log(LogStatus.PASS, "Search", "Search with a String");
			if(screenFlag.equalsIgnoreCase("pass")||screenFlag.equalsIgnoreCase("all"))
			test.log(LogStatus.INFO, "Sesarch Results",
			test.addScreenCapture(ReportManager.
				CaptureScreen(driver, 
				Thread.currentThread().getStackTrace()[1].getMethodName().toString())));
		}
		else
		{
			test.log(LogStatus.FAIL, "Search", "Search with a String");
			if(screenFlag.equalsIgnoreCase("fail")||screenFlag.equalsIgnoreCase("all"))
			test.log(LogStatus.INFO, "Sesarch Results",
			test.addScreenCapture(ReportManager.
				CaptureScreen(driver, 
				Thread.currentThread().getStackTrace()[1].getMethodName().toString())));
		}
	}
}
