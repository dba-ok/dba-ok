package edu.dartmouth.cs65;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TransactionEntryDbHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "transactionentries.db";
	private static final int DATABASE_VERSION = 1;

    // Data types are defined below
    public static final String CREATE_TABLE_ENTRIES = "CREATE TABLE IF NOT EXISTS "
            + Globals.TABLE_NAME_ENTRIES
            + " ("
            + Globals.KEY_ROWID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Globals.KEY_DATE_TIME
            + " DATETIME NOT NULL, "
            + Globals.KEY_LOCATION
            + " INTEGER NOT NULL, "
//            + Globals.KEY_ACCOUNT_TYPE
//            + " INTEGER NOT NULL, "
//            + Globals.KEY_TRANSACTION_TYPE
//            + " INTEGER NOT NULL, "
            + Globals.KEY_AMOUNT + " FLOAT NOT NULL );";    
    
	private static final String[] mColumnList = new String[] { 
		Globals.KEY_ROWID, Globals.KEY_DATE_TIME, Globals.KEY_LOCATION, 
		Globals.KEY_AMOUNT };
	
    // Constructor  
    public TransactionEntryDbHelper(Context context) {
        // DATABASE_NAME is, of course the name of the database, which is defined as a string constant
        // DATABASE_VERSION is the version of database, which is defined as an integer constant
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create table schema if not exists    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ENTRIES);
    }

    // Insert a item given each column value
    public long insertEntry(TransactionEntry entry) {
    	SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        
		Timestamp timeStamp = new Timestamp(entry.getDateTimeinMillis()); 
		long dateTimeUTC = timeStamp.getTime();
    	
        values.put(Globals.KEY_DATE_TIME, dateTimeUTC); 
        values.put(Globals.KEY_LOCATION, entry.getLocation());
        values.put(Globals.KEY_AMOUNT, entry.getAmount()); 
        
        // Insert row 
        long id = db.insert(Globals.TABLE_NAME_ENTRIES, null, values); 
        db.close(); 
        
        // Return ID
        return id; 
    }
    
    public void insertEntryList(ArrayList<TransactionEntry> entryList){
    	for(TransactionEntry entry: entryList){
    		insertEntry(entry);
    	}
    }
    
    // Remove an entry by giving its index
    public void removeEntry(long rowIndex) {
    	SQLiteDatabase db = this.getWritableDatabase(); 
    	
        db.delete(Globals.TABLE_NAME_ENTRIES, Globals.KEY_ROWID+"="+rowIndex, null);
        
        db.close();    
    }
    
    public void deleteAllEntries(){
    	SQLiteDatabase db = this.getWritableDatabase(); 
    	db.delete(Globals.TABLE_NAME_ENTRIES, null, null);
    	db.close();
    }

    // Query a specific entry by its index.
    public TransactionEntry fetchEntryByIndex(long rowId) throws SQLException {
    	SQLiteDatabase dbObj = getReadableDatabase();
		TransactionEntry entry = null;
		// do the query with the condition KEY_ROWID = rowId
		Cursor cursor = dbObj.query(true, Globals.TABLE_NAME_ENTRIES, mColumnList,
				Globals.KEY_ROWID + "=" + rowId, null, null, null, null, null);

		// move the cursor to the first record
		if (cursor.moveToFirst()) {
			// convert the cursor to a TransactionEntry object
			entry = cursorToEntry(true, cursor);
		}

		// close the cursor
		cursor.close();
		dbObj.close();

		return entry;
    }

    
    /*
     * Query a specific entry by its account type--to find all DBA entries for example!
     *
    public TransactionEntry fetchEntryByAccountType(int accountType) throws SQLException{
    	SQLiteDatabase dbObj = getReadableDatabase();
		TransactionEntry entry = null;
		// do the query with the condition KEY_ACCOUNT_TYPE = accountType
		Cursor cursor = dbObj.query(true, Globals.TABLE_NAME_ENTRIES, mColumnList,
				Globals.KEY_ACCOUNT_TYPE + "=" + accountType, null, null, null, null, null);

		// move the cursor to the first record
		if (cursor.moveToFirst()) {
			// convert the cursor to a TransactionEntry object
			entry = cursorToEntry(true, cursor);
		}

		// close the cursor
		cursor.close();
		dbObj.close();

		return entry;

    } */
    
    
	// convert the row in the cursor to a TransactionEntry object
	private TransactionEntry cursorToEntry(boolean needGPS, Cursor cursor) {
		
		TransactionEntry entry = new TransactionEntry();
		
		Date date = new Date(cursor.getLong(cursor.getColumnIndex(Globals.KEY_DATE_TIME)));
		Calendar cal = Calendar.getInstance();
		cal.setTime(date); 		
		
		entry.setId(cursor.getLong(cursor.getColumnIndex(Globals.KEY_ROWID)));
//		entry.setDateTimeLong(cursor.getLong(cursor.getColumnIndex(Globals.KEY_DATE_TIME)));
		entry.setDateTime(cal); 
		entry.setLocation(cursor.getInt(cursor.getColumnIndex(Globals.KEY_LOCATION)));
		entry.setAmount(cursor.getDouble(cursor.getColumnIndex(Globals.KEY_AMOUNT)));
		
		return entry;
	}
	
	
    // Query the entire table, return all rows
    public ArrayList<TransactionEntry> fetchEntries() {
		SQLiteDatabase database = getWritableDatabase();
		ArrayList<TransactionEntry> entryList = new ArrayList<TransactionEntry>();
		Cursor cursor = database.query(Globals.TABLE_NAME_ENTRIES, mColumnList, 
				null, null, null, null, null);
		while (cursor.moveToNext()){
			TransactionEntry entry = cursorToEntry(false, cursor);
			entryList.add(entry);
		}
		cursor.close();
		database.close();
		return entryList;
	}

    /* fetch all entries corresponding a certain account, either mealswipe or DBA
    public ArrayList<TransactionEntry> fetchAccountEntries(int accountType){
    	SQLiteDatabase database = getWritableDatabase();
		ArrayList<TransactionEntry> entryList = new ArrayList<TransactionEntry>();
		Cursor cursor = database.query(Globals.TABLE_NAME_ENTRIES, mColumnList, 
				Globals.KEY_ACCOUNT_TYPE + "=" + accountType, null, null, null, null);
		while (cursor.moveToNext()){
			TransactionEntry entry = cursorToEntry(false, cursor);
			entryList.add(entry);
		}
		cursor.close();
		database.close();
		return entryList;
    } */
	
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
    		int newVersion) {
        Log.w(MainActivity.class.getName(), "Upgrading database from version "
            + oldVersion + " to " + newVersion
            + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS ");
        onCreate(database);
    }   


}
