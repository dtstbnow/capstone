using System;

namespace MFA
{
    internal class Authentication
    {
        // user data variables
        #pragma warning disable 0414
        private string username;
        private string macAddress;
        private string password;
        private string smsAddress;
        private bool smsCapable;
        private int facialData;
        private int vocalData;
        #pragma warning restore 0414

        /// <summary>
        /// Authentication constructor
        /// </summary>
        /// 
        /// <param name="userData">
        /// string array containing user data
        /// layout: [username,macAddress,password,smsAddress,smsCapable,facialData,vocalData]
        /// </param>
        public Authentication(string username, string macAddress, string[] verificationData)
        {
            this.username = username;
            this.macAddress = macAddress;

            password = verificationData[0];
            smsAddress = verificationData[1];

            if (verificationData[2] == "1")
            {
                smsCapable = true;
            }
            else if (verificationData[2] == "0")
            {
                smsCapable = false;
            }

            facialData = Int32.Parse(verificationData[3]);
            vocalData = Int32.Parse(verificationData[4]);
        }
    }
}