import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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

public class ManageMyIDScraper{
	private static final String LOGIN_PAGE = "https://dartmouth.managemyid.com/student/login.php";
	private static final String WELCOME_PAGE = "https://dartmouth.managemyid.com/student/welcome.php";
	private static final String HISTORY_PAGE = "https://dartmouth.managemyid.com/student/svc_history.php";
	private static final String TRANSACTION_PAGE = "https://dartmouth.managemyid.com/student/svc_history_view.php";
	private String welcomePage;
	private HttpContext context;
	
	public ManageMyIDScraper(String username, String password){
		try {
			authenticate(username, password); //login to welcome.php
		} catch (IOException e) {
			System.out.println("Authentication failed due to IOException.");
			e.printStackTrace();
		} 
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
	
	public void getHistoryByDates(Calendar start, Calendar end) throws ClientProtocolException, IOException{
		List <NameValuePair> params = new ArrayList <NameValuePair>();
		
		//GET svc_history.php
		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpGet getHistory = new HttpGet(HISTORY_PAGE);
		HttpResponse historyResponse= httpclient.execute(getHistory, context);
		HttpEntity historyEntity = historyResponse.getEntity();
		EntityUtils.consume(historyEntity);

		//Set parameters for svc_history_view.php
		params.add(new BasicNameValuePair("FromMonth", convertToString(start.get(Calendar.MONTH))));
		params.add(new BasicNameValuePair("FromDay", convertToString(start.get(Calendar.DAY_OF_MONTH))));
		params.add(new BasicNameValuePair("FromYear", convertToString(start.get(Calendar.YEAR))));
		params.add(new BasicNameValuePair("ToMonth", convertToString(start.get(Calendar.MONTH))));
		params.add(new BasicNameValuePair("ToDay", convertToString(start.get(Calendar.DAY_OF_MONTH))));
		params.add(new BasicNameValuePair("ToYear", convertToString(start.get(Calendar.YEAR))));
		params.add(new BasicNameValuePair("plan", "S32"));
		params.add(new BasicNameValuePair("", ""));
		
		String sesstok = getSessionToken(HISTORY_PAGE);
		if (sesstok.length() > 0){
			params.add(new BasicNameValuePair("__sesstok", sesstok));
		}
		
		//POST to svc_history_view.php
		HttpPost postTransaction = new HttpPost(TRANSACTION_PAGE);
		postTransaction.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		postTransaction.addHeader("Referer", HISTORY_PAGE);
		postTransaction.addHeader("Connection", "close");
		
		
		
		HttpResponse transactionResponse = httpclient.execute(postTransaction, context);
		HttpEntity transactionEntity = transactionResponse.getEntity();
		
		System.out.println(EntityUtils.toString(transactionEntity));
	}
	
	
	/*
	 * Converts Calendar values to strings, making adjustments for ManageMyId
	 */
	public static String convertToString(int value){
		String param = Integer.toString(value);
		String zero = "0";
		
		if (param.length() < 2){ //so May or "5" becomes "05"
			param = zero + param;
		}
		
		return param;
	}
	public static void main(String[] args) throws ParseException, IOException{
		ManageMyIDScraper test = new ManageMyIDScraper("eva.w.xiao@dartmouth.edu", "testpassword");
		//test.getDBABalance();
		//test.getSwipeBalance();
		Calendar FOURTEEN_SPRING_START = getCalendarForDate(2,24,2014);
		Calendar FOURTEEN_SPRING_END = getCalendarForDate(5,3,2014);
		test.getHistoryByDates(FOURTEEN_SPRING_START, FOURTEEN_SPRING_END);

	}
	
	public static Calendar getCalendarForDate(int month,int day,int year){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH,month);
		c.set(Calendar.DAY_OF_MONTH,day);
		c.set(Calendar.YEAR,year);
		return c;
	}
}