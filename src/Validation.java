import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @Description This class contains all the test case specific methods
 * for navigation and validation
 * @author sandipan.singha
 *
 */
public class Validation {
	
//----------------Series of overload methods that only validate condition--------------------
/*
 * @Description This method is the base method for general validation.
 * This method compares the text associated to an element with the passed string
 * @param driver
 * @param element
 * @param strToCheck
 * @return True or False
 */
	public static Boolean validate(WebElement element,String strToCheck)
	{
		if(element.getText().compareToIgnoreCase(strToCheck)==0)
			return true;
		return false;
	}
/**
 * @Description This method is the overloaded method for general validation.
 * It checks whether the specific element is displayed.
 * @param element
 * @return True or False
 */
	public static Boolean validate(WebElement element)
	{
		if(element.isDisplayed())
			return true;
		return false;
	}
/**
 * @Description This method is the overloaded method for general validation.
 * It checks whether a specific string exists in the text associated to an element.
 * @param element
 * @param strToCheck
 * @param flag
 * @return True or False
 */
	public static Boolean validate(WebElement element, String strToCheck,String flag)
	{
		if(flag.equalsIgnoreCase("contains"))
			if(element.getText().toLowerCase().contains(strToCheck))
				return true;
		return false;
	}
	public static boolean isClickable(WebElement el, WebDriver driver)
	{
		try{
			WebDriverWait wait = new WebDriverWait(driver, 6);
			wait.until(ExpectedConditions.elementToBeClickable(el));
			return true;
		}
		catch (Exception e){
			return false;
		}
	}
}
