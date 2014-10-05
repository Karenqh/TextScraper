package walmart;

import java.io.*;
import java.net.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class TextScraper {
	
	// Read from an URL
	private String readURL (String target_url) {
		URL url;
		URLConnection urlConnection;
		BufferedReader reader;
		StringBuffer output;
		
		try {
			// Set up URL connection
			url = new URL(target_url);
			urlConnection = url.openConnection();
			
			// Read content from server
			reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			output = new StringBuffer();
			String line = "";
			while ((line = reader.readLine()) != null) {
				// Trim whitespace
				if ( !(line.trim().equals("")) ) {
					output.append(line + "\n");
				}
			}
			reader.close();	
			return output.toString().trim();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.out.println("MalformedURLException!");
			return "";
		} catch(IOException e){
			e.printStackTrace();
			System.out.println("IOException!");
			return "";
		}					
	}

	public int numberResult(String keywords) {
		// Pre-process keywords
		keywords = keywords.replace(" ", "%20");
		String address = "http://www.walmart.com/search/?query="
				+ keywords;
		// Retrieve contents
		String html = readURL(address);
		
		// Check failure
		if (html.equals("")) {
			System.out.println("Failed to retrieve info from web page.");
			return 0;
		}
		
		// Parse HTML
		Document doc = Jsoup.parse(html);			
		// Find the element that contains the result
		Element resultContainer = doc.getElementsByClass("result-summary-container").get(0);
		if (resultContainer == null) {
			System.out.println("Result container not found!");
			return 0;
		}
		// Get the text content
		String resultText = resultContainer.text();
		int start_index = resultText.indexOf("of ") + 3;
		int end_index = resultText.indexOf(" results");
		String resultNumber = resultText.substring(start_index, end_index);
		// Walmart doesn't include "," in number result, but just in case
		if (resultNumber.contains("c")) {
			resultNumber = resultNumber.replace(",", "");
		}			
		return Integer.parseInt(resultNumber);
	}

	private String getPage(String keywords, int pageNumber) {
		// http://www.walmart.com/search/?query=digital%20camera&page=2&cat_id=0
		keywords = keywords.replace(" ", "%20");
		// If the 1st page is requested
		if (pageNumber==1) {
			String address = "http://www.walmart.com/search/?query=" 
					+ keywords;
			return readURL(address);
		}
		// If page number greater 1 is given
		String page_str = "&page=" + pageNumber;
		String address = "http://www.walmart.com/search/?query="
				+ keywords + page_str + "&cat_id=0";
		
		return readURL(address);		
	}

	public void printResults(String keywords, int pageNumber) {
		// Find the target page for scraping
		String page = getPage(keywords, pageNumber);
		if (page.equals("")) {
			System.out.println("This page is not found.");
			return;
		} 
		
		// Parse the HTML
		Document doc = Jsoup.parse(page);
		// Fine elements containing titles and prices
		Elements titles = doc.getElementsByClass("js-product-title");
		Elements prices = doc.getElementsByClass("item-price-container");
		
		// Assume 16 results on each page
		for (int i=0; i<16; i++) {
			String title = "", price = "";
			
			// Get product title
			Element titleEl = titles.get(i);
			title = titleEl.text();
			//TODO: do we need to trim the title?
			
			// Get price
			// Types of price: $xxx.xx | From $xxx.xx | $xxx.xx - $xxx.xx
			Element priceEl = prices.get(i).child(0);
			if (priceEl.attr("class").equals("price-from")) {
				price = "From ";
				priceEl = prices.get(i).child(1);
			}
			// Get the price text and parse
			String priceString = priceEl.text();
			int start_index = priceString.indexOf("$");
			if ( start_index == -1) {
				price = "Price is not given";
			}
			price = price + priceString.substring(start_index);
			
			// Store in the Result object and print the product info		
			System.out.println(i+1);
			Result product = new Result(title, price);
			product.printInfo();
		}
	}
	
	
	//------- Main Routine ------
	public static void main(String[] args) {
		// Check arguments
		if (args.length==0 || args.length>2) {
			System.out.print("Please provide valid inputs!");
			return;
		}
		
		TextScraper ts = new TextScraper();
		
		// Query 1: return number of results
		if (args.length==1) {
			int number = ts.numberResult(args[0]);
			if (number==0){
				System.out.println("No results found!");
			} else {
				System.out.println("Number of result: " + number);
			}						
		}
		
		// Query 2: print list of results
		if (args.length==2) {
			try {
				int pageNumber = Integer.parseInt(args[1]);
				if (pageNumber<1) {
					System.out.println("Please input an integer greater than 0!");
					return;
				}
				// Print product information
				ts.printResults(args[0], pageNumber);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				System.out.println("Please provide an integer as a page number!");
				return;
			}
		}

	}
}
