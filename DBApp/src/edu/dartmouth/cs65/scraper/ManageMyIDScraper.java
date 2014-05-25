package edu.dartmouth.cs65.scraper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.NameValuePair;
import ch.boye.httpclientandroidlib.ParseException;
import ch.boye.httpclientandroidlib.client.ClientProtocolException;
import ch.boye.httpclientandroidlib.client.CookieStore;
import ch.boye.httpclientandroidlib.client.entity.UrlEncodedFormEntity;
import ch.boye.httpclientandroidlib.client.methods.HttpGet;
import ch.boye.httpclientandroidlib.client.methods.HttpPost;
import ch.boye.httpclientandroidlib.client.params.ClientPNames;
import ch.boye.httpclientandroidlib.client.params.CookiePolicy;
import ch.boye.httpclientandroidlib.client.protocol.ClientContext;
import ch.boye.httpclientandroidlib.impl.client.BasicCookieStore;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;
import ch.boye.httpclientandroidlib.message.BasicNameValuePair;
import ch.boye.httpclientandroidlib.protocol.BasicHttpContext;
import ch.boye.httpclientandroidlib.protocol.HttpContext;
import ch.boye.httpclientandroidlib.util.EntityUtils;

import edu.dartmouth.cs65.Globals;
import edu.dartmouth.cs65.TransactionEntry;

public class ManageMyIDScraper{
	private static final String LOGIN_PAGE = "https://dartmouth.managemyid.com/student/login.php";
	private static final String WELCOME_PAGE = "https://dartmouth.managemyid.com/student/welcome.php";
	private static final String HISTORY_PAGE = "https://dartmouth.managemyid.com/student/svc_history.php";
	private static final String TRANSACTION_PAGE = "https://dartmouth.managemyid.com/student/svc_history_view.php";
	private static final int TRANSACTION_LENGTH = 6;
	private static final String PLAN = "S32";
	
	private String welcomePage;
	private String transactionPage;
	private HttpContext context;
	private HashMap<String, Integer> locationMap;
	
	public ManageMyIDScraper(String username, String password){
		try {
			authenticate(username, password); //login to welcome.php
		} catch (IOException e) {
			System.out.println("Authentication failed due to IOException.");
			e.printStackTrace();
		} 

		//Temporary --  need to ask about Globals
		locationMap = new HashMap<String, Integer>();
		locationMap.put("King Arthur Flour Coffee Bar", 0);
		locationMap.put("Collis Cafe", 1);
		locationMap.put("Novack Cafe", 2);
		locationMap.put("53 Commons", 3);
		locationMap.put("Courtyard Cafe", 4);
	
		
	}
	
	public void authenticate(String username, String password) throws IOException{
		String sesstok;
		boolean success;
		
		//Set up cookie store to save cookies
		DefaultHttpClient httpclient = new DefaultHttpClient();
        CookieStore cookieStore = new BasicCookieStore();
        httpclient.getParams().setParameter(
          ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY); 
        
        //Store Cookiestore in Context
        HttpContext c = new BasicHttpContext();
        c.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        
        //GET request to ManageMyID's login page
        HttpGet getLogin = new HttpGet(LOGIN_PAGE);
        HttpResponse loginResponse = httpclient.execute(getLogin, c);
        
        HttpEntity loginEntity = loginResponse.getEntity();
        
        //Retrieve session token if need be
        sesstok = getSessionToken(EntityUtils.toString(loginEntity));
        EntityUtils.consume(loginEntity);
        
        //POST request to login page using username and password
        HttpPost postLogin = new HttpPost(LOGIN_PAGE);
        
        //Package username and password for POST request
        List <NameValuePair> params = new ArrayList <NameValuePair>();
        
        if (sesstok.length() > 0){ //Add session token to params if it exists
        	params.add(new BasicNameValuePair("__sesstok", sesstok));
        }
        
        params.add(new BasicNameValuePair("user", username));
        params.add(new BasicNameValuePair("pwd", password));
        postLogin.addHeader("Referer", LOGIN_PAGE); //Add "Referer" field to POST header
        
        postLogin.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        HttpResponse loginResponse2 = httpclient.execute(postLogin, c);

        HttpEntity loginEntity2 = loginResponse2.getEntity();
        EntityUtils.consume(loginEntity2);
        
        //GET request to welcome page 
        HttpGet getWelcome = new HttpGet(WELCOME_PAGE);
        HttpResponse welcomeResponse = httpclient.execute(getWelcome, c);
        HttpEntity welcomeEntity = welcomeResponse.getEntity();
        
        //Define class variables before consuming entity
        welcomePage = EntityUtils.toString(welcomeEntity);
        context = c;
        
        EntityUtils.consume(welcomeEntity);
        
        //Check if we're successful
        success = authenticationSuccess(welcomePage); //Check if we managed to log in correctly
        System.out.println("User logged into ManageMyID: " + success);
    }
	
