package AutomationFramework;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

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

    @FindBy(xpath = "//select[contains(@name,'county') and contains(@id,'cntry')]")
    WebElement xpathDdnPrimaryDropDown;

    @FindBy(xpath = "//select[contains(@name,'boroughs') and contains(@id,'lb')]")
    WebElement xpathDdnBoroughs;

    @FindBy(xpath = "//select[contains(@name,'uk_city') and contains(@id,'tcuk')]")
    WebElement xpathDdnTopCities;

    @FindBy(xpath = "//select[contains(@name,'catchments') and contains(@id,'catchments')]")
    WebElement xpathDdnCatchments;

    public void navigateToRegistrationPageStep1() throws Exception
    {
        xpathBtnJoinNow.click();
        if(!xpathLblRegistrationPageHeading.isDisplayed())
        {
            Thread.sleep(3000);
        }

    }

    public void createLocationJSON() throws Exception
    {
        JSONObject jsonParentObject = new JSONObject();
        JSONObject jsonChildObject=null;
        JSONArray jsonArray=null;
        JSONArray parentJsonArray=null;
        ArrayList<String> catchmentArray;
        List<WebElement> primaryDdnOptions;
        List<WebElement> secondaryOptions;
        List<WebElement> tertiaryDdnOptions;
        Select primaryDdn;
        Select secondaryDdn=null;
        Select catchmentDdn;

        Util.scrollToElement(xpathDdnPrimaryDropDown);
        primaryDdn = new Select(xpathDdnPrimaryDropDown);
        //for(int i=0;i<primaryDdn.getOptions().size();i++) {
        for(int i=0;i<3;i++) {
            primaryDdn.selectByIndex(i);
            if (i < 3)
            {

                if (!primaryDdn.getFirstSelectedOption().getText().contains("Please Select One"))
                {
                    Thread.sleep(1000);
                    if (primaryDdn.getFirstSelectedOption().getText().contains("London"))
                        secondaryDdn = new Select(xpathDdnBoroughs);
                    else if (primaryDdn.getFirstSelectedOption().getText().contains("Top Cities"))
                        secondaryDdn = new Select(xpathDdnTopCities);
                    jsonChildObject = new JSONObject();
                    //for (int j =0 ; j < secondaryDdn.getOptions().size(); j++)
                    for (int j =0 ; j < 3; j++)
                    {
                        secondaryDdn.selectByIndex(j);
                        if (!secondaryDdn.getFirstSelectedOption().getText().contains("Please Select One")
                                && !secondaryDdn.getFirstSelectedOption().getText().contains("Select")) {
                            Thread.sleep(1000);
                            catchmentDdn = new Select(xpathDdnCatchments);
                            jsonArray = new JSONArray();
                            for (int k = 0; k < catchmentDdn.getOptions().size(); k++) {
                                catchmentDdn.selectByIndex(k);
                                jsonArray.add(catchmentDdn.getFirstSelectedOption().getText().trim());
                            }
                        }
                        jsonChildObject.put(secondaryDdn.getFirstSelectedOption().getText().trim(), jsonArray);
                    }
                }
            }
            else if(i==3)
            {
                jsonChildObject=new JSONObject();
                jsonParentObject.put("County Names are below:", jsonChildObject);
            }
            if(i!=3)
                jsonParentObject.put(primaryDdn.getFirstSelectedOption().getText().trim(), jsonChildObject);

        }

        parentJsonArray.add(jsonParentObject.toJSONString());
        Util.jsonFileWriter(jsonParentObject);

    }
}
