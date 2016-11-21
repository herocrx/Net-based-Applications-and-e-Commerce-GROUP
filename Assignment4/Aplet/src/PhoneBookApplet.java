import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.swing.JApplet;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;


public class PhoneBookApplet extends JApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames =  {
			"First Name",
            "Last Name",
            "Phone Number"};
	private final int columnCnt = 3;

	private JTable table;
	private Object[][] data;
	

	
	public PhoneBookApplet() throws HeadlessException {
		// TODO Auto-generated constructor stub
	}

	public void init() {
        //Execute a job on the event-dispatching thread
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                	// Load phone book data from server.
                	loadPhoneBookData();
                	createGUI();
                }
            });
        } catch (Exception e) { 
            System.err.println("createGUI didn't complete successfully");
        }
        

    }
	
	void createGUI() {
		
		
		table = new JTable(data, columnNames);
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		setContentPane(scrollPane);
	}
	
	void loadPhoneBookData() {
		// Set data array.
		
		URL url;
		try {
			// read address book from URL
			//url = new URL("http://netappsvm.informatik.uni-stuttgart.de/~group12/phonebook.txt");
			url = new URL("http://duerr-vm1.informatik.uni-stuttgart.de/~duerrfk/phonebook.txt");
			URLConnection connection = url.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			ArrayList<String> addresslist = new ArrayList<String>();
			while ((line = reader.readLine()) != null) {
				addresslist.add(line);
			}
			
			data = new Object[addresslist.size()][columnCnt];
			// save addresslist to data object
			for (int i=0; i<addresslist.size(); i++) {
				String item = addresslist.get(i);
				String[] splittedLine = item.trim().split(",");
				data[i][0] = splittedLine[1]; // firstname
				data[i][1] = splittedLine[0]; // lastname
				data[i][2] = splittedLine[2]; // phone number
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
//		// TODO: Load phone book data from web server using an HTTP connection
//		// instead of setting dummy data.
//		int rowCnt = 1;
//		data = new Object[rowCnt][columnCnt];
//		data[0][0] = "John";
//		data[0][1] = "Doe";
//		data[0][2] = "5550815";
	}
}
