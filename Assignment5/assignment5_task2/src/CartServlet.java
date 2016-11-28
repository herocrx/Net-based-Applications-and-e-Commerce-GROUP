import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class CartServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// get current session if there is one (and do not create a new one)
		HttpSession session = request.getSession(false);
		
		ArrayList<String> items = new ArrayList<String>();
		// get items if there's already a session
		if (!session.isNew()) {
			items = (ArrayList<String>) session.getAttribute("items");
		}
		
		// write html page
		PrintWriter writer = response.getWriter();
		writer.println( "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">"
				+ "<html>\n" 
				+ "<head>\n"
				+ "<title>Simple Webshop - New Item Added</title>\n"
				+ "</head>"
				+ "<body>");
		// if there are no items, inform the user about this
		if (items.size() == 0) {
			writer.println("Sorry, your shopping cart is emtpy. Go back to the main page to add some items.<br>");
		// else print out list of items
		} else {
			writer.println("You have the following items in your shopping cart:");
			writer.println("<ul>");
			for (int i=0; i<items.size();i++) {
				writer.println("<li>Item #"+ (i+1) + ": " + items.get(i) + "</li>");
			}
			writer.println("</ul>");
		}
		
		writer.println("<a href=\"webshop.html\">Back to main page</a>");
		writer.println("</body>\n" 
				+ "</html>");
	}

}
