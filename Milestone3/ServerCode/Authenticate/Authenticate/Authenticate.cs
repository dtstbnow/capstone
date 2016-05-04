using System;
using MySql.Data.MySqlClient;
using System.Data;
using System.Configuration;
using System.IO;

namespace Authenticate
{
	internal class Authenticate
	{
		// MySql connection string
		private static String connectionString;

		// user data
		private static string username;
		private static string macAddress;
		//private static string password;

		private static string authenticationType;

		//private static string facialData;
		//private static string vocalData;

		/// <summary>
		/// The entry point of the program, where the program control starts and ends.
		/// </summary>
		/// <param name="args">authenticationType username macAddress authenticationData</param>
		/// <returns>Returns 0 on failure, 1 on success, -1 for unrecognized authentication type</returns>
		static int Main (string[] args)
		{
			username = args [0];
			macAddress = args [1];

			//Get first authentication type
			authenticationType = GetFirstAuthType ();

			//do authentication
			bool passed = false;
			switch (authenticationType) {
			case "pwd":
				passed = TestPassword (args [2]);
				break;
			case "face":
				passed = TestFacial (args [2]);
				break;
			case "voice":
				passed = TestVocal (args [2]);
				break;
			case "sms":
				passed = SMS (args [2]);
				break;
			}

			if (!passed) {
				//WriteResult ("0");
				return 0;
			}

			//Get second authentication type
			authenticationType = GetSecondAuthType ();

			//Do second authentication
			passed = false;
			switch (authenticationType) {
			case "pwd":
				passed = TestPassword (args [3]);
				break;
			case "face":
				passed = TestFacial (args [3]);
				break;
			case "voice":
				passed = TestVocal (args [3]);
				break;
			case "sms":
				passed = SMS (args [3]);
				break;
			}

			if (!passed) {
				//WriteResult ("0");
				return 0;
				//return;
			}

			//WriteResult ("1");
			return 1;
		}

		/// <summary>
		/// Attempts to authenticate user through password.
		/// </summary>
		/// <returns><c>true</c>, if password matches, <c>false</c> otherwise.</returns>
		/// <param name="pass">user's password</param>
		private static bool TestPassword(String pass)
		{
			// connection string data
			connectionString = "SERVER=localhost; DATABASE=MultiAuth; UID=root; PASSWORD=!!capstone2016heckyes!!; Allow User Variables=True";
			string userPassword = ""; // user's stored password

			using (var con = new MySqlConnection(connectionString))
			{
				string cmdString = "SELECT password FROM MultiAuth.users WHERE username=@username"; // Sql query
				MySqlCommand cmd = new MySqlCommand(cmdString, con);
				cmd.Parameters.AddWithValue("@username", username);
				con.Open(); // open connection

				using (MySqlDataReader reader = cmd.ExecuteReader())
				{
					while (reader.Read())
					{
						userPassword = reader["password"].ToString(); // Retrieve password
					}

					con.Close(); // close connection
				}
			}

			// Return true on match, false otherwise
			return pass.Equals (userPassword);

		}

		/// <summary>
		/// Attempts to authenticate user through vocal data.
		/// </summary>
		/// <returns><c>true</c>, if vocal matches, <c>false</c> otherwise.</returns>
		/// <param name="vD">user's vocaldata</param>
		private static bool TestVocal(String vD)
		{
			return false;
		}

		/// <summary>
		/// Attempts to authenticate user through facial data
		/// </summary>
		/// <returns><c>true</c>, if facial data matches, <c>false</c> otherwise.</returns>
		/// <param name="fD">user's facialdata</param>
		private static bool TestFacial(String fD)
		{
			return false;
		}

		private static bool SMS(String smsCode)
		{
			//retrieve SMS Code from DB
			String storedCode = "";
			String connectionString = "SERVER=localhost; DATABASE=MultiAuth; UID=root; PASSWORD=!!capstone2016heckyes!!; Allow User Variables=True";

			using (var con2 = new MySqlConnection(connectionString)) { // dispose of connection when finished
				con2.Open (); // open a connection
				using (MySqlTransaction trans2 = con2.BeginTransaction()) {
					try {

						//command to execute query
						using (MySqlCommand cmd = new MySqlCommand("getsmscode", con2, trans2))
						{
							cmd.CommandType = CommandType.StoredProcedure;

							cmd.Parameters.AddWithValue("paramUserName", username);
							cmd.Parameters.AddWithValue("paramMacAddress", macAddress);

							using (MySqlDataReader reader = cmd.ExecuteReader()) {
								while(reader.Read()) {
									storedCode = reader["code"].ToString();
								}
							}
							cmd.Parameters.Clear(); // Clear params
						}
						trans2.Commit();
					}
					catch(Exception ex)
					{
						Console.WriteLine(ex.ToString());
						trans2.Rollback();
					}
				}
			}
			return smsCode.Equals (storedCode);
		}

		private static String GetFirstAuthType()
		{
			return GetAuthType ("getFirstAuthType", "methodOne");
		}

		private static String GetSecondAuthType()
		{
			return GetAuthType ("getSecondAuthtype", "methodTwo");
		}

		private static String GetAuthType(String sp, String varName)
		{
			String returnValue = "";
			String connectionString = "SERVER=localhost; DATABASE=MultiAuth; UID=root; PASSWORD=!!capstone2016heckyes!!; Allow User Variables=True";

			using (var con2 = new MySqlConnection(connectionString)) { // dispose of connection when finished
				con2.Open (); // open a connection
				using (MySqlTransaction trans2 = con2.BeginTransaction()) {
					try {

						//command to execute query
						using (MySqlCommand cmd = new MySqlCommand(sp, con2, trans2))
						{
							cmd.CommandType = CommandType.StoredProcedure;

							cmd.Parameters.AddWithValue("paramUserName", username);
							cmd.Parameters.AddWithValue("paramMacAddress", macAddress);

							using (MySqlDataReader reader = cmd.ExecuteReader()) {
								while(reader.Read()) {
									returnValue = reader[varName].ToString();
								}
							}
							cmd.Parameters.Clear(); // Clear params
						}
						trans2.Commit();
					}
					catch(Exception ex)
					{
						Console.WriteLine(ex.ToString());
						trans2.Rollback();
					}
				}
			}
			return returnValue;
		}
	}
}
