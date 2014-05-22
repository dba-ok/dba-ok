package edu.dartmouth.cs65.dbaok;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class TransactionEntry {
	
	public static final String DATE_FORMAT = "H:mm:ss MMM d yyyy";
	
	private long id; 
	
	private  Calendar mDateTime;    // When does this transaction happen
	private String mLocation;       // Where does this transaction happen
	private int mAccountType;   	// Which account did this use (DBA, SmartChoice 5, etc.)
	private int mTransactionType;   // Whether a meal swipe or debit was used (0 for mealswipe, 1 for debit)
	private double mAmount;  		// Cost if debit, number of swipes if meal swipe used

	public TransactionEntry() {
		mDateTime = Calendar.getInstance();    
		mLocation = "unknown";        
		mAccountType = 0;   
		mTransactionType = 0;  
		mAmount = 0;   
	}
	
	/* Getters and Setters */ 
	public long getId() {
		return id; 
	}
	
    public void setId(long id) {
        this.id = id;
    }
    
    public String getDateTimeString() {
    	GregorianCalendar calendar = new GregorianCalendar(); 
    	calendar.setTimeInMillis(this.getDateTimeinMillis()); 
    	SimpleDateFormat dateFormat; 
    	dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()); 
    	return dateFormat.format(calendar.getTime()); 
    }
    
    public long getDateTimeinMillis() {
        return mDateTime.getTimeInMillis();
    }
    
    public void setDateTimeLong(long dateTime) {
        mDateTime.setTimeInMillis(dateTime);
    }
    
    public void setDateTime(int year, int month, int day) {
    	mDateTime.set(year, month, day); 
    }
    
    public String getLocation() {
    	return mLocation; 
    }
    
    public void setLocation(String location) {
    	mLocation = location; 
    }
    
    public int getAccountType() {
    	return mAccountType; 
    }
    
    public void setAccountType(int accountType) {
    	mAccountType = accountType; 
    }
    
    public int getTransactionType() {
    	return mTransactionType; 
    }
    
    public void setTransactionType(int transactionType) {
    	mTransactionType = transactionType; 
    }
    
    public double getAmount() {
    	return mAmount; 
    }
    
    public void setAmount(double amount) {
    	mAmount = amount; 
    }

}
