package ybhdev.googlemapsproject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by USER on 12/11/2017.
 */

public class AsyncSaveReport extends AsyncTask<String,String,String> {
    Context activityContext;
    String save_pos_url = "http://192.168.1.5:8000/savepost.inc.php";
    String operation;
    public AsyncSaveReport(Context ctx)
    {
        this.activityContext = ctx;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... strings) {
        operation = strings[0];
        if (operation.equals("savepost")) {

            try {
                Double anchorX = Double.parseDouble(strings[1]);
                Double anchorY = Double.parseDouble(strings[2]);
                String fulladr = strings[3];
                int intensity = Integer.parseInt(strings[4]);
                String user = strings[5];
                URL url = new URL(save_pos_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                String post_date = URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(user,"UTF-8")
                        +"&"+URLEncoder.encode("x","UTF-8")+"="+URLEncoder.encode(anchorX.toString(),"UTF-8")+
                        "&"+URLEncoder.encode("y","UTF-8")+"="+URLEncoder.encode(anchorY.toString(),"UTF-8")+
                        "&"+URLEncoder.encode("adr","UTF-8")+"="+URLEncoder.encode(fulladr,"UTF-8")+
                        "&"+URLEncoder.encode("intensity","UTF-8")+"="+URLEncoder.encode(String.valueOf(intensity),"UTF-8");
                bufferedWriter.write(post_date);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"ISO-8859-1"));
                String res ="";
                String line ="";
                while((line=bufferedReader.readLine())!=null){res+=line;}
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return res;

           } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        return null;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }


}
