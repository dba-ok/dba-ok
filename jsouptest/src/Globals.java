package edu.dartmouth.cs65;

import java.util.Calendar;

public class Globals {

	public static final String DATE_FORMAT = "H:mm:ss MMM d yyyy";
	
	public static final String TABLE_NAME_ENTRIES = "entries"; 
	public static final String KEY_ROWID = "id";
	public static final String KEY_DATE_TIME = "date_time"; 
	public static final String KEY_LOCATION = "location";
	public static final String KEY_ACCOUNT_TYPE = "account_type"; 
	public static final String KEY_TRANSACTION_TYPE = "transaction_type"; 
	public static final String KEY_AMOUNT = "amount"; 
	
	public static final int ACCOUNT_TYPE_DBA = 0;
	public static final int ACCOUNT_TYPE_MEAL = 1;
	
	
	// term start/end dates
	public static final Calendar FOURTEEN_SPRING_START = getCalendarForDate(2,24,2014);
	public static final Calendar FOURTEEN_SPRING_END = getCalendarForDate(5,3,2014);

	public static final Calendar FOURTEEN_SUMMER_START = getCalendarForDate(5,19,2014);
	public static final Calendar FOURTEEN_SUMMER_END = getCalendarForDate(7,26,2014);

	public static final Calendar FOURTEEN_FALL_START = getCalendarForDate(8,15,2014);
	public static final Calendar FOURTEEN_FALL_END = getCalendarForDate(10,26,2014);

	public static final Calendar FIFTEEN_WINTER_START = getCalendarForDate(0,5,2014);
	public static final Calendar FIFTEEN_WINTER_END = getCalendarForDate(2,17,2014);

	public static final Calendar FIFTEEN_SPRING_START = getCalendarForDate(2,30,2014);
	public static final Calendar FIFTEEN_SPRING_END = getCalendarForDate(5,14,2014);

	// term names 
	public static final String FOURTEEN_SPRING = "14S";
	public static final String FOURTEEN_SUMMER = "14X";
	public static final String FOURTEEN_FALL = "14F";
	public static final String FIFTEEN_WINTER = "15W";
	public static final String FIFTEEN_SPRING = "15S";

	// Location String constants
	public static final String KAF_LOCATION = "Kaf";
	public static final String COLLIS_LOCATION = "Collis";
	public static final String NOVACK_LOCATION = "Novack";
	public static final String FOCO_LOCATION = "Foco";
	public static final String HOP_LOCATION = "Hop";
	public static final String SNACKBAR_LOCATION = "EW Snackbar";
	public static final String COLLIS_MARKET_LOCATION = "Collis Market";

	// Location String constants
	public static final int KAF_LOCATION_INT = 0;
	public static final int COLLIS_LOCATION_INT = 1;
	public static final int NOVACK_LOCATION_INT = 2;
	public static final int FOCO_LOCATION_INT = 3;
	public static final int HOP_LOCATION_INT = 4;
	public static final int SNACKBAR_LOCATION_INT = 5;
	public static final int COLLIS_MARKET_LOCATION_INT = 6;
	
	// integer constants for daily vs. weekly spending
	public static final int AVG_DAILY_SPENDING = 0;
	public static final int AVG_WEEKLY_SPENDING = 1;

	
	// ACTUALLY GET THIS FROM MANAGEMYID AND SAVE TO SHARED PREFERENCES ****
	public static final double TOTAL_DBA_AMOUNT = 920.00;
	
	public static Calendar getCalendarForDate(int month,int day,int year){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH,month);
		c.set(Calendar.DAY_OF_MONTH,day);
		c.set(Calendar.YEAR,year);
		return c;
	}
	
	public final static String[] DAYS = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
	
	public static final int LATEST_CLOSE_TIME = 3;

}
