package stockAnalyser;

public class testFile {
	
	public static void main(String[] args) throws Exception {
		AATicker ticker = new AATicker();
		
		ticker.getMultipleStockQuotes("TWTR,AMZN,GOOG,SNAP");
		
	}
	
}



