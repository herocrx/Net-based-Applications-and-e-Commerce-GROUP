import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
* The program implements a simple web client that requests html, plain text 
* and jpeg files from the given URL. 
* The user is already presented with a list of URLs from which he/she can choose
* or he/she types in a different URL.
* The client then fetches the file and saves it to the hard disk or shows the
* user the HTTP-status code in case of an error.
*
* @author Małgorzata Kęska, Hubert Kuc, Beate Rothmund
* @version 1.0
* @since 2016-11-06
*/

public class MainWebClient {

	public static void main(String[] args) {
		try {
		// class handling the connection to the web server and saving the file 
		WebClient webClient = new WebClient();
		
		// preset list of URLs to choose from
		ArrayList<String> urls = new ArrayList<String>();
		urls.add("http://www.priloc.de/");
		urls.add("http://www.priloc.de/img/priloc.jpg");
		urls.add("http://www.ipvs.uni-stuttgart.de/abteilungen/vs/abteilung/mitarbeiter/john.doe/de");
		urls.add("http://www.google.com/");
		
		// reader for user input
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		
		boolean requestingFile = true;
		String userInput;
		String url;
		while (requestingFile) {
			
			System.out.println("Type in a URL or choose one of the preset URLs by typing in the corresponding number:");
			for (int i=0; i<urls.size(); i++) {
				System.out.println(i + " " + urls.get(i));
			}
			
			userInput = input.readLine().trim();
			if (userInput.matches("^\\d+$") && Integer.parseInt(userInput) < urls.size()) {
				url = urls.get(Integer.parseInt(userInput));
			} else {
				url = userInput;
			}
			
			// superficially check the validity of the given url
			if (url.startsWith("http://") && url.matches(".+\\w/.*")) {
				System.out.println("Requesting file from " + url);
				webClient.parseUrl(url);
				webClient.sendHttpRequest();
			} else {
				System.out.println("Not a valid URL. No file requested.");
			}
			
			while (true) {
				System.out.println("Do you want to request another file? (y/n)");
				userInput = input.readLine().trim();
				if (userInput.equals("n")) {
					requestingFile = false;
					break;
				} else if (userInput.equals("y")) {
					break;
				}
			}
				
			
		}
		
		} catch(UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
