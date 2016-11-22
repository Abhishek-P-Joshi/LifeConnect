package com.example.amank.lifeconnect;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek on 11/14/2016.
 */

public class GetFromDatabase {

    public GetFromDatabase(){

    }

    public String JSON_String = null;
    public String myJSON;

    public String GetData(final String sql, final String FileSelect){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("sql", sql));


                InputStream inputStream = null;
                String result = null;
                try {
                    DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                    HttpPost httppost = null;
                    switch (FileSelect){
                        case FileName.ServerPHP.Demo:
                            httppost = new HttpPost("http://79.170.40.227/lifeconnect.com/DemoGet.php");
                            break;
                        case FileName.ServerPHP.Patient:
                            httppost = new HttpPost("http://79.170.40.227/lifeconnect.com/GetPatient.php");
                            break;
                        case FileName.ServerPHP.Doctor:
                            httppost = new HttpPost("http://79.170.40.227/lifeconnect.com/GetDoctor.php");
                    }
                    //HttpPost httppost = new HttpPost("http://79.170.40.227/lifeconnect.com/DemoGet.php");
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));


                    // Depends on your web service
                    //httppost.setHeader("Content-type", "application/json");

                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();

                    inputStream = entity.getContent();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    // Oops
                }
                finally {
                    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                myJSON=result;
                //delegate.processFinish(myJSON);
                //return myJSON;
            }
        }
        GetDataJSON g = new GetDataJSON();
        try{
            g.execute();
            JSON_String = g.get();
        }catch (Exception e){

        }
        return JSON_String;
    }



    public void setJSON_String (String myJSON){
        JSON_String = myJSON;
    }
}
