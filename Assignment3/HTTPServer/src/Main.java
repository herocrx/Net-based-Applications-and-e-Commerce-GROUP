/**
* The program implements a simple web server that provides html
* and jpeg files for the given request. 
* The user then can view the file in the browser or get the error message.
*
* @author Ma³gorzata Kêska, Hubert Kuc, Beate Rothmund
* @version 1.0
* @since 2016-11-14
*/



public class Main {

	public static void main(String[] args) {
		TCPServer server=new TCPServer();
		while(true){
		server.HTTPresponse();		
		}
		}

}
