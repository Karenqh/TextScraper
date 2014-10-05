Walmart.com Results Text Scraper

System Requirements
-----------------------
This text scraper is developed in Java with Eclipse. Execution of this program requires JVM. 

Overview of the Program
-----------------------
This program connects to a page on www.walmart.com and returns results about given keywords. Depending on the number of arguments, it calls different methods to either return the total number of results found or print out titles and prices of the products listed on the specified page. Contents is retrieved from a web page via URLConnection, and a Jsoup (http://jsoup.org/) method is called to parse the HTML and get elements containing information.
 
How To Run
-----------------------
Run the executable file from command line in the following two ways:

Query 1: input (requires a single argument)
	java -jar textscraper.jar <keyword>  (e.g. java -jar textscraper.jar "baby strollers")
Query 2: (requires two arguments)
	java -jar textscraper.jar <keyword> <page number> (e.g. java -jar textscraper.jar "baby strollers" 2)
 
FAQ
-----------------------
Q: What's in this folder?
A: Source codes in folder "src", an executable jar called textscraper.jar, and a README file.
   
Q: What external libraries did you use?
A: Jsoup (http://jsoup.org/), for parsing HTML.


(c) Qin He 2014
