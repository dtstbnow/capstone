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
import java.util.ArrayList;

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
		/*input = input.substring(input.indexOf("face") + 7);
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
		String data = Password + "//" + SMS + "//1//" + face + "//" + voice;*/
		ArrayList<String> parsedData = new ArrayList<String>();
		parsedData = ParseJson(input);
		String data = parsedData.get(2) + "//" + parsedData.get(3) + "//1//" + parsedData.get(4) + "//" + parsedData.get(5);
		String app = "/home/jonathan/Desktop/capstone/capstone/Milestone3/ServerCode/Registration/Registration/bin/Debug/Registration.exe";
		String[] args = { "mono", app, parsedData.get(0), parsedData.get(1), data };
		try {
			//Process p = new ProcessBuilder( args ).start();
			Process p = Runtime.getRuntime().exec(args);
			int errCode = p.waitFor();
			//System.out.println(p.getInputStream());
			//System.out.println(p.getErrorStream());
			response.getWriter().append("{\"Response\":\"REGISTERED\"}");
			//response.getWriter().append(face);
		}
		catch (Exception e){
			response.getWriter().append("{\"Response\":\"ERROR\"}");
		}
		finally {}
	}
	
	private ArrayList<String> ParseJson(String data)
	{
		ArrayList<String> parsed = new ArrayList<String>();
		for(int i = 0; i < 6; i++) {
			parsed.add("");
		}
		while(data.length() > 0) {
			String key = data.substring(2, data.indexOf(":") - 1);
			String value = data.substring(data.indexOf(":") + 2);
			if(value.contains(",")) {
				value = value.substring(0, value.indexOf(",") - 1);
			} else {
				value = value.substring(0, value.length() - 2);
			}
			
			System.out.println("Key: " + key + " Value: " + value);
			switch(key) {
			case "name": 
				parsed.set(0, value);
				break;
			case "macAdress":
				parsed.set(1, value);
				break;
			case "Password":
				parsed.set(2, value);
				break;
			case "SMS":
				parsed.set(3, value);
				break;
			case "face":
				parsed.set(4, value);
				break;
			case "voice":
				parsed.set(5, value);
				break;
			}
			data = data.substring(data.indexOf(":") + 2);
			if(data.contains(",")) {
				data = data.substring(data.indexOf(","));				
			} else {
				data = "";
			}
			
			
			System.out.println(data);
		}
		return parsed;
	}
}