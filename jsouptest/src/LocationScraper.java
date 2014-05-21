import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LocationScraper{
    private final static String URL = "http://www.dartmouth.edu/dining/menus";
    private final static String[] LOCATIONS = {"Class of '53 Commons", "Collis", "Late Night Collis", "Collis Market", "Novack Cafe", 
    	"Courtyard Cafe"};
    private Map<String, String[]> operatingHours;
    private Document webpage;
    
    public LocationScraper(){
    	boolean success = false;
    	operatingHours = new HashMap<String, String[]>();
		
        try { //try to connect to ManageMyID
            webpage = Jsoup.connect(URL).timeout(0).get();
            success = true;
        }
        catch (IOException e) {
        	System.out.println("IOException caught");
        }           
			
		System.out.println("success is " + success);
    }
    
    
 // Query the webpage and fill the operatingHours variable with locations and their opening/closing times
    public void getLocationHours(){
    	Element hoursDiv = getHoursDiv();
    	Elements locations = getLocations(hoursDiv);
    	Elements times = getLocations(hoursDiv);
    	
    	for (Element location: locations){
    		
    	}
    	
    }

    private Element getHoursDiv(){
    	Elements divs = webpage.select("div");
    	Element hoursDiv = null;
    	
    	for (Element div: divs){
    		if (div.attr("id").equals("a-content")){
    			hoursDiv = div;
    		}
    	}
    	
    	return hoursDiv;
    }
    
    private Elements getLocations(Element div){
    	Elements locations = new Elements();
    	Elements children = div.getAllElements();
    	
    	for (Element child: children){
    		if (child.tagName().equals("h2")){
    			locations.add(child);
    		}
    	}
    	
    	return locations;
    }
    
    private Elements getTimes(Element div){
    	Elements times = new Elements();
    	Elements children = div.getAllElements();
    	
    	for (Element child: children){
    		if(child.tagName().equals("h6")){
    			times.add(child);
    		}
    	}
    	
    	return times;
    }
    
    // Query the hours of a specific location 
    //public String getHours(String location){
    //}

    // Check if a location is open or not using the current time
    //public boolean isOpen(String location){
    //}

    public static void main(String[] args){
    	LocationScraper test = new LocationScraper();
    	test.getLocationHours();
    }
}