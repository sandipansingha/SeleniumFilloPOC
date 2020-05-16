//import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.codoid.products.fillo.Connection;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;



/**
* @Description This class establishes connection between framework and external data source
* @author sandipan.singha	
* @Date 29.06.2016
* @param input parameter = property object
* @param output parameter = string
*/
public class ConnectionClass {

	//Declaration of necessary objects for connection 	
	public static String url;
	public static Connection con;
	public static Statement st;
	public static ResultSet rs;
	public static Recordset rcs;
	public static String query;
	public static ExtentReports extent;
	public static ExtentTest test;
	public static Fillo fillo;
	public static Connection filloCon;

/**
 * @Description This is a method to fetch Data and return ResulSet.
 * @param Properties object prop
 * @param Boolean object flag
 * @return ResultSet object rs
 */
	/*public static ResultSet dataConnect(Properties prop,String Sheetname)
	  {
		
	    try
	    {	//Declaration of connection bridge
	    	Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
	    	
	    	//Connection String
		    String myDB = "jdbc:odbc:Driver={Microsoft Excel Driver (*.xls, *.xlsx, *.xlsm, *.xlsb)};DBQ="+ "./"+prop.getProperty("dataSource")+";"
		        + "READONLY=false";
		    
		    *//**
		     * Opening Data connection using connection string and 
		     * linking the connection to connection object con.
		     *//*
		    con = DriverManager.getConnection(myDB);
		    
		    //Statement object to send sql statement to datasource
		    st = con.createStatement();
		    
		    *//**
		     * ResultSet object rs storing the result of the executed
		     * query in the DataSource.
		     *//*
		    if(Sheetname.equalsIgnoreCase("Actions"))
		    	rs = st.executeQuery("Select * from ["+Sheetname+"$] where ExecuteFlag='yes'");
		    else if(Sheetname.equalsIgnoreCase("AppData"))
		    	rs = st.executeQuery("Select * from ["+Sheetname+"$] where TestName='"+prop.getProperty("releaseName")+"'");
	        	              
	    }catch(Exception e)
	    {
	    	System.out.println(e);
	    	test.log(LogStatus.FATAL, Util.textWrap(e.toString(),
    			"redbold")+
    			"</br>&nbsp&nbspClass Name: "+Thread.currentThread().getStackTrace()[1].getClassName().toString()+
    			"</br>&nbsp&nbspMethod Name: "+Thread.currentThread().getStackTrace()[1].getMethodName().toString());
	    }
	    return rs;
	  }	*/
	
	public static Recordset filoDataConnect(Properties prop, String Sheetname)
	{
		fillo=new Fillo();
		
		try
		{
			//Declaration of connection bridge
			
			filloCon=fillo.getConnection("./"+prop.getProperty("dataSource"));
			/**
		     * ResultSet object rs storing the result of the executed
		     * query in the DataSource.
		     */
			if(Sheetname.equalsIgnoreCase("Actions"))
		    	rcs = filloCon.executeQuery("Select * from "+Sheetname+" where ExecuteFlag='yes'");
		    else if(Sheetname.equalsIgnoreCase("AppData"))
		    	rcs = filloCon.executeQuery("Select * from "+Sheetname+" where TestName='"+prop.getProperty("releaseName")+"'");
		}
		catch(Exception e)
		{}
		return rcs;
	}
}

	
