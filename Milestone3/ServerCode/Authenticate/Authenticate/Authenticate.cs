using System;
using MySql.Data.MySqlClient;
using System.Data;
using System.Configuration;

namespace Authenticate
{
	internal class Authenticate
	{
		// MySql connection string
		private static String connectionString;

		// user data
		private static string username;
		private static string macAddress;
		private static string password;

		private static string authenticationType;
		private static string authenticationData;

		private static string smsAddress;
		private static bool smsCapable;

		private static string facialData;
		private static string vocalData;

		/// <summary>
		/// The entry point of the program, where the program control starts and ends.
		/// </summary>
		/// <param name="args">authenticationType username macAddress authenticationData</param>
		/// <returns>Returns 0 on failure, 1 on success, -1 for unrecognized authentication type</returns>
		static int Main (string[] args)
		{
			authenticationType = args [0];
			username = args [1];
			macAddress = args [2];

			// Authenticate with given type
			if (authenticationType.Equals ("password"))
			{ // Password authentication
				password = args [3];
				if (TestPassword (password))
				{
					//Console.Write ("SUCCESS!");
					return 1;
				}
				else
				{
					//Console.Write ("FAILURE!");
					return 0;
				}
			}
			else if (authenticationType.Equals ("vocal"))
			{ // Vocal authentication
				vocalData = args [3];
				// test vocaldata
				if (TestVocal (vocalData))
				{
					//Console.Write ("SUCCESS!");
					return 1;
				}
				else
				{
					//Console.Write ("FAILURE!");
					return 0;
				}

			}
			else if (authenticationType.Equals ("facial"))
			{ // Facial authentication
				facialData = args [3];
				// test facialdata
				if (TestFacial (facialData))
				{
					//Console.Write ("SUCCESS!");
					return 1;
				}
				else
				{
					//Console.Write ("FAILURE!");
					return 0;
				}
			}
			else
			{ // Unrecognized authentication type
				return -1;
			}
		}

		/// <summary>
		/// Attempts to authenticate user through password.
		/// </summary>
		/// <returns><c>true</c>, if password matches, <c>false</c> otherwise.</returns>
		/// <param name="pass">user's password</param>
		private static bool TestPassword(String pass)
		{
			// connection string data
			connectionString = "SERVER=localhost; DATABASE=MultiAuth; UID=root; PASSWORD=!!capstone2016heckyes!!;Allow User Variables=True";
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
			if(password.Equals(userPassword))
			{
				return true;
			}
			else
			{
				return false;
			}

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

	}
}
