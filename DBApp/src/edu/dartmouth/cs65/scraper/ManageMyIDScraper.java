package edu.dartmouth.cs65.scraper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.NameValuePair;
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
	private HttpEntity welcomeEntity;
	
	public ManageMyIDScraper(String username, String password){
		try {
			welcomeEntity = authenticate(username, password); //log in to welcome.php
		} catch (IOException e) {
			System.out.println("Authentication failed.");
			e.printStackTrace();
		} 
	}
	
	public HttpEntity authenticate(String username, String password) throws IOException{
		HttpEntity finalEntity;
		String sesstok;
		
		//Set up cookie store to save cookies
		DefaultHttpClient httpclient = new DefaultHttpClient();
        CookieStore cookieStore = new BasicCookieStore();
        httpclient.getParams().setParameter(
          ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY); 
        
        //Store Cookiestore in Context
        HttpContext context = new BasicHttpContext();
        context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        
        //GET request to ManageMyID's login page
        HttpGet getLogin = new HttpGet(LOGIN_PAGE);
        HttpResponse loginResponse = httpclient.execute(getLogin, context);
        
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

        HttpResponse loginResponse2 = httpclient.execute(postLogin, context);

        HttpEntity loginEntity2 = loginResponse2.getEntity();
        EntityUtils.consume(loginEntity2);
        
        //GET request to welcome page 
        HttpGet getWelcome = new HttpGet(WELCOME_PAGE);
        HttpResponse welcomeResponse = httpclient.execute(getWelcome, context);
        HttpEntity welcomeEntity = welcomeResponse.getEntity();
        finalEntity = welcomeEntity;
        
        boolean success = authenticationSuccess(EntityUtils.toString(welcomeEntity)); //Check if we managed to log in correctly
        System.out.println("User logged into ManageMyID: " + success);
        EntityUtils.consume(welcomeEntity);
        
        return finalEntity;
    }
	
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
	
	//ManageMyID sometimes has a session token that you need to include when authenticating
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
	
	public static void main(String[] args){
		ManageMyIDScraper test = new ManageMyIDScraper("eva.w.xiao@dartmouth.edu", "testpassword");
	}
}