import com.codoid.products.fillo.Recordset;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Properties;

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
	public static void navigate(WebDriver driver, ExtentTest test, Properties prop, String screenFlag) {
		Recordset rs;
		String appUrl = null;
		String appName = null;
		String pageValidationString;
		//Fetch the Application URL
		try {
			rs = ConnectionClass.filoDataConnect(prop, "AppData");
			while (rs.next()) {
				appUrl = rs.getField("AppURL");
				appName = rs.getField("AppName");
			}
			pageValidationString = ConnectionClass.getScenarioData(prop, "PageValidationString", DriverClass.scenarioName);
		} catch (Exception e1) {
			test.log(LogStatus.ERROR, "Application URL", " - Unable to fetch App URL" + Util.textWrap(e1.toString(), "redbold"));
		}
		//Navigate to the Test URL using selected Browser
		driver.navigate().to(appUrl);

		//Validate whether navigation is successfull
		switch (appName) {
			case "YouTube":
				if (driver.getTitle().contains("YouTube")) {
					test.log(LogStatus.PASS, "Navigate", "Navigate to URL");
					if (screenFlag.equalsIgnoreCase("pass") || screenFlag.equalsIgnoreCase("all"))
						test.log(LogStatus.INFO, "Home Screen",
								test.addScreenCapture(ReportManager.
										CaptureScreen(driver,
												Thread.currentThread().getStackTrace()[1].getMethodName().toString())));
				} else {
					test.log(LogStatus.FAIL, "Search", "Search with a String");
					if (screenFlag.equalsIgnoreCase("fail") || screenFlag.equalsIgnoreCase("all"))
						test.log(LogStatus.INFO, "Home Screen",
								test.addScreenCapture(ReportManager.
										CaptureScreen(driver,
												Thread.currentThread().getStackTrace()[1].getMethodName().toString())));
				}
			case "ToBeAdded":
				if (Validation.validate(PageObjectClass.btnSignIn(driver))) {
					test.log(LogStatus.PASS, "Navigate", "Navigate to URL");
					if (screenFlag.equalsIgnoreCase("pass") || screenFlag.equalsIgnoreCase("all"))
						test.log(LogStatus.INFO, "Home Screen",
								test.addScreenCapture(ReportManager.
										CaptureScreen(driver,
												Thread.currentThread().getStackTrace()[1].getMethodName().toString())));
				} else {
					test.log(LogStatus.FAIL, "Navigate", "Navigate to URL");
					if (screenFlag.equalsIgnoreCase("fail") || screenFlag.equalsIgnoreCase("all"))
						test.log(LogStatus.INFO, "Home Screen",
								test.addScreenCapture(ReportManager.
										CaptureScreen(driver,
												Thread.currentThread().getStackTrace()[1].getMethodName().toString())));
				}
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
	public static void play(WebDriver driver, ExtentTest test, Properties prop, String screenFlag)
	{
		double currentSeekTime=0.0;
		double totalDurationTime=0.0;
		new WebDriverWait(driver,5)
				.until(ExpectedConditions
						.elementToBeClickable(YouTubePageObjects.btnPlay));
		YouTubePageObjects.btnPlay.click();
		do{
			try {
				currentSeekTime=Double.parseDouble(YouTubePageObjects.txtCurrentSeekTime.getText());
				totalDurationTime=Double.parseDouble(YouTubePageObjects.txtTotalDurationTime.getText());
				driver.manage().timeouts().wait(5000);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				DriverClass.test.log(LogStatus.ERROR, "Test Closure",
						"Unable to release resources due to:"+Util.textWrap(e.toString(),
								"darkredbold")+
								"</br>&nbsp&nbspClass Name: "+Thread.currentThread().getStackTrace()[1].getClassName()+
								"</br>&nbsp&nbspMethod Name: "+Thread.currentThread().getStackTrace()[1].getMethodName());
			}
		}
		while(currentSeekTime!=totalDurationTime);
	}
}
