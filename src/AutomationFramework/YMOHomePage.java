package AutomationFramework;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class YMOHomePage
{
    WebDriver driver;
    public YMOHomePage(WebDriver driver)
    {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
    @FindBy(xpath="//h1[@class='modal-title']")
    WebElement xpathLblModalTitle;

    @FindBy(xpath="//a[contains(text(),'Registration')]")
    WebElement xpathLnkRegistrationPage;

    @FindBy(xpath = "//*[@id='popModal']//button[@class='close']")
    WebElement xpathBtnPopupClose;

    public void dismissModalAlert() throws Exception
    {
        Alert alert;
        Util.wait.until(ExpectedConditions.visibilityOf(xpathLblModalTitle));
        if(Validation.validate(xpathLblModalTitle,"Your Moment"))
        {
            try {
                alert=driver.switchTo().alert();
                alert.dismiss();
            } catch (Exception e) {
                xpathBtnPopupClose.click();
            }
        }
    }

    public void navigateToRegistrationPage() throws Exception
    {
        xpathLnkRegistrationPage.click();
    }
}
