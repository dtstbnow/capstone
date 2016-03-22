using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MFA
{
    class Launcher
    {
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
            string username = args[0];
            string macAddress = args[1];
            string verificationType = args[2];
            string verificationData = args[3];


            if (verificationType.Equals("register"))
            {
                Console.WriteLine("Processing registration request from user " + username);
                // parse userData string and pass result as attribute for new Registration object.
                Registration newUser = new Registration(username, macAddress, Parse(verificationData));
                newUser.InitializeDB(); // DB init
                newUser.InsertRecord(); // insert record if it doesn't exist
            }
            else if (verificationType.Equals("authenticate"))
            {
                Console.WriteLine("Processing authentication request from user " + username);
                // parse userData string and pass result as attribute for new Authentication object.
                Authentication authenticate = new Authentication(username, macAddress, Parse(verificationData));
            }
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
        private static string[] Parse(string data)
        {
            string[] userData = data.Split(new string[] { "//" }, System.StringSplitOptions.None);

            return userData;
        }
    }
}