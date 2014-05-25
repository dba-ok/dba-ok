
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class TransactionEntry {

	public static final String DATE_FORMAT = "H:mm:ss MMM d yyyy";

	private long id;

	private Calendar mDateTime; // When does this transaction happen
	private int mLocation; // Where does this transaction happen
	private double mAmount; // Cost if debit, number of swipes if meal swipe
							// used

	public TransactionEntry() {
		mLocation = 0;
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

	// returns day in year
	@SuppressWarnings("static-access")
	public int getDateTimeDay() {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(this.getDateTimeinMillis());
		return calendar.DAY_OF_YEAR;
	}

	public long getDateTimeinMillis() {
		return mDateTime.getTimeInMillis();
	}

	public void setDateTimeLong(long dateTime) {
		mDateTime.setTimeInMillis(dateTime);
	}

	public void setDateTime(Calendar c) {
		mDateTime = c;
	}

	public int getLocation() {
		return mLocation;
	}

	public String getLocationString() {
		String location = "";
		switch (mLocation) {
		case Globals.KAF_LOCATION_INT:
			location = Globals.KAF_LOCATION;
			break;
		case Globals.COLLIS_LOCATION_INT:
			location = Globals.COLLIS_LOCATION;
			break;
		case Globals.FOCO_LOCATION_INT:
			location = Globals.FOCO_LOCATION;
			break;

		case Globals.NOVACK_LOCATION_INT:
			location = Globals.NOVACK_LOCATION;
			break;

		case Globals.HOP_LOCATION_INT:
			location = Globals.HOP_LOCATION;
			break;

		case Globals.SNACKBAR_LOCATION_INT:
			location = Globals.SNACKBAR_LOCATION;
			break;
		}
		return location;
	}

	public void setLocation(int location) {
		mLocation = location;
	}

	public double getAmount() {
		return mAmount;
	}

	public void setAmount(double amount) {
		mAmount = amount;
	}

}
