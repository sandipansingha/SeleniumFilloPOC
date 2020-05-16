import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class YouTubePageObjects {
    //Declaration of WebElement object to be used for element identification
    public WebDriver driver=null;
    public YouTubePageObjects(WebDriver driver)
    {
        this.driver = driver;

        //This initElements method will create all WebElements
        PageFactory.initElements(driver, this);
    }
    @FindBy(xpath = "//button[contains(@title,'Play')]")
    public static WebElement btnPlay;

    @FindBy(xpath = "//span[contains(@class,'ytp-time-current')]")
    public static WebElement txtCurrentSeekTime;

    @FindBy(xpath = "//span[contains(@class,'ytp-time-duration')]")
    public static WebElement txtTotalDurationTime;
}