	/*
	 * Checks if authentication worked
	 */
	private boolean authenticationSuccess(String html){
		boolean authenticated = false;
		Document webpage = Jsoup.parse(html);
		Elements links = webpage.select("a[href]");
		
		for (Element link: links){
			if (link.text().equals("CURRENT BALANCE")){
				authenticated = true;
				break;
			}
		}
		
		return authenticated;
	}
	
	/*
	 * Gets the user's total DBA at the beginning of the term
	 */
	public String getTotalDBA(){
		String totalBalance = "";
		Document transaction = Jsoup.parse(transactionPage);

		Element e = transaction.select("td").first();
		totalBalance = e.text();
		
		return totalBalance;
	}
	
	/*
	 * Parse the session token from a ManageMyID webpage if it exists
	 */
	private String getSessionToken(String html){
		String sesstok = "";
		Document loginPage = Jsoup.parse(html);
		Elements scripts = loginPage.select("script");

		for (Element script: scripts){
			String stripped = script.data().replaceAll("\\s+","");
			String[] parsed = stripped.split("=");
			if (parsed[0].equals("var__sesstok")){
				sesstok = parsed[1].substring(1, parsed[1].length() - 2);//strip away apostrophes and semicolon
			}
		}
		
		return sesstok;
	}
	
	/*
	 * Note: This code relies on the welcome.php HTML having the <td>balance_value<td> cell being after the
	 * "Dining DBA" cell in one of the table elements. 
	 */
	public String getDBABalance(){
		Document welcome = Jsoup.parse(welcomePage);
		Elements cells = welcome.select("td");
		String balance = "";

		for (int i = 0; i < cells.size(); i++){
			if (cells.get(i).text().equals("Dining DBA")){
				balance = cells.get(i + 1).text();
				break;
			}
		}
		
		return balance;
	}
	
	/*
	 * Note: This code relies on the fact that the Swipes Remaining cell in welcome.php is identifiable by its
	 * "colspan" attribute. The cell doesn't have an id or name attribute. 
	 */
	public String getSwipeBalance(){
		Document welcome = Jsoup.parse(welcomePage);
		Elements cells = welcome.select("td");
		String balance = "";

		for (int i = 0; i < cells.size(); i++){
			if (cells.get(i).attr("colspan").equals("2")){
				balance = cells.get(i).text();
				
				break;
			}
		}
		
		return balance;
	}
	
	/*
	 * Retrieves the HTML from the transaction history page given a start and end date
	 */
	public void getTransactionHistoryPage(Calendar start, Calendar end) throws ClientProtocolException, IOException{
		List <NameValuePair> params = new ArrayList <NameValuePair>();
		DefaultHttpClient httpclient = new DefaultHttpClient();

		//Set parameters for svc_history_view.php
		params.add(new BasicNameValuePair("FromMonth", convertToString(start.get(Calendar.MONTH))));
		params.add(new BasicNameValuePair("FromDay", convertToString(start.get(Calendar.DAY_OF_MONTH))));
		params.add(new BasicNameValuePair("FromYear", convertToString(start.get(Calendar.YEAR))));
		params.add(new BasicNameValuePair("ToMonth", convertToString(end.get(Calendar.MONTH))));
		params.add(new BasicNameValuePair("ToDay", convertToString(end.get(Calendar.DAY_OF_MONTH))));
		params.add(new BasicNameValuePair("ToYear", convertToString(end.get(Calendar.YEAR))));
		params.add(new BasicNameValuePair("plan", PLAN));
		
		String sesstok = getSessionToken(welcomePage);
		if (sesstok.length() > 0){
			params.add(new BasicNameValuePair("__sesstok", sesstok));
		}
		
		//POST to svc_history_view.php
		HttpPost postTransaction = new HttpPost(TRANSACTION_PAGE);
		postTransaction.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		postTransaction.addHeader("Referer", HISTORY_PAGE);
				
		HttpResponse transactionResponse = httpclient.execute(postTransaction, context);
		HttpEntity transactionEntity = transactionResponse.getEntity();
		
		transactionPage = EntityUtils.toString(transactionEntity);
		EntityUtils.consume(transactionEntity);
	}
	
