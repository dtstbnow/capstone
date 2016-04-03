package capstone.multifactorauthentication;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by Stabenow on 3/10/2016.
 */
public class JSONMessage {
    public JSONObject content = new JSONObject();
    public JSONMessage(Activity a){
        WifiManager manager = (WifiManager) a.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String address = info.getMacAddress();
        Log.v("is it null", ""+(address == null));
        Log.v("address", ""+address);
        try{
            content.accumulate("macAdress", address);
//            content.accumulate("name", "test");
        }catch (org.json.JSONException e){
            Log.v("Json error", e.toString());;
        }



    }

    public JSONMessage(JSONObject j){
        content = j;

    }

    public void AddFile(String name, File file){
        byte [] b;
        try{

//            byte[] b =  IOUtils.toByteArray(file);
            try{
                b = FileUtils.readFileToByteArray(file);
            }
            catch(java.io.IOException e){
                return;
            }


            content.accumulate(name, b);
            Log.v("success", content.toString());
        }
        catch(org.json.JSONException e)
        {
            Log.v("Json error", e.toString());

        }
    }

    public void AddBmp(String name, Bitmap bmp){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        try{
            content.accumulate(name, byteArray);
            Log.v("success", content.toString());
        }
        catch(org.json.JSONException e)
        {
            Log.v("Json error", e.toString());

        }
    }

    public void AddString(String name, String string){
        try{
            content.accumulate(name, string);
            Log.v("success", content.toString());
        }
        catch(org.json.JSONException e)
        {
            Log.v("Json error", e.toString());

        }
    }

    public JSONObject getContent(){
        return content;
    }

    public String getStringContent(){
        return content.toString();
    }


}
