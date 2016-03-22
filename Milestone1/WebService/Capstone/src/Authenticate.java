

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Authenticate
 */
@WebServlet({ "/Authenticate", "/Auth" })
public class Authenticate extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Authenticate() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Nothing to see here...");
		//doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// read in the Json string
		DataInputStream reader = new DataInputStream(request.getInputStream());
		byte[] bytes = new byte[request.getContentLength()];
		reader.read(bytes);
		String input = new String(bytes);
		
		//split out the data from the read-in string		
		input = input.substring(input.indexOf("macAddress") + 14);
		String macAddress = input.substring(0, input.indexOf("verType") - 3);
		input = input.substring(input.indexOf("verType") + 10);
		String verType = input.substring(0, input.indexOf("data") - 3);
		input = input.substring(input.indexOf("data") + 7);
		String data = input.substring(0, input.indexOf("name") - 3);
		input = input.substring(input.indexOf("name") + 7);
		String name = input.substring(0, input.length() - 1);
		System.out.println(name + " requests " + verType + " authentication from " + macAddress + " with " + data);
	}

}
