import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WebClient {
	Socket socket;
	String host;
	String resourcePath;
	
	// gets the content type from the given header
	private String getContentType(String header) {
		String contentType = null;
		Pattern pattern = Pattern.compile("Content-Type:.+[\r\n]+");
		Matcher matcher = pattern.matcher(header);
		if (matcher.find()) {
			contentType = matcher.group(0).trim();
			contentType = contentType.replace("Content-Type: ", "").trim();
		}
		return contentType;
	}
	
	// creates the filename from the original url and the content type information
	private String getFilename(String header) {
		String filename = "";
		if (resourcePath.equals("/")) {
			filename += host.replace("www.", "");
			filename = filename.substring(0, filename.indexOf("."));
			filename += "_index";
		} else {
			filename += resourcePath.split("/")[resourcePath.split("/").length -1];
		}
		
		String type = getContentType(header);
		if (type.equals("text/html")) {
			filename += ".html";
		} else if (type.equals("text/plain")) {
			filename += ".txt";
		} else if (type.equals("image/jpeg") 
				&& !(resourcePath.endsWith("jpg") || resourcePath.endsWith("jpeg") || resourcePath.endsWith("jpe"))) {
			filename += ".jpg";
		}
		
		return filename;
	}
	
	// gets the HTTP-status code from the given header
	private String getHttpStatus(String header) {
		String status = null;
		Pattern pattern = Pattern.compile("HTTP.+[\r\n]+");
		Matcher matcher = pattern.matcher(header);
		if (matcher.find()) {
			status = matcher.group(0).trim();
			status = status.substring(status.indexOf(" ") + 1);
		}
		return status;
	}
	
	
	/*
	 * parses the URL (into host and resource path),
	 * assumes no authentication, port info and resource details given
	 */
	public void parseUrl(String url) {
		// remove protocol information
		url = url.replace("http://", "");
		int firstSlash = url.indexOf("/");
		host = url.substring(0, firstSlash);
		resourcePath = url.substring(firstSlash);
	}
	
	// method reads a line from the byte stream and returns it as a string
	private String readLine(DataInputStream inStream) throws IOException {
		String line = "";
		int nextChar;
		while ((nextChar = inStream.read()) != -1) {
			line += (char) nextChar;
			if (nextChar == (int) '\n') {
				break;
			}
		}
		return line;
	}
	
	/*
	 * opens a tcp socket to the server on port 80 and sends a HTTP request for the file
	 * and saves the file to the hard disk.
	 */
	public void sendHttpRequest() throws UnknownHostException, IOException {
		System.out.println("Connecting to server...");
		socket = new Socket(InetAddress.getByName(host), 80);
		
		// create and send HTTP request
		System.out.println("Sending file request...");
		String httpRequest = "GET " + resourcePath + " HTTP/1.0\r\n"
							+ "Host: " + host + "\r\n"
							+ "\r\n";
		PrintWriter outStream = new PrintWriter(socket.getOutputStream());
		outStream.println(httpRequest);
		outStream.flush();
		
		// receive HTTP response from server
		DataInputStream inStream = new DataInputStream(socket.getInputStream());
		// read header
		String header = "";
		String line;
		while (!(line = readLine(inStream).trim()).equals("")) {
			header += line + "\r\n";
		}
		String status = getHttpStatus(header);
		String contentType = getContentType(header);
		
		// file was successfully fetched
		if (status.equals("200 OK")) {
			System.out.println("File successfully requested. Saving file to hard disk...");
			// read HTTP response body and save received file to hard disk
			FileOutputStream fileStream = new FileOutputStream(getFilename(header));
			byte[] buffer = new byte[4096];
			int length;
			while ((length = inStream.read(buffer)) != -1) {
				fileStream.write(buffer, 0, length);
			}
			inStream.close();
			fileStream.close();
		// an error occurred when fetching the file
		} else {
			System.out.println("An error occurred when fetching the file: " + status);
		}
		
	}
	
}
