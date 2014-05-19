

import java.io.IOException;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class JSoupTest{
	private Document mainPage;
	//private Map<String, String> cookies;
	private String cookie;
	private static final String LOGIN_PAGE = "https://dartmouth.managemyid.com/student/login.php";
	private static final String WELCOME_PAGE = "https://dartmouth.managemyid.com/student/welcome.php";
	
	public JSoupTest(){
		boolean success = false;
		
        try { //try to connect to ManageMyID
            mainPage = Jsoup.connect(LOGIN_PAGE).timeout(0).get();
            success = true;
        }
        catch (IOException e) {
        	System.out.println("IOException caught");
        }           
			
		System.out.println("success is " + success);//for testing
	}
	
	public void authenticate(String username, String password){ //get cookie for ManageMyID
		try{ //try to login
			Document doc = Jsoup
					.connect("https://dartmouth.managemyid.com/student/login.php")
					.data("user", username)
					.data("pwd", password)
					.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.137 Safari/537.36")
					.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
					.header("Accept-Encoding", "gzip, deflate")
					.header("Accept-Language", "en-US,en;q=0.5")
					.header("Connection", "keep-alive")
					.method(Connection.Method.POST)
					.timeout(0)
					.get();
			Elements links = doc.select("a[href]"); //find links just to see what the page looks like
			System.out.println(links); //are we still on login.php?
			
			String sessionId = res.cookie("PHPSESSID");
			System.out.println("Cookie? " + sessionId);
			cookie = sessionId; //save ManageMyID cookie
		}
		catch(IOException e){
			e.printStackTrace();
			System.out.println("Authentication failed.");
		}
		
	}
	
	private Document connectWithCookie(){
		Document doc = null;
		
		try{ //use cookie to get to user's welcome page
			doc = Jsoup.connect(WELCOME_PAGE).cookie("PHPSESSID", cookie).get();
			return doc;
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		return doc;
	}
	
	public static void main(String[] args){
		JSoupTest test = new JSoupTest();
		
	}
}