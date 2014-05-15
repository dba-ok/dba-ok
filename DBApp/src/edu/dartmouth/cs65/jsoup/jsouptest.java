package edu.dartmouth.cs65.jsoup;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class JSoupTest{
	private Document webpage;
	
	public JSoupTest(){
		try{
			webpage = Jsoup.connect("https://dartmouth.managemyid.com/student/login.php").get();
			
		}
		catch(IOException e){
			System.out.println("Connection to ManageMyID failed");
		}
	}
	
	public void authenticate(String username, String password){
		try{
			Document doc = Jsoup
					.connect("https://dartmouth.managemyid.com/student/login.php")
					.data("txtUserName", username)
					.data("txtPassword", password)
					.method(Connection.Method.POST)
					.get();
		}
		catch(IOException e){
			e.printStackTrace();
			System.out.println("Authentication failed.");
		}
	}
	
	public static void main(String[] args){
		JSoupTest test = new JSoupTest();
		System.out.println(test.webpage.data());
		
		//test.authenticate("eva.w.xiao@dartmouth.edu", "1dandelion");
		
	}
}