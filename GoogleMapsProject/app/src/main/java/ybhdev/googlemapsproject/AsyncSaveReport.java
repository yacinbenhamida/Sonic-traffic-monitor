package ybhdev.googlemapsproject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by USER on 12/11/2017.
 */

public class AsyncSaveReport extends AsyncTask<String,String,String> {
    Context activityContext;
    ProgressDialog pDialog;
    public AsyncSaveReport(Context ctx, ProgressDialog pDialog)
    {
        this.activityContext = ctx;
        this.pDialog = pDialog;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog.setMessage("Loading...");
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

    }


}
