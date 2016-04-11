package Capstone;
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
import java.lang.ProcessBuilder;
import java.lang.Process;

/**
 * Servlet implementation class Authenticate
 */
@WebServlet({ "/Register", "/Reg" })
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
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
		System.out.println(input);
		//split out the data from the read-in string		
		input = input.substring(input.indexOf("face") + 7);
		String face = input.substring(0,  input.indexOf("macAdress") - 3);		
		input = input.substring(input.indexOf("macAdress") + 12);
		String macAddress = input.substring(0, input.indexOf("voice") - 3);
		input = input.substring(input.indexOf("voice") + 8);
		String voice = input.substring(0, input.indexOf("SMS") - 3);
		input = input.substring(input.indexOf("SMS") + 6);
		String SMS = input.substring(0, input.indexOf("Password") - 3);
		input = input.substring(input.indexOf("Password") + 11);
		String Password = input.substring(0, input.indexOf("name") - 3);
		input = input.substring(input.indexOf("name") + 7);
		String name = input.substring(0, input.length() - 2);
		String data = Password + "//" + SMS + "//1//" + face + "//" + voice;
		//System.out.println(name + " requests " + verType + " authentication from " + macAddress + " with " + data);
		//response.getWriter().append("info received");
		//doPost(request, response);
		//System.out.println(name);
		String app = "/home/jonathan/Desktop/UM/Capstone/capstone/Milestone1/ServerCode/Registration/Registration/bin/Debug/Registration.exe";
		//System.out.println(app);
		String[] args = { "mono", app, name, macAddress, data };
		//System.out.println(args[0] + " " + args[1] + " " + args[2] + " " + args[3]);
		try {
			//Process p = new ProcessBuilder( args ).start();
			Process p = Runtime.getRuntime().exec(args);
			int errCode = p.waitFor();
			//System.out.println(p.getInputStream());
			//System.out.println(p.getErrorStream());
			response.getWriter().append("{\"Response\":\"REGISTERED\"}");
		}
		catch (Exception e){
			response.getWriter().append("{\"Response\":\"ERROR\"}");
		}
		finally {}
	}

}