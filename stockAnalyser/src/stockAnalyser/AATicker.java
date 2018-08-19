package stockAnalyser;


import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import javax.net.ssl.HttpsURLConnection;

public class AATicker	
{
private static String AA_URL 	= "https://www.alphavantage.co/query?function=@SRS@&symbol@SYM@";

private static String AA_API_KEY= "&apikey=HFB1PJY3VGDJN3O3";

private static String SERIES		= "@SRS@";
private static String SYMBOL		= "@SYM@";
private static String ONE_MINUTE	= "&interval=1min";
private static int BUF_SLAB		= 512;

private HttpsURLConnection https;
private InputStream outFromWebServer;
private StringBuilder buffer;
private String urlTemplate;
private String series;
private boolean history;

public AATicker()
	{
	}	//END constructor
/**
 * Get current prices for the list of stock symbols (tickers). Only use this when the Stock Market is open,
 * which is normally between 09:30 and 16:00 EST on week days.
 * @param symbols
 *  A comma delimited list of symbols, such as
 *  'AAPL,FB,JNJ,TSLA'
 * @return
 *  A list of Quote objects
 * @throws Exception
 */

public Vector <Quote> getMultipleStockQuotes(String symbols) throws Exception
	{
	System.out.println("Getting Current Prices for multiple stocks");
	setTemplate(true, false);
	if(callAlphaAdvantage(false, symbols) != 0)
		{
		throw new Exception("Multiple quoting fails");
		}	//END encountered service problem
	else
		return findStockQuotes();
	}	//END get an historical quote
/**
 * Get current and/or historic prices for a single stock symbol (ticker). 
 * @param symbol 
 *  such as 'AAPL'
 * @param history
 *  If true then retrieve stock price history for specific date
 * @param when
 *  The historic date
 * @return
 *  A Quote object
 * @throws Exception
 */
public Quote getStockQuote(String symbol, boolean history, Calendar when)
	{
	int rc = 0;
	Quote quote = null;
	this.history = history;
	setTemplate(false, history);
	System.out.println("Starting AlphaAdvantage for '"+symbol+"'");
	if((rc = callAlphaAdvantage(true, symbol)) != 0)
		{
		quote = new Quote(symbol, when);
		quote.setCondition(-1);
		if(rc == Quote.AA_NO_SERVICE)
			quote.setCondition(Quote.AA_NO_SERVICE);
		}	//END encountered service problem
	else
		quote = findHistoricQuote(symbol, when);
	System.out.println("Ending AlphaAdvantage for '"+symbol+"'");
	return quote;
	}	//END get an historical quote

private void setTemplate(boolean immediate, boolean history)
	{
	if(immediate)
		{
		urlTemplate = new String (AA_URL+AA_API_KEY).replace(SERIES, TickerType.BATCH_STOCK_QUOTES.name());
		series = STOCK_QUOTES;	
		}
	else
	if(history)
		{
		urlTemplate = new String (AA_URL+ONE_MINUTE+AA_API_KEY).replace(SERIES, TickerType.TIME_SERIES_WEEKLY_ADJUSTED.name());
		series = TIME_SERIES_WEEKLY;
		}
	else
		{
		urlTemplate = new String (AA_URL+ONE_MINUTE+AA_API_KEY).replace(SERIES, TickerType.TIME_SERIES_INTRADAY.name());
		series = TIME_SERIES_INTRADAY;
		}	
	}	//END set url template

private int callAlphaAdvantage(boolean single, String symbol)
	{
	int rc = 0;
	String ss = "="+symbol;
	if(!single)
		ss = "s"+ss;
	String tickerURL = urlTemplate.replace(SYMBOL, ss);	
	try
		{
		URL url = new URL(tickerURL);
		https = (HttpsURLConnection)url.openConnection(); 
		https.setDoInput(true);
		https.setDoOutput(true);
		https.setRequestMethod("GET");
		https.setRequestProperty("Content-Type", "text/html; charset=utf-8");

    	if(https.getResponseCode() != 200)
    		{
    		System.out.println("RC="+https.getResponseCode()+" "+https.getResponseMessage());
    	    rc = https.getResponseCode();
    	    }
    	else
    		{
    		outFromWebServer =  new DataInputStream(https.getInputStream());
    		if(!loadBuffer())
    			rc = -1;
    		outFromWebServer.close();
    		}	//END normal response
    	}
    catch (IOException e)
    	{
    	System.out.println("1. HTTPS FAIL="+e.getMessage());
    	rc = -1;
    	}
	return rc;
	}	//END make a request to AlphaAdvantage
private static String TIME_SERIES_WEEKLY 	= "Weekly Adjusted Time Series";
private static String TIME_SERIES_INTRADAY 	= "Time Series (1min)";
private static String STOCK_QUOTES			= "Stock Quotes";

private static String TSW_OPEN 				= "1. open";
private static String TSW_HIGH 				= "2. high";
private static String TSW_LOW 				= "3. low";
private static String TSW_CLOSE 			= "4. close";
private static String TSW_ADJUSTED 			= "5. adjusted close";
private static String TSW_VOLUME 			= "6. volume";
private static String TSW_DIVIDEND 			= "7. dividend amount";

protected SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
@SuppressWarnings("unused")
private Quote findHistoricQuote(String symbol, Calendar when)
	{
	Quote quote = null;
	String open = null, high=null, low=null, adjusted=null, volume = null, timestamp = null;
	String price=null, dividend=null;
	BigDecimal bigDividend = BigDecimal.ZERO;
	pointer = buffer.indexOf(series);
	if(pointer != -1)
		{
		pointer = pointer+series.length()+3;
		while(true)
			{
			pointer++;
			int endPtr = buffer.indexOf("}", pointer);
			if(endPtr == -1)
				break;
			int len = endPtr-pointer-1;
			char [] dst = new char [len];
			buffer.getChars(pointer, pointer+len, dst, 0);
//TODO buffer can include huge # of blanks, perhaps due to timing
			String text = new String(dst);
			int d0 = text.indexOf("\"")+1;			// left quote for date
			if(d0 <= 0)
				{
				pointer = endPtr;
				continue;
				}
			int d1 = text.indexOf("\"", d0);		// right hand quote for date
			String quoteDate = text.substring(d0, d1).replaceAll(" ","");
			if(quoteDate.length() > 10)
				quoteDate = quoteDate.substring(0, 10);		// strip off timestamp
			open = getNextElement(TSW_OPEN);
			high = getNextElement(TSW_HIGH);
			low = getNextElement(TSW_LOW);
			price = getNextElement(TSW_CLOSE);
			if(history)
				adjusted = getNextElement(TSW_ADJUSTED);
			volume = getNextElement(TSW_VOLUME);
			if(history)
				{
				dividend = getNextElement(TSW_DIVIDEND);
				if(dividend != null && !dividend.startsWith("\""))
					bigDividend = bigDividend.add(new BigDecimal(dividend));
				}
			pointer = buffer.indexOf("}", pointer);		//point to end of this group
	System.out.println(symbol+" HIST="+history+" DATE="+quoteDate+" PRICE="+price+" DIV="+dividend);
			if(!history || (quoteDate != null && !DateCalculation.getAnyDate(quoteDate).after(when)))
				{
				quote = new Quote(symbol, symbol, checkBigDecimal(price), 
									DateCalculation.getAnyDate(quoteDate));
				quote.setDividend(bigDividend);
				break;
				}	//END found desired period	*/
			}	//END loop thru buffer
		}	//END find series
	 return quote;
	}

private static BigDecimal checkBigDecimal(String value)
	{
	BigDecimal big = BigDecimal.ZERO;
	if(value == null || "".equals(value) || "null".equals(value))
		return big;
	value = value.replace(",","");	
	try
		{
		big = new BigDecimal(value);
		}
	catch (NumberFormatException e)
		{
//* let if default to zero
		}
	return big;
	}	//END set BigDecimal value
private static String ERROR_MSG	= "\"Error Message\":";
private boolean loadBuffer()
	{
	boolean rc = true;
	try
		{
		buffer = new StringBuilder(BUF_SLAB);
		int k = 0;
		while(true)
			{
			byte [] bytes = new byte [BUF_SLAB];
			k = outFromWebServer.read(bytes);
			buffer.append(new String(bytes, "UTF-8"));
			if(k == -1)
				break;
			}	//END loop filling buffer
		if(buffer.indexOf(ERROR_MSG) != -1)
			rc = false;
		}	
    catch (IOException e)
    	{
    	System.out.println("3. BUFFER FAIL="+e.getMessage());	
    	}
	return rc;
	}	//END load buffer

private static String STOCK_NAME 	= "1. symbol";
private static String STOCK_PRICE 	= "2. price";
private static String STOCK_VOLUME 	= "3. volume";
private static String STOCK_TIMESTAMP 	= "4. timestamp";
private int pointer;
private Vector <Quote> findStockQuotes()
	{
	Vector <Quote> quotes = new Vector <Quote> ();
	String symbol = null, price = null, volume = null, timestamp = null;
	pointer = buffer.indexOf(STOCK_QUOTES);
	pointer = pointer+STOCK_QUOTES.length()+4;
	while((symbol = getNextElement(STOCK_NAME)) != null )
		{
		price = getNextElement(STOCK_PRICE);
		volume = getNextElement(STOCK_VOLUME);
		timestamp = getNextElement(STOCK_TIMESTAMP);
		Calendar ts = DateCalculation.getAnyTimestamp(timestamp);
		Quote quote = new Quote(symbol, symbol, checkBigDecimal(price), ts);
		quote.setVolume(Double.valueOf(volume));
		quotes.add(quote);	
		System.out.println("Symbol="+symbol+" PRICE="+price+" VOL="+volume+" TS="+DateCalculation.getAnyTimestamp(ts));
		}	//END find price	
	return quotes;
	}

private String getNextElement(String name)
	{
	if((pointer = buffer.indexOf(name, pointer)) == -1)
		return null;
	pointer = pointer+(name.length()+4);
	int ending = buffer.indexOf("\"", pointer);
	int length = ending-pointer;
	char [] dst = new char [length];
	buffer.getChars(pointer, pointer+length, dst, 0);
	return new String(dst);
	}
}	//END AATicker class
