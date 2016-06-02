using System;
using MySql.Data.MySqlClient;
using System.Data;
using System.Configuration;
using System.Net;
using System.Net.Mail;
using System.Linq;
using System.Collections.Generic;
using System.Threading.Tasks;
using System.Text;

namespace Reserve
{
	internal class Reserve
	{
		private static string username;
		private static string macAddress;
		private static string methodOne;
		private static string methodTwo;

		static void Main (string[] args)
		{
			username = args [0];
			macAddress = args [1];
			methodOne = args [2];
			methodTwo = args [3];
			String connectionString = "SERVER=localhost; DATABASE=MultiAuth; UID=root; PASSWORD=!!capstone2016heckyes!!; Allow User Variables=True";

			using (var con1 = new MySqlConnection(connectionString)) { // dispose of connection when finished
				con1.Open (); // open a connection

				using (MySqlTransaction trans = con1.BeginTransaction()) { // begin SQL Transaction
					try {
						//command to execute query
						using (MySqlCommand cmd = new MySqlCommand("insertReservation", con1, trans)) {
							cmd.CommandType = CommandType.StoredProcedure;

							cmd.Parameters.AddWithValue ("paramUserName", username);
							cmd.Parameters.AddWithValue ("paramMacAddress", macAddress);
							cmd.Parameters.AddWithValue ("paramMethodOne", methodOne);
							cmd.Parameters.AddWithValue ("paramMethodTwo", methodTwo);

							cmd.ExecuteNonQuery ();
							cmd.Parameters.Clear (); // Clear params
						}
						trans.Commit ();
					} catch (Exception ex) {
						Console.WriteLine (ex.Message);
						trans.Rollback ();
					}
					trans.Dispose ();
				}
			}

			if(methodOne.Equals("sms")) {
				//send text message
				SendSms ();
			}

			if (methodTwo.Equals ("sms")) {
				//send text message
				SendSms ();
			}
		}

		private static void SendSms() {
			//get sms "address" from mac
			String phoneNumber = "";
			String connectionString = "SERVER=localhost; DATABASE=MultiAuth; UID=root; PASSWORD=!!capstone2016heckyes!!; Allow User Variables=True";

			using (var con = new MySqlConnection(connectionString)) // dispose of connection when finished
			{
				con.Open(); // open a connection

				using (MySqlTransaction trans = con.BeginTransaction()) { // begin SQL Transaction
					try {
						//command to execute query
						using (MySqlCommand cmd = new MySqlCommand("getsmsaddress", con, trans)) {
							cmd.CommandType = CommandType.StoredProcedure;

							cmd.Parameters.AddWithValue ("paramMacAddress", macAddress);

							//cmd.ExecuteNonQuery ();
							using (MySqlDataReader reader = cmd.ExecuteReader()) {
								while(reader.Read()) {
									phoneNumber = reader["smsaddress"].ToString();
								}
							}
							//userId = (int)cmd.ExecuteScalar();
							//userId = (int)cmd.LastInsertedId;
							//long temp = cmd.LastInsertedId;
							cmd.Parameters.Clear (); // Clear params
						}
						trans.Commit();
					} catch (Exception ex) {
						Console.WriteLine (ex.Message);
						trans.Rollback();
					}
					trans.Dispose ();
				}

			}


			MailMessage msg = new MailMessage ();
			msg.From = new MailAddress ("umcapstone2016@gmail.com");
			msg.To.Add (phoneNumber + "@txt.att.net");
			msg.To.Add (phoneNumber + "@message.alltel.com");
			msg.To.Add (phoneNumber + "@myboostmobile.com");
			msg.To.Add (phoneNumber + "@message.sprintpcs.com");
			msg.To.Add (phoneNumber + "@tmomail.net");
			msg.To.Add (phoneNumber + "@email.uscc.net");
			msg.To.Add (phoneNumber + "@vtext.com");
			msg.To.Add (phoneNumber + "@vmobl.com");

			//msg.Subject = "";

			const string chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
			var random = new Random();
			String message = new string(Enumerable.Repeat(chars, 6).Select(s => s[random.Next(s.Length)]).ToArray());
			msg.Body = message;



			using (var con2 = new MySqlConnection(connectionString)) { // dispose of connection when finished
				con2.Open ();
				using (MySqlTransaction trans2 = con2.BeginTransaction()) {
					try {

						//command to execute query
						using (MySqlCommand cmd = new MySqlCommand("setsmscode", con2, trans2)) {
							cmd.CommandType = CommandType.StoredProcedure;

							cmd.Parameters.AddWithValue ("paramUserName", username);
							cmd.Parameters.AddWithValue ("paramMacAddress", macAddress);
							cmd.Parameters.AddWithValue ("paramCode", message);

							cmd.ExecuteNonQuery ();
							cmd.Parameters.Clear (); // Clear params
						}
						trans2.Commit ();
					} catch (Exception ex) {
						Console.WriteLine (ex.ToString ());
						trans2.Rollback ();
					}
				}
			}

			SmtpClient client = new SmtpClient ();
			//client.UseDefaultCredentials = true;
			client.Host = "smtp.gmail.com";
			client.Port = 587;
			client.EnableSsl = true;
			client.DeliveryMethod = SmtpDeliveryMethod.Network;
			client.UseDefaultCredentials = false;
			client.Credentials = new NetworkCredential ("umcapstone2016@gmail.com", "fhjeifmjerikdsmv");
			client.Timeout = 2000000;
			client.Send (msg);
			msg.Dispose ();

		}
	}
}

