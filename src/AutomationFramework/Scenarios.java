package AutomationFramework;

import com.codoid.products.fillo.Recordset;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import java.util.Properties;

/**
 * @Description This class stores the scenario-wise functions for the required business flow
 * @author sandipan.singha
 * @Date 29.06.2016
 */
public class Scenarios
{
	/**
	 * @Description This method navigates to the desired URL
	 * @param driver
	 * @param test
	 * @param prop
	 * @param screenFlag
	 */
	public void navigate(WebDriver driver, ExtentTest test, Properties prop, String screenFlag) {
		Recordset rs;
		String appUrl = null;
		String appName = null;
		String pageValidationString=null;
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
		try{
			driver.get(appUrl);
		}catch (Exception e){
			driver.navigate().to(appUrl);
		}
		//Validate whether navigation is successfull
		switch (appName) {
			case "YouTube":
				if (driver.getTitle().contains(pageValidationString)) {
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
				break;
			case "YMO":
				if (driver.getTitle().equalsIgnoreCase("Your Meals Online")) {
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
				break;
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
	public static void play(WebDriver driver, ExtentTest test, Properties prop, String screenFlag) throws Exception
	{
		YouTubePageObjects ytp=new YouTubePageObjects(driver);
		String currentSeekTime = null;
		String totalDurationTime = null;
		//Instantiate Action Class
		Actions actions = new Actions(driver);
		try{
			if(ytp.videoArea.isDisplayed()) {
				if (!Validation.isClickable(ytp.btnPause, driver))
					if (Validation.isClickable(ytp.btnPlay, driver))
						ytp.btnPlay.click();
					else {
						//Mouse hover menuOption 'Music'
						actions.moveToElement(ytp.videoArea).perform();
						if (!Validation.isClickable(ytp.btnPause, driver))
							if (Validation.isClickable(ytp.btnPlay, driver))
								ytp.btnPlay.click();
					}
				if (Validation.isClickable(ytp.btnMute, driver))
					ytp.btnMute.click();
				else {
					Thread.sleep(5000);
					//Mouse hover menuOption 'Music'
					actions.moveToElement(ytp.videoArea).perform();
					ytp.btnMute.click();
				}
				do {
					if (ytp.txtCurrentSeekTime.isEnabled()) {
						currentSeekTime = ytp.txtCurrentSeekTime.getText().trim();
						totalDurationTime = ytp.txtTotalDurationTime.getText();
						Thread.sleep(3000);
						if (Integer.parseInt(currentSeekTime.split(":")[0]) >= 1) {
							test.log(LogStatus.PASS, "Play", "Play video");
							if (screenFlag.equalsIgnoreCase("pass") || screenFlag.equalsIgnoreCase("all"))
								test.log(LogStatus.INFO, "Home Screen",
										test.addScreenCapture(ReportManager.
												CaptureScreen(driver,
														Thread.currentThread().getStackTrace()[1].getMethodName().toString())));
							break;
						}
					}else {
						//Mouse hover menuOption 'Music'
						actions.moveToElement(ytp.videoArea).perform();
						Thread.sleep(3000);
						if (Integer.parseInt(currentSeekTime.split(":")[0]) >= 1) {
							test.log(LogStatus.PASS, "Play", "Play video");
							if (screenFlag.equalsIgnoreCase("pass") || screenFlag.equalsIgnoreCase("all"))
								test.log(LogStatus.INFO, "Home Screen",
										test.addScreenCapture(ReportManager.
												CaptureScreen(driver,
														Thread.currentThread().getStackTrace()[1].getMethodName().toString())));
							break;
						}
					}
			}
			while (!currentSeekTime.equals(totalDurationTime));
		}
		} catch (Exception e)
		{
			test.log(LogStatus.FAIL, "Play", "Play video");
			if (screenFlag.equalsIgnoreCase("fail") || screenFlag.equalsIgnoreCase("all"))
				test.log(LogStatus.INFO, "Home Screen",
						test.addScreenCapture(ReportManager.
								CaptureScreen(driver,
										Thread.currentThread().getStackTrace()[1].getMethodName().toString())));
			System.out.println(e.getMessage());
			DriverClass.test.log(LogStatus.ERROR, "Scenario Execution",
					"Play function failed due to:" + Util.textWrap(e.toString(),
							"darkredbold") +
							"</br>&nbsp&nbspClass Name: " + Thread.currentThread().getStackTrace()[1].getClassName() +
							"</br>&nbsp&nbspMethod Name: " + Thread.currentThread().getStackTrace()[1].getMethodName());
		}
		driver.close();
		DriverClass.browserWindowClosed=true;
	}
	public  void navigateToYMORegistrationPage(WebDriver driver, ExtentTest test, Properties prop, String screenFlag)
	{
		YMOHomePage ymoHP=new YMOHomePage(driver);
		YMORegistrationPage ymoRP=new YMORegistrationPage(driver);
		try {
			ymoHP.dismissModalAlert();
			ymoHP.navigateToRegistrationPage();
			if (screenFlag.equalsIgnoreCase("pass") || screenFlag.equalsIgnoreCase("all"))
				test.log(LogStatus.INFO, "Home Screen",
						test.addScreenCapture(ReportManager.
								CaptureScreen(driver,
										Thread.currentThread().getStackTrace()[1].getMethodName().toString())));
		} catch (Exception e) {
			test.log(LogStatus.ERROR, "YMO Home Page navigation",
					" Exception on home page" + Util.textWrap(e.toString(),
							"redbold"));
		}

		try {
			ymoRP.navigateToRegistrationPage();
			if (ymoRP.xpathLblRegistrationPageHeading.isDisplayed()) {
				test.log(LogStatus.PASS, "Navigate", "Navigate to Customer Registration Page Step 1");
				if (screenFlag.equalsIgnoreCase("pass") || screenFlag.equalsIgnoreCase("all"))
					test.log(LogStatus.INFO, "Customer Registration Page",
							test.addScreenCapture(ReportManager.
									CaptureScreen(driver,
											Thread.currentThread().getStackTrace()[1].getMethodName().toString())));
			} else {
				test.log(LogStatus.FAIL, "Navigate", "Navigate to Customer Registration Page Step 1");
				if (screenFlag.equalsIgnoreCase("fail") || screenFlag.equalsIgnoreCase("all"))
					test.log(LogStatus.INFO, "Customer Registration Page",
							test.addScreenCapture(ReportManager.
									CaptureScreen(driver,
											Thread.currentThread().getStackTrace()[1].getMethodName().toString())));
			}
		} catch (Exception e) {
			test.log(LogStatus.ERROR, "YMO Registration Page navigation",
					" Exception on Registration Page" + Util.textWrap(e.toString(),
							"redbold"));
		}
	}
}
