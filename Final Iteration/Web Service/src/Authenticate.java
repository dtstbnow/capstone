

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
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

/**
 * Servlet implementation class Authenticate
 */
@WebServlet({ "/Authenticate" })
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
		response.getWriter().append("Nothing here, buster!");
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
		ArrayList<String> parsedData = new ArrayList<String>();
		parsedData = ParseJson(input);
		String app = "/home/jonathan/Desktop/capstone/capstone/Milestone3/ServerCode/Authenticate/Authenticate/bin/Debug/Authenticate.exe";
		String[] args = { "mono", app, parsedData.get(0), parsedData.get(1), parsedData.get(2), parsedData.get(3)};
		try {
			//Process p = new ProcessBuilder( args ).start();
			Process p = Runtime.getRuntime().exec(args);
			
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String result = "", s = null;
			while ((s = stdInput.readLine()) != null) {
			    result = s;
			}
			System.out.println(result);
			int errCode = p.waitFor();
			
			System.out.println(errCode);
			//p.destroyForcibly();
			//while(p.isAlive()){}
			response.getWriter().append("{\"Response\":\"" + p.exitValue() + "\"}");
		}
		catch (Exception e)
		{
			response.getWriter().append("{\"Response\":\"400\"}");
		}
		finally {}
	}
	
	private ArrayList<String> ParseJson(String data)
	{
		ArrayList<String> parsed = new ArrayList<String>();
		for(int i = 0; i < 4; i++) {
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
			case "method1":
				parsed.set(2, value);
				break;
			case "method2":
				parsed.set(3, value);
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