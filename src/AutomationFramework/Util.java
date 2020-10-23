package AutomationFramework;

import com.google.common.reflect.ClassPath;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * @Description This is a utility class for framework level actions
 * @author sandipan.singha
 */
public class Util {
	//Declaration of WebDriver Type and Path
	public static String driverType;
	public static String driverPath;
	public static String systemBrowserPath;

	public static boolean archiveFlag;
	public static WebDriverWait wait;

	/**
	 * @Description This method sets the WebDriver properties as per target Browser.
	 * The [driverType] and [driverPath] are stored in the config.properties file.
	 * @param prop is the property object
	 * @return returns loaded driver object
	 */
	public static void setBrowser(Properties prop)
	{
		/*
		 * This code sets the selenium webdriver with desired browser
		 * properties.
		 */
		//driverType=prop.getProperty("driverType");
		driverType=DriverClass.browserFlag;
		driverPath=prop.getProperty("driverPath");

		DriverClass.driver=null;
		switch(prop.getProperty("browserType").toLowerCase())
		{
			case "chrome":
				System.setProperty("webdriver."+driverType+".driver",driverPath);
				//setup the chromedriver using WebDriverManager
				WebDriverManager.chromedriver().setup();
				DriverClass.driver = new ChromeDriver();
				DriverClass.driver.manage().deleteAllCookies();
				DriverClass.driver.manage().window().maximize();
				systemBrowserPath=prop.getProperty("browserPath");
				break;
			case "firefox":
				//setup the chromedriver using WebDriverManager
				WebDriverManager.firefoxdriver().setup();
				DriverClass.driver= new FirefoxDriver();
				DriverClass.driver.manage().window().maximize();
				systemBrowserPath=prop.getProperty("browserPath");
				break;
			case "ie":
				//to be loaded if required
				break;
			case "opera":
				//to be loaded if required
				break;
			case "safari":
				//to be loaded if required
				break;
		}
		if(DriverClass.driver!=null) {
			DriverClass.browserWindowClosed=false;
			DriverClass.test.log(LogStatus.INFO, "Browser Launch", " - Browser selection and launch successfull");
		} else
			DriverClass.test.log(LogStatus.ERROR, "Browser Launch", " - Browser launch failed");
		//Initializing wait object with 10 seconds as parameter.
		wait=new WebDriverWait(Objects.requireNonNull(DriverClass.driver), Integer.parseInt(prop.getProperty("customWait")));
	}
	/**
	 * @Description This method closes the WebDriver and releases resources.
	 * @param driver is closed and quit
	 * @param extent flushes the report
	 */
	public static void killMethod(WebDriver driver,ExtentReports extent, ExtentTest test )
	{
		test=extent.startTest("<b>Test Closure</b>");
		try
		{
			if(!DriverClass.browserWindowClosed)
				driver.close();
			driver.quit();
			if(ConnectionClass.con!=null)
				ConnectionClass.con.close();
			test.log(LogStatus.INFO, "Resource Release", " - Resources released successfully");
			extent.endTest(test);
		}catch (Exception e1) {
			System.out.println(e1.getMessage());
			DriverClass.test.log(LogStatus.ERROR, "Test Closure",
					"Unable to release resources due to:"+Util.textWrap(e1.toString(),
							"darkredbold")+
							"</br>&nbsp&nbspClass Name: "+Thread.currentThread().getStackTrace()[1].getClassName()+
							"</br>&nbsp&nbspMethod Name: "+Thread.currentThread().getStackTrace()[1].getMethodName());
			extent.endTest(test);
		}
	}
	/**
	 * @Description This method wraps a string according to necessary style formatting
	 * @param input takes unformatted text
	 * @param style takes style for formatting
	 * @return output (string)
	 */
	public static String textWrap(String input,String style)
	{
		String output="";
		switch(style.toLowerCase())
		{
			case "darkredbold":
				output="</br><b><font color='#bc104a'>"+input+"</font></b>";
			case "redbold":
				output="</br><b><font color='red'>"+input+"</font></b>";
				break;
			default:
				throw new IllegalStateException("Unexpected value: " + style.toLowerCase());
		}
		return output;
	}
	/**
	 * @Description This method launches the Test Execution in Browser
	 */
	public static void launchReport()
	{
		//Launch Report in browser
		try {
			Runtime rTime = Runtime.getRuntime();
			String url = ReportManager.RootPath + ReportManager.Path;
			String browser = Util.systemBrowserPath+" ";
			rTime.exec(browser + url);
			//Process pc = rTime.exec(browser + url);
			//pc.waitFor();

		} catch (Exception e)
		{
			ReportManager.logger.info("\n"+e.toString()+"\n");
		}
	}
	/**
	 * @Description Copy reports to archive path mentioned in config property "ReportArchive"
	 * @param prop property object
	 */
	public static void archiveReport(Properties prop)
	{
		try
		{
			if(!(prop.getProperty("ReportArchive").isEmpty())&&!(prop.getProperty("ReportArchive").equalsIgnoreCase("na")))
			{
				FileUtils.copyDirectory(new File(ReportManager.RootPath+ReportManager.testRunPath),
						new File(prop.getProperty("ReportArchive")+ReportManager.testRunPath));
				archiveFlag=true;
			}
		}catch(Exception e)
		{
			ReportManager.logger.info("\n"+e.toString()+"\n");
			archiveFlag=false;
		}
	}
	/**
	 * @Description This method creates a Winzip archive of the Test Report
	 * @param prop property object
	 */
	public static boolean CreateZip(Properties prop)
	{
		File srcDirectory, zipfile, zipdir;
		if(!(prop.getProperty("ZipArchive").isEmpty())&&!(prop.getProperty("ZipArchive").equalsIgnoreCase("na")))
		{
			try
			{
				//Creates new zip archive directory
				zipdir = new File(prop.getProperty("ZipArchive")+ReportManager.zipName);
				zipdir.mkdirs();
				zipfile = new File(prop.getProperty("ZipArchive")+ReportManager.zipName+".zip");
				srcDirectory = new File(ReportManager.RootPath+ReportManager.testRunPath);

				URI base = srcDirectory.toURI();
				Deque<File> queue = new LinkedList<>();
				queue.push(srcDirectory);
				OutputStream out = new FileOutputStream(zipfile);
				Closeable res = out;
				try {
					ZipOutputStream zout = new ZipOutputStream(out);
					res = zout;
					while (!queue.isEmpty()) {
						srcDirectory = queue.pop();
						for (File child : srcDirectory.listFiles()) {
							String name = base.relativize(child.toURI()).getPath();
							if (child.isDirectory()) {
								queue.push(child);
								name = name.endsWith("/") ? name : name + "/";
								zout.putNextEntry(new ZipEntry(name));
							} else {
								zout.putNextEntry(new ZipEntry(name));
								copy(child, zout);
								zout.closeEntry();
							}
						}
					}
				} finally {
					res.close();
					if(!archiveFlag)
						zipdir.delete();
				}
				return true;
			}
			catch(IOException ioe)
			{
				ReportManager.logger.info("\n"+ioe.toString()+"\n");
			}
		}
		return false;
	}
	/*
	 * @Description This method is a helper to CreateZip method to copy data.
	 * It copies from data from inputstream to outputstream
	 * @param in
	 * @param out
	 */
	private static void copy(File file, OutputStream out)
	{
		InputStream in;
		byte[] buffer = new byte[1024];
		try {
			in = new FileInputStream(file);
			while (true)
			{
				int readCount = in.read(buffer);
				if (readCount < 0) {
					break;
				}
				out.write(buffer, 0, readCount);
			}
			in.close();
		} catch (Exception e) {
			ReportManager.logger.info("\n"+e.toString()+"\n");
		}
	}
	public static ArrayList<String> getClassNames(String packageName)
	{
		ArrayList<String> classNames=new ArrayList();
		final ClassLoader loader = Thread.currentThread()
				.getContextClassLoader();
		try {

			ClassPath classpath = ClassPath.from(loader); // scans the class path used by classloader
			for (ClassPath.ClassInfo classInfo : classpath.getTopLevelClasses(packageName)) {
				if(!classInfo.getSimpleName().endsWith("_")){
					classNames.add(classInfo.getSimpleName());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return classNames;
	}
	public static void jsonFileWriter(JSONObject jsonObject) throws Exception
	{
		FileWriter file = new FileWriter(ReportManager.RootPath
				+ReportManager.testRunPath+"YMOAddressLocation.json");
		file.write(jsonObject.toJSONString());
		file.close();
	}
	public static void scrollToElement(WebElement ele)
	{
		JavascriptExecutor js = (JavascriptExecutor) DriverClass.driver;
		js.executeScript("arguments[0].scrollIntoView();", ele);
	}
}
