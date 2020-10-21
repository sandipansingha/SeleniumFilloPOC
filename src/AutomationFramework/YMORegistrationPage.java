package AutomationFramework;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class YMORegistrationPage {
    WebDriver driver;
    public YMORegistrationPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//a[@class='link-btn' and contains(text(),'join now')]")
    WebElement xpathBtnJoinNow;

    @FindBy(xpath = "//h1[contains(text(),'CUSTOMERS') and contains(text(),'ON-LINE REGISTRATION')]")
    WebElement xpathLblRegistrationPageHeading;

    public void navigateToRegistrationPage() throws Exception
    {
        xpathBtnJoinNow.click();
        if(!xpathLblRegistrationPageHeading.isDisplayed())
        {
            Thread.sleep(3000);
        }

    }
}