	/*
	 * Creates an ArrayList of TransactionEntry objects using the data from
	 * svc_history_view.php
	 */
	private ArrayList<TransactionEntry> getTransactionHistory(){
		int TRANSACTION_START, currCell, location;
		double spent;
		Calendar dateTime;
		ArrayList<TransactionEntry> entries = new ArrayList<TransactionEntry>();
		
		//Get all cells from transaction history page
		Document transaction = Jsoup.parse(transactionPage);
		Elements cells = transaction.select("td");
		
		TRANSACTION_START = 2; //where cells start showing transaction data
		currCell = TRANSACTION_START;
		
		//Iterate through all cells on svc_history_view, retrieve data, and save as
		//TransactionEntry objects
		while (currCell < cells.size()){
			TransactionEntry newEntry = new TransactionEntry();
			
			//Retrieve values for TransactionEntry object
			dateTime = setTransactionCalendar(cells.get(currCell).text());
			location = locationMap.get(cells.get(currCell + 1).text());
			spent = convertValueStringToDouble(cells.get(currCell + 4).text());
			
			//Set new values for newEntry
			newEntry.initializeDateTime(dateTime);
			newEntry.setLocation(location);
			newEntry.setAmount(spent);
			
			System.out.println("date Time :" + dateTime.get(Calendar.MONTH) + "/" + dateTime.get(Calendar.DAY_OF_MONTH) + 
					"/" + dateTime.get(Calendar.YEAR));
			System.out.println("location: " + location);
			System.out.println("money spent: " + spent);
			
			//Add new TransactionEntry object to ArrayList
			entries.add(newEntry);
			
			currCell += TRANSACTION_LENGTH; //Go to next transaction
		}
		
		return entries;
	}
	
	/*
	 * Takes $xx.xx String and converts to a double
	 */
	private double convertValueStringToDouble(String dbaSpent){
		String stripped = dbaSpent.substring(1);
		double value = Double.parseDouble(stripped);
		
		return value;
	}
	
	/*
	 * Takes date and time string (xx/xx/xxxx yy:yy:yy) from transaction history
	 *  and converts it into a Calendar object 
	 */
	private Calendar setTransactionCalendar(String dateTime){
		String date, time;
		String[] splitDateTime;
		int month, day, year, hour, minute, second;
		Calendar c;
		
		splitDateTime = dateTime.split(" ");
		date = splitDateTime[0];
		time = splitDateTime[1];
		c = Calendar.getInstance();
		
		month = Integer.parseInt(date.substring(0, 2)) - 1;
		day = Integer.parseInt(date.substring(3, 5));
		year = Integer.parseInt(date.substring(6, 10));
		
		hour = Integer.parseInt(time.substring(0, 2));
		minute = Integer.parseInt(time.substring(3, 5));
		second = Integer.parseInt(time.substring(6, 8));
		
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.YEAR, year);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, second);
		
		return c;
	}
	
	/*
	 * Converts Calendar values to strings, making adjustments for ManageMyId
	 */
	private static String convertToString(int value){
		String param = Integer.toString(value);
		String zero = "0";
		
		if (param.length() < 2){ //so May or "5" becomes "05"
			param = zero + param;
		}
		
		return param;
	}
	
	public static void main(String[] args) throws ParseException, IOException{
		ManageMyIDScraper test = new ManageMyIDScraper("eva.w.xiao@dartmouth.edu", "testpassword");
		test.getDBABalance();
		test.getSwipeBalance();

		test.getTransactionHistoryPage(Globals.FOURTEEN_SPRING_START, Globals.FOURTEEN_SPRING_END);
		test.getTransactionHistory();
		
	}
	
}