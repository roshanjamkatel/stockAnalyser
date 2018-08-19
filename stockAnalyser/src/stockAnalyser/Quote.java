package stockAnalyser;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Quote implements Cloneable
{
private SimpleDateFormat DATE_ONLY = new SimpleDateFormat("yyyy-MM-dd");
public static int AA_NO_SERVICE= 503;
private String ticker;
private String title;
private String exchange;
private Boolean closed;
private BigDecimal price;
private BigDecimal change;
private BigDecimal peg;
private BigDecimal dividend;
private double volume;
private	Calendar tradeDate;
private Calendar dividendDate;;
private boolean unknownTicker;
private int condition;

public Quote(String ticker, Calendar tradeDate)
	{
	this(ticker, ticker, null, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
			tradeDate, null, true);
	}
public Quote(String ticker, String title, BigDecimal price, Calendar tradeDate)
	{
	this(ticker, title, null, price, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
			tradeDate, null, false);
	}
public Quote(String ticker, String title, String exchange, BigDecimal price, BigDecimal change, BigDecimal peg, BigDecimal dividend,
				Calendar tradeDate, Calendar dividendDate, boolean unknownTicker)
	{
	this.ticker = ticker;
	this.title = title;
	this.exchange = exchange;
	this.price = price;
	this.change = change;
	this.peg = peg;
	this.dividend = dividend;
	this.tradeDate = tradeDate;
	this.dividendDate = dividendDate;
	this.unknownTicker = unknownTicker;
	}	//END constructor

public Quote clone() throws CloneNotSupportedException
	{
	return (Quote) super.clone();
	} // END clone method
public boolean isUnknownTicker()
	{
	return unknownTicker;
	}
public void setUnknownTicker(boolean b)
	{
	unknownTicker = b;
	}
public boolean isClosed()
	{
	return closed;
	}
public void setIsClosed()
	{
	closed = true;
	}
public String getTicker()
	{
	return ticker;
	}
public BigDecimal getPrice()
	{
	return price;
	}

public void setPrice(BigDecimal price)
	{
	this.price = price;
	}

public double getVolume()
	{
	return volume;
	}

public void setVolume(double volume)
	{
	this.volume = volume;
	}
public BigDecimal getChange()
	{
	if(change == null)
		return BigDecimal.ZERO;
	return change;
	}

public void setChange(BigDecimal change)
	{
	this.change = change;
	}

public BigDecimal getPeg()
	{
	return peg;
	}

public void setPeg(BigDecimal peg)
	{
	this.peg = peg;
	}

public BigDecimal getDividend()
	{
	if(dividend == null)
		return BigDecimal.ZERO;
	return dividend;
	}

public void setDividend(BigDecimal dividend)
	{
	this.dividend = dividend;
	}

public Calendar getTradeDate()
	{
	return tradeDate;
	}
public String getTradeDateStr()
	{
	if(tradeDate == null)
		return "-";	
	return  DATE_ONLY.format(tradeDate.getTime());
	}
public void setTradeDate(Calendar tradeDate)
	{
	this.tradeDate = tradeDate;
	}

public int getCondition()
	{
	return condition;
	}
public void setCondition(int c)
	{
	condition = c;
	}
public Calendar getDividendDate()
	{
	return dividendDate;
	}
public String getDividendDateStr()
	{
	if(dividendDate == null)
		return "-";
	return  DATE_ONLY.format(dividendDate.getTime());
	}
public void setDividendDate(Calendar dividendDate)
	{
	this.dividendDate = dividendDate;
	}

public String getTitle()
	{
	return title;
	}
public String getExchange()
	{
	return exchange;
	}
}	//END quote class
