import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;


public class PhoneBookSearchPost {
	static HashMap<String, HashMap<String, String>> phonebook; // lastname --> firstname --> number
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Content-type: text/html\r\n");
		System.out.println("<html>"
							+ "<head>" 
							+ "<title>Web-Based Phonebook</title>"
							+ "</head>"
							+ "<body>");
		String lastname = "";
		String firstname = "";
		try {
			String contentLength = System.getenv("CONTENT_LENGTH");
			byte[] buffer = new byte[Integer.parseInt(contentLength)];
			System.in.read(buffer);
			String input = new String(buffer);;

			
			// read firstname and lastname from URL
			// decode URL
			input = new URI(input).getPath();
			String[] splitInput = input.split("&");
			// search parameter-value pairs for firstname and lastname
			for (String pair: splitInput) {
				if (pair.split("=")[0].equals("firstname")) {
					firstname = pair.split("=")[1];
				} else if (pair.split("=")[0].equals("lastname")) {
					lastname = pair.split("=")[1];
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// read phonebook from file
		readPhoneBook();
		
		// test if name is in phonebook
		if (phonebook.containsKey(lastname) && phonebook.get(lastname).containsKey(firstname)) {
			// and print phone number
			System.out.println(firstname + " " + lastname + "'s phone number is " + phonebook.get(lastname).get(firstname));
		} else {
			// or error message
			System.out.println("Sorry - there's no phone number stored for " + firstname + " " + lastname);
		}
		
		System.out.println("</body></html>");
	}
	
	
	/*
	 * reads content of phonebook.txt and saves it into a HashMap of form
	 * lastname --> firstname --> phone number
	 */
	private static void readPhoneBook() {
		try {
			phonebook = new HashMap<String, HashMap<String, String>>();
			
			//File file = new File("WebContent/WEB-INF/phonebook.txt");
			File file = new File("../phonebook.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line=reader.readLine()) != null) {
				String[] splitLine = line.trim().split(",");
				String lastname = splitLine[0];
				String firstname = splitLine[1];
				String number = splitLine[2];
				if (!phonebook.containsKey(lastname)) {
					phonebook.put(lastname, new HashMap<String,String>());
				}
				phonebook.get(lastname).put(firstname, number);
			}					
					
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
