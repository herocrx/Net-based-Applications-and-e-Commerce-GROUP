import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class WebshopServlet extends HttpServlet {
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {	
		// get current session or create a new one
		HttpSession session = request.getSession(true);
		
		// get item and add it to the cart
		String item = request.getParameter("item");
		
		// add item to cart
		ArrayList<String> items = new ArrayList<String>();
		// get items previously added to cart (if the session already existed)
		if (!session.isNew()) {
			items = (ArrayList<String>) session.getAttribute("items");
		}
		items.add(item);
		// add current item list to session
		session.setAttribute("items", items);
		
		// get number of items in cart
		int numItems = items.size();
		
		// set response type to html
		response.setContentType("text/html");
		
		// write html page
		PrintWriter writer = response.getWriter();
		writer.println( "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">"
						+ "<html>\n" 
						+ "<head>\n"
						+ "<title>Simple Webshop - New Item Added</title>\n"
						+ "</head>"
						+ "<body>");
		writer.println("New item (" + item + ") successfully added to the shopping cart.<br>\n" 
						+"Your cart contains " + numItems + " items.<br>");
		writer.println("<a href=\"webshop.html\">Back to main page</a>");
		writer.println("</body>\n" 
						+ "</html>");
	}

}
