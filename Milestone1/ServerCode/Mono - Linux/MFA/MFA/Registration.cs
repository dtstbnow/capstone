using System;
using MySql.Data.MySqlClient;
using System.Data;
using System.Configuration;

namespace MFA
{
	internal class Registration
	{
		// MySql variables;
		private String connectionString;

		// user data variables
		private string username;
		private string macAddress;
		private string password;
		private string smsAddress;
		private bool smsCapable;
		private string facialData;
		private string vocalData;

		#pragma warning disable 0649, 0169
		private int userId;
		private int deviceId;
		private int userDeviceId;
		private string facialDataId;
		private string vocalDataId;
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

			facialData = verificationData[3];
			vocalData = verificationData[4];
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
						using (MySqlCommand cmd = new MySqlCommand("insertuserdevices", con2, trans))
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
		public void InitializeDB()
		{
			connectionString = ConfigurationManager.ConnectionStrings ["MySQL"].ConnectionString;
		}
	}
}