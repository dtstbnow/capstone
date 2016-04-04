using System;
using MySql.Data.MySqlClient;
using System.Data;
using System.Configuration;

namespace Registration
{
	internal class Registration
	{
		// MySql variables;
		private static String connectionString;

		// user data variables
		private static string username;
		private static string macAddress;
		private static string password;

		private static string verificationData;
		private static string smsAddress;
		private static bool smsCapable;
		private static string facialData;
		private static string vocalData;

		#pragma warning disable 0649, 0169
		private static int userId;
		private static int deviceId;
		private static int userDeviceId;
		private static string facialDataId;
		private static string vocalDataId;
		#pragma warning restore 0649, 0169

		/// <summary>
		/// The web service will parse the JSON object and pass MFA.exe one argument, the userData string.
		/// 
		/// verificationData layout
		/// password//smsAddress//smsCapable//facialData//voiceData
		/// 
		/// </summary>
		/// <param name="args">username macAddress verificationType verificationData</param>
		static void Main(string[] args)
		{
			username = args [0];
			macAddress = args [1];
			verificationData = args[2];

			Parse(verificationData);

			InsertRecord();
		}

		/// <summary>
		/// Split the string and assign the data to their respected variables.
		/// 
		/// data String layout:
		/// password//smsAddress//smsCapable//facialData//vocalData
		/// 
		/// </summary>
		/// <param name="data">string array that holds user data seperated by a delimiter ("//")</param>
		/// <returns>returns userData array, ready to be passed to registration or authentication class</returns>
		private static void Parse(string data)
		{
			string[] userData = data.Split(new string[] { "//" }, System.StringSplitOptions.None);

			password = userData[0];
			smsAddress = userData[1];

			if (userData[2].Equals("1"))
			{ 
				smsCapable = true;
			}
			else if (userData[2].Equals("0"))
			{
				smsCapable = false;
			}

			facialData = userData[3];
			vocalData = userData [4];
		}

		/// <summary>
		/// Insert records into MySQL tables
		/// </summary>
		private static void InsertRecord()
		{
			InitializeDB(); // Initialize the database

			using (var con2 = new MySqlConnection(connectionString)) // dispose of connection when finished
			{
				con2.Open(); // open a connection

				using (MySqlTransaction trans = con2.BeginTransaction()) // begin SQL Transaction
				{
					try
					{
						//command to execute query
						using (MySqlCommand cmd = new MySqlCommand("insertuser", con2, trans))
						{
							cmd.CommandType = CommandType.StoredProcedure;

							cmd.Parameters.AddWithValue("paramUsername", username);
							cmd.Parameters.AddWithValue("paramPassword", password);

							cmd.ExecuteNonQuery();
							cmd.Parameters.Clear(); // Clear params
						}

						//command to execute query
						using (MySqlCommand cmd = new MySqlCommand("insertdevice", con2, trans))
						{
							cmd.CommandType = CommandType.StoredProcedure;

							cmd.Parameters.AddWithValue("paramMacAddress", macAddress);
							cmd.Parameters.AddWithValue("paramSmsCapable", smsCapable);
							cmd.Parameters.AddWithValue("paramSmsAddress", smsAddress);

							cmd.ExecuteNonQuery();
							cmd.Parameters.Clear(); // Clear params
						}

						//command to execute query
						using (MySqlCommand cmd = new MySqlCommand("insertuserdevice", con2, trans))
						{
							cmd.CommandType = CommandType.StoredProcedure;

							cmd.Parameters.AddWithValue("paramUserId", userId);
							cmd.Parameters.AddWithValue("paramDeviceId", deviceId);

							cmd.ExecuteNonQuery();
							cmd.Parameters.Clear(); // Clear params
						}

						//command to execute query
						using (MySqlCommand cmd = new MySqlCommand("insertfacialdata", con2, trans))
						{
							cmd.CommandType = CommandType.StoredProcedure;

							cmd.Parameters.AddWithValue("paramUserId", userId);
							cmd.Parameters.AddWithValue("paramFacialData", facialData);

							cmd.ExecuteNonQuery();
							cmd.Parameters.Clear(); // Clear params

						}

						//command to execute query
						using (MySqlCommand cmd = new MySqlCommand("insertvocaldata", con2, trans))
						{
							cmd.CommandType = CommandType.StoredProcedure;

							cmd.Parameters.AddWithValue("paramUserId", userId);
							cmd.Parameters.AddWithValue("paramVocalData", vocalData);

							cmd.ExecuteNonQuery();
							cmd.Parameters.Clear(); // Clear params

						}
						trans.Commit();
					}
					catch(Exception ex)
					{
						Console.WriteLine(ex.ToString());
						trans.Rollback();
					}
				}
			}
		}

		/// <summary>
		/// Initialization DB connectionString
		/// </summary>
		private static void InitializeDB()
		{
			connectionString = ConfigurationManager.ConnectionStrings ["MySQL"].ConnectionString;
		}
	}
}
