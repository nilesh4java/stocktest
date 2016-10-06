/**
 * 
 */
package com.pubmatic.nilesh;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

/**
 * @author Nilesh
 *
 */
public class StockData {

	public static final int SYMBOL = 0;
	public static final int NAME = 1;
	public static final int BID_PRICE = 2;
	public static final int ASK_PRICE = 3;
	public static final int OPEN_PRICE = 4;
	public static final int YEAR_TARGET_PRICE = 5;
	public static final int YEAR_HIGH = 6;
	public static final int YEAR_LOW = 7;

	public static void main(String[] args) {
		// startTimeStamp for finding out how much time it took to get the final output
		long startTimeStamp = new Date().getTime();

		// readTestCaseFile() method will read the Test Case file that is provided and return it in the format required for passing it in the Yahoo Finance API
		String responseString = readTestCaseFile();

		try {
			// Making the Yahoo Finance API call
			String stockString = "http://finance.yahoo.com/d/quotes.csv?s=" + responseString + "&f=snbaot8kj";
			URL stockURL = new URL(stockString);
			BufferedReader in = new BufferedReader(new InputStreamReader(stockURL.openStream()));
			CSVReader reader = new CSVReader(in);
			List<String[]> csvContent = reader.readAll();

			if (csvContent != null && csvContent.size() > 0) {

				// Writing to the Output File
				String outputCSVFileName = "StockDataOutput_" + (new Date().getTime() + ".csv");
				CSVWriter writer = new CSVWriter(new FileWriter(outputCSVFileName));
				List<String[]> data = new ArrayList<String[]>();
				data.add(new String[] { "Stock Symbol", "Current Price", "Year Target Price", "Year High", "Year Low" });
				for (String[] row : csvContent) {
					String symbol = row[SYMBOL];
					String bidPrice = row[BID_PRICE];
					if (!(bidPrice.matches("[0-9.]*"))) {
						bidPrice = "-1";
					}
					String yearTargetPrice = row[YEAR_TARGET_PRICE];
					if (!(yearTargetPrice.matches("[0-9.]*"))) {
						yearTargetPrice = "-1";
					}
					String yearHigh = row[YEAR_HIGH];
					if (!(yearHigh.matches("[0-9.]*"))) {
						yearHigh = "-1";
					}
					String yearLow = row[YEAR_LOW];
					if (!(yearLow.matches("[0-9.]*"))) {
						yearLow = "-1";
					}
					data.add(new String[] { symbol, bidPrice, yearTargetPrice, yearHigh, yearLow });
				}
				writer.writeAll(data);
				writer.close();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long endTimeStamp = new Date().getTime();
		System.out.println(">>> Thanks. We got the output in " + (endTimeStamp - startTimeStamp) + " milliseconds only.");
	}

	public static String readTestCaseFile() {
		String responseString = "";
		BufferedReader br = null;
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader("Stocks.txt"));
			while ((sCurrentLine = br.readLine()) != null) {
				responseString += "+" + sCurrentLine;
			}
			if (responseString.startsWith("+")) {
				responseString = responseString.substring(1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return responseString;
	}
}