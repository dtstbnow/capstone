

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

import java.util.Random;

/**
 * Servlet implementation class Reserve
 */
@WebServlet("/Reserve")
public class Reserve extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Reserve() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		//get username/mac address and add to Pending table
		//determine authentication type
		//return authentication type
		//doPost(request, response);
		// read in the Json string
		DataInputStream reader = new DataInputStream(request.getInputStream());
		byte[] bytes = new byte[request.getContentLength()];
		reader.read(bytes);
		String input = new String(bytes);
		System.out.println(input);
		ArrayList<String> parsedData = new ArrayList<String>();
		parsedData = ParseJson(input);
		
		/*input = input.substring(input.indexOf("macAdress") + 12);
		String macAddress = input.substring(0, input.indexOf("name") - 3);
		
		input = input.substring(input.indexOf("name") + 7);
		String name = input.substring(0, input.length() - 2);
		
		System.out.println(name);
		System.out.println(macAddress);*/
		
		ArrayList<String> methods = new ArrayList<String>();
		//methods.add("face");
		//methods.add("voice");
		methods.add("sms");
		methods.add("pwd");
		Random rand = new Random();
		int authMethod = rand.nextInt(methods.size());
		String methodOne = methods.get(authMethod);
		methods.remove(authMethod);
		authMethod = rand.nextInt(methods.size());
		String methodTwo = methods.get(authMethod);
		
		System.out.println(methodOne);
		
		//add to reservation table
		String app = "/home/jonathan/Desktop/capstone/capstone/Milestone3/ServerCode/Reserve/Reserve/Reserve/bin/Debug/Reserve.exe";
		String[] args = { "mono", app, parsedData.get(0), parsedData.get(1), methodOne, methodTwo };
		try {
			//Process p = new ProcessBuilder( args ).start();
			Process p = Runtime.getRuntime().exec(args);
			int errCode = p.waitFor();
			//System.out.println(p.getInputStream());
			//System.out.println(p.getErrorStream());
			//response.getWriter().append("{\"Response\":\"REGISTERED\"}");
			response.getWriter().append("{\"MethodOne\":\"" + methodOne + "\",\"MethodTwo\":\"" + methodTwo + "\"}");
		}
		catch (Exception e){
			//response.getWriter().append("{\"Response\":\"ERROR\"}");
			response.getWriter().append(e.getMessage());
		}
		finally {}
	}

	private ArrayList<String> ParseJson(String data)
	{
		ArrayList<String> parsed = new ArrayList<String>();
		for(int i = 0; i < 2; i++) {
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
