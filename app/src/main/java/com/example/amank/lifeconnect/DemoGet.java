package com.example.amank.lifeconnect;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DemoGet extends AppCompatActivity {

    String myJSON;

    private static final String TAG_RESULTS="result";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_AGE ="age";
    private static final String TAG_PLACE ="place";

    JSONArray peoples = null;

    ArrayList<HashMap<String, String>> personList;

    ListView list;
    private String JsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_get);

        list = (ListView) findViewById(R.id.listView);
        personList = new ArrayList<HashMap<String,String>>();
        String sql = "SELECT * FROM Demo";
        GetFromDatabase getFromDatabase = new GetFromDatabase();
        JsonString = getFromDatabase.GetData(sql, FileName.ServerPHP.Demo);
        //getData(sql);
        showList(JsonString);
    }

    protected void showList(String myJSON){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0;i<peoples.length();i++){
                JSONObject c = peoples.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String name = c.getString(TAG_NAME);
                String age = c.getString(TAG_AGE);
                String place = c.getString(TAG_PLACE);

                HashMap<String,String> persons = new HashMap<String,String>();

                persons.put(TAG_ID,id);
                persons.put(TAG_NAME,name);
                persons.put(TAG_AGE,age);
                persons.put(TAG_PLACE,place);

                personList.add(persons);
            }

            ListAdapter adapter = new SimpleAdapter(
                    DemoGet.this, personList, R.layout.list_item,
                    new String[]{TAG_ID,TAG_NAME,TAG_AGE,TAG_PLACE},
                    new int[]{R.id.id, R.id.name, R.id.age ,R.id.place}
            );

            list.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

//    public void getData(final String sql){
//        class GetDataJSON extends AsyncTask<String, Void, String> {
//
//            @Override
//            protected String doInBackground(String... params) {
//
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//                nameValuePairs.add(new BasicNameValuePair("sql", sql));
//
//
//                InputStream inputStream = null;
//                String result = null;
//                try {
//                    DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
//                    HttpPost httppost = new HttpPost("http://79.170.40.227/lifeconnect.com/DemoGet.php");
//                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//
//                    // Depends on your web service
//                    //httppost.setHeader("Content-type", "application/json");
//
//                    HttpResponse response = httpclient.execute(httppost);
//                    HttpEntity entity = response.getEntity();
//
//                    inputStream = entity.getContent();
//                    // json is UTF-8 by default
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
//                    StringBuilder sb = new StringBuilder();
//
//                    String line = null;
//                    while ((line = reader.readLine()) != null)
//                    {
//                        sb.append(line + "\n");
//                    }
//                    result = sb.toString();
//                } catch (Exception e) {
//                    // Oops
//                }
//                finally {
//                    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
//                }
//                return result;
//            }
//
//            @Override
//            protected void onPostExecute(String result){
//                myJSON=result;
//                showList(myJSON);
//                //return myJSON;
//            }
//        }
//        GetDataJSON g = new GetDataJSON();
//        g.execute();
//    }

}
