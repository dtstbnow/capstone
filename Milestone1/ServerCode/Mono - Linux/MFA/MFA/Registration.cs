using System;
using MySql.Data.MySqlClient;
using System.Data;

namespace MFA
{
	internal class Registration
	{
		// MySql variables;
		private string connectionString;

		// user data variables
		private string username;
		private string macAddress;
		private string password;
		private string smsAddress;
		private bool smsCapable;
		private int facialData;
		private int vocalData;

		#pragma warning disable 0649, 0169
		private int userId;
		private int deviceId;
		private int userDeviceId;
		private int facialDataId;
		private int vocalDataId;
		#pragma warning restore 0649, 0169

		/// <summary>
		/// Registration constructor
		/// </summary>
		/// 
		/// <param name="verificationData">
		/// string array containing user data
		/// layout: [password,smsAddress,smsCapable,facialData,vocalData]
		/// </param>
		public Registration(string username, string macAddress, string[] verificationData)
		{
			this.username = username;
			this.macAddress = macAddress;

			password = verificationData[0];
			smsAddress = verificationData[1];

			if (verificationData[2].Equals("1"))
			{
				smsCapable = true;
			}
			else if (verificationData[2].Equals("0"))
			{
				smsCapable = false;
			}

			facialData = Convert.ToInt32(verificationData[3]);
			vocalData = Convert.ToInt32(verificationData[4]);
		}

		/// <summary>
		/// Insert records into MySQL tables
		/// </summary>
		public void InsertRecord()
		{
			using (var con2 = new MySqlConnection(connectionString)) // dispose of connection when finished
			{
				con2.Open(); // open a connection

				using (MySqlTransaction trans = con2.BeginTransaction()) // begin SQL Transaction
				{
					try
					{
						//command to execute query
						using (MySqlCommand cmd = new MySqlCommand(@"INSERT INTO MultiAuth.users (username, password) 
                            SELECT * FROM (SELECT ?username, ?password) as t 
                            WHERE NOT EXISTS (SELECT username FROM MultiAuth.users WHERE username=?username)", con2, trans))
						{
							cmd.Parameters.AddWithValue("?username", username);
							cmd.Parameters.AddWithValue("?password", password);

							cmd.ExecuteNonQuery();
							cmd.Parameters.Clear(); // Clear params
						}

						//command to execute query
						using (MySqlCommand cmd = new MySqlCommand(@"INSERT INTO MultiAuth.devices (macAddress, smsCapable, smsAddress) 
                            SELECT * FROM (SELECT ?macAddress, ?smsCapable, ?smsAddress) as t 
                            WHERE NOT EXISTS (SELECT macAddress FROM MultiAuth.devices WHERE macAddress=?macAddress)", con2, trans))
						{
							cmd.Parameters.AddWithValue("?macAddress", macAddress);
							cmd.Parameters.AddWithValue("?smsCapable", smsCapable);
							cmd.Parameters.AddWithValue("?smsAddress", smsAddress);

							cmd.ExecuteNonQuery();
							cmd.Parameters.Clear(); // Clear params
						}

						//command to execute query
						using (MySqlCommand cmd = new MySqlCommand(@"INSERT INTO MultiAuth.userDevices (userId, deviceId) 
                            SELECT users.userId, devices.deviceId FROM MultiAuth.users, MultiAuth.devices 
                            WHERE NOT EXISTS (SELECT userId, deviceId FROM MultiAuth.userDevices WHERE userId=?userId AND 
                            deviceId=?deviceId)", con2, trans))
						{
							cmd.Parameters.AddWithValue("?userId", userId);
							cmd.Parameters.AddWithValue("?deviceId", deviceId);
							cmd.Parameters.AddWithValue("?userDeviceId", userDeviceId);

							cmd.ExecuteNonQuery();
							cmd.Parameters.Clear(); // Clear params
						}

						//command to execute query
						using (MySqlCommand cmd = new MySqlCommand(@"INSERT INTO MultiAuth.facialData (userId, facialData) 
                            SELECT users.userId, ?facialData FROM MultiAuth.users 
                            WHERE NOT EXISTS (SELECT facialData FROM MultiAuth.facialData WHERE facialData=?facialData)", con2, trans))
						{
							cmd.Parameters.AddWithValue("?facialData", facialData);

							cmd.ExecuteNonQuery();
							cmd.Parameters.Clear(); // Clear params
						}

						//command to execute query
						using (MySqlCommand cmd = new MySqlCommand(@"INSERT INTO MultiAuth.vocalData (userId, vocalData) 
                            SELECT users.userId, ?vocalData FROM MultiAuth.users 
                            WHERE NOT EXISTS (SELECT vocalData FROM MultiAuth.vocalData WHERE vocalData=?vocalData)", con2, trans))
						{
							cmd.Parameters.AddWithValue("?vocalData", vocalData);

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
		public void InitializeDB()
		{
			string server = "localhost";
			string database = "MultiAuth";
			string uid = "root";
			string password = "!!capstone2016heckyes!!";

			connectionString = "Server=" + server + ";" + "Database=" +
				database + ";" + "Uid=" + uid + ";" + "Password=" + password +
					";Allow User Variables=True";
		}

		/// <summary>
		/// Test whether the MYSQL connection is open
		/// </summary>
		private void TestConnection()
		{
			using (var con = new MySqlConnection(connectionString))
			{
				con.Open();
				var temp = con.State.ToString();
				if (con.State == ConnectionState.Open && temp == "Open") // connection is open
				{
					Console.WriteLine("Connection is working.");
				}
				else // connection is closed - check connection string for errors
				{
					Console.WriteLine("Connection closed. Check connection string");
				}
			}
		}
	}
}