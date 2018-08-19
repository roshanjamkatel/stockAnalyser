package stockAnalyser;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateCalculation 
{
public static final SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");
public static final SimpleDateFormat YYYYMMDD_TS = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
public static final SimpleDateFormat YYYYMMDD_TS_ALT = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
public static final String TODAY 		= "TODAY";
public static final String YESTERDAY 	= "YESTERDAY";
public static final String DAY_AFTER	= "+";
public static final long MILLIS_IN_DAY	= 86400000;

/**
 * Create a Calendar for today's date with time stripped.		
 * @return
 *  Calendar for today.
 */
public static Calendar getTodaysDate()
	{
	Calendar date = new GregorianCalendar();
	stripTime(date);
	return date;
	}	//END get today's date
/**
 * Create a Calendar for today's date and time.		
 * @return
 *  Calendar for today's timestamp.
 */
public static Calendar getTodaysDateTime()
	{
	Calendar date = new GregorianCalendar();
	return date;
	}	//END get today's date
public static String getTodaysDateTimeStrAlt()
	{
	Calendar date = new GregorianCalendar();
	return YYYYMMDD_TS.format(date.getTime());
	}	//END get today's timestamp
public static String getTodaysDateTimeStr()
	{
	Calendar date = new GregorianCalendar();
	return YYYYMMDD_TS.format(date.getTime());
	}	//END get today's timestamp

public static String getCurrentTimeStr()
	{
	return DateCalculation.getTodaysDateTimeStr().substring(11, 16);
	}	//END get the current time 

/** Reset hour, minutes, seconds and milliseconds to exactly Zero hour
 * 
 * @param date
 *  Calendar from which time is to be stripped. 
 */
private static void stripTime(Calendar date)
	{
	date.set(Calendar.HOUR_OF_DAY, 0);
	date.set(Calendar.MINUTE, 0);
	date.set(Calendar.SECOND, 0);
	date.set(Calendar.MILLISECOND, 0);
	}	//END remove time from calendar
/**
 * Create a Calendar for any date value with time stripped.
 * @param yyyymmdd
 *  Any date value in the format 'yyyy-mm-dd'		
 * @return
 *  Calendar for any date, or null if the input date was invalid.
 */

public static Calendar getAnyDate(String yyyymmdd)
	{
	Calendar date = new GregorianCalendar();
	Date anyDate = null;
	try 
		{
		anyDate = Date.valueOf(yyyymmdd);
		date.setTime(anyDate);
		stripTime(date);
		}	//END try to create calendar date 
	catch (Exception e) 
		{
		date = null;
		}	//END catch invalid date instance	
	return date;
	}	//END create calendar for any date
public static Calendar getAnyTimestamp(String yyyymmddhhmmss)
	{
	Calendar date = new GregorianCalendar();
	Timestamp anyDate = null;
	try 
		{
		anyDate = Timestamp.valueOf(yyyymmddhhmmss);
		date.setTime(anyDate);
		}	//END try to create calendar date 
	catch (Exception e) 
		{
		date = null;
		}	//END catch invalid timestamp instance	
	return date;
	}	//END create calendar for any timestamp

public static Calendar getAnyDate(long when)
	{
	return getAnyDate(when, false);	
	}
public static Calendar getAnyDate(long when, boolean strip)
	{
	Calendar date = new GregorianCalendar();
	Date anyDate = null;
	try 
		{
		anyDate = new Date(when);
		date.setTime(anyDate);
		if(strip)
			stripTime(date);
		}	//END try to create calendar date 
	catch (Exception e) 
		{
		date = null;
		}	//END catch invalid date instance	
	return date;	
	}
public static String getAnyDate(Calendar when)
	{
	return YYYYMMDD.format(when.getTime());	
	}

public static String getAnyTimestamp(Calendar when)
	{
	return YYYYMMDD_TS.format(when.getTime());	
	}
/**
 * Get today's date as a long value.
 * @return
 *  A long containing today's date.
 */
public static long getToday()
	{
	return getTodaysDate().getTimeInMillis();
	}	//END get zero hour today in milliseconds
/**
 * Get tomorrow's date as a long value.
 * @return
 *  A long containing tomorrow's date.
 */
public static long getTomorrow()
	{
	return getDayAfter(getTodaysDate());
	}	//END get zero hour today in milliseconds
/**
 * Get yesterday's date as a long value.
 * @return
 *  A long containing yesterday's date.
 */
public static long getYesterday()
	{
	return getDayBefore(getTodaysDate());
	}	//END get zero hour today in milliseconds
/**
 * Get the long value of the day after a specified date.
 * @param date
 *  The specified date.
 * @return
 *  A long containing the day after the specified date.
 */
public static long getDayAfter(Calendar date)
	{
	return getDayAfter(date, 1);
	}	//END get zero hour 'tomorrow' in milliseconds
/**
 * Get the Calendar value of the day after a specified date.
 * @param date
 *  The specified date.
 * @return
 *  A Calendar containing the day after the specified date.
 */

public static Calendar getDayAfterAsCalendar(Calendar date, int n)
	{
	Calendar tomorrow = new GregorianCalendar();
	tomorrow.setTime(new Date(getDayAfter(date, n)));
	return tomorrow;
	}
/**
 * Get the long value of the date after a specified date is advanced by multiple days.
 * @param date
 *  The specified date.
 * @param days
 *  The number of days the date is to be advanced
 * @return
 *  A long containing advanced date.
 */  
public static long getDayAfter(Calendar date, int days)
	{
	Calendar temp = new GregorianCalendar();
	temp.setTimeInMillis(date.getTimeInMillis());
	temp.add(Calendar.DAY_OF_MONTH, days);
	return temp.getTimeInMillis();
	}	//END get zero hour 'tomorrow' in milliseconds
/**
 * Get the long value of the day before a specified date.
 * @param date
 *  The specified date.
 * @return
 *  A long containing the day before the specified date.
 */
public static long getDayBefore(Calendar date)
	{
	return getDayBefore(date, -1);
	}	//END get zero hour 'yesterday' in milliseconds
/**
 * Get the long value of 'n' days before a specified date.
 * @param date
 *  The specified date
 * @param days
 *  A negative number indicating the number of days before the specified date.
 * @return
 *  A long containing the day before the specified date.
 */
public static long getDayBefore(Calendar date, int days)
	{
	date.add(Calendar.DAY_OF_MONTH, days);
	return date.getTimeInMillis();
	}	//END get zero hour 'yesterday' in milliseconds
/**
 * Get the number of days between 2 dates
 * @param recent
 *  The time for the recent date
 * @param past
 *  The time for the past date
 * @return
 *  The number of days between the recent and past dates.
 */
public static int getDaysBetween(long recent, long past)
	{
	return Math.toIntExact((recent-past)/MILLIS_IN_DAY);
	}
/**
 * Calculate the (millisecond) calendar date value for a date expressed directly in format 'yyyy-mm-dd', or
 * indirectly by the literal TODAY or YESTERDAY. 
 * @param when
 *  The input date either in 'yyyy-mm-dd' format, or as a literal.
 * @return
 *  the date in milliseconds.
 */
public static long getSpecificDate(String when)
	{
	return getSpecificDate(when, 0, 0);
	}
/**
 * Calculate the (millisecond) calendar date value for a date expressed directly in format 'yyyy-mm-dd', or
 * indirectly by the literal TODAY or YESTERDAY. Optionally subtract a number of 'lookback' days.
 * @param when
 *  The input date either in 'yyyy-mm-dd' format, or as a literal.
 * @param lookback
 *  If not 0, then the number of days to be subtracted from the final value calculated from the first argument.
 * @return
 *  the date in milliseconds.
 */
public static long getSpecificDate(String when, int lookback)
	{
	return getSpecificDate(when, 0, lookback);
	}
/**
 * Calculate the (millisecond) calendar date value for a date expressed directly in format 'yyyy-mm-dd', or
 * indirectly by the literal TODAY, YESTERDAY, or '+' meaning the day after a passed in value. 
 * Optionally subtract a number of 'lookback' days.
 * @param when
 *  The input date either in 'yyyy-mm-dd' format, or as a literal.
 * @param ldate
 *  If not 0, then the millisecond value of a previously calculated calendar date. This value is only used
 *  when the when argument = '+' (day after literal).
 * @param lookback
 *  If not 0, then the number of days to be added to, or subtracted from if the value is negative, the final value
 *  calculated from the first 2 arguments.
 * @return
 *  the date in milliseconds.
 */
public static long getSpecificDate(String when, long ldate, int lookback)
	{
	long sdate = 0;
	if(when != null)
		{
		if(YESTERDAY.equals(when))
			sdate = DateCalculation.getYesterday();
		else
		if(TODAY.equals(when))
			sdate = DateCalculation.getToday();
		else
		if(DAY_AFTER.equals(when))
			{
			if(ldate != 0)
				{
				Calendar cdate = new GregorianCalendar();
				cdate.setTimeInMillis(ldate);
				return DateCalculation.getDayAfter(cdate);
				}	//END get the day after
			else
				return 0;		//* requires a date to be specified
			}	//END calculate day after
		else
			{
			Calendar whenDate = DateCalculation.getAnyDate(when);
			if(whenDate != null)
				sdate = whenDate.getTimeInMillis();
			}	//END handle actual date
		}	//END set date
	if(lookback != 0)
		{
		Calendar cdate = new GregorianCalendar();
		cdate.setTimeInMillis(sdate);
		sdate = DateCalculation.getDayBefore(cdate, lookback);
		}	//END apply the lookback 
	return sdate;
	}	//END get a specific date
}	//END time calculation class
