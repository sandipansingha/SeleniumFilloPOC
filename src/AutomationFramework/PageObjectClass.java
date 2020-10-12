package AutomationFramework; /**
 * @Description This class contains the identification properties of all the page elements
 * @author sandipan.singha 
 * of the web application under test
 * @Date 29.06.2016
 */
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PageObjectClass {
	//Declaration of WebElement object to be used for element identification
	public static WebElement element=null;
public static WebElement txtSearch(WebDriver driver)
{
	element=driver.findElement(By.name("q"));
	return element;
}
public static WebElement btnSearch(WebDriver driver)
{
	element=null;
	element=driver.findElement(By.xpath("//*[@value='Google Search']"));
	return element;
}
public static WebElement btnSignIn(WebDriver driver)
{
	element=null;
	element=driver.findElement(By.xpath("//*[@id='gb_70']"));
	return element;
}
public static WebElement lblResultStat(WebDriver driver)
{
	element=null;
	element=driver.findElement(By.xpath("//*[@id='resultStats']"));
	return element;
}
}

