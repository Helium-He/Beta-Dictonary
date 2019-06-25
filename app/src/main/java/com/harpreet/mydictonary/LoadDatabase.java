package com.harpreet.mydictonary;

import android.app.AlertDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;

import java.io.IOException;

public class LoadDatabase extends AsyncTask<Void,Void,Boolean> {

    private Context context;
    private AlertDialog alertDialog;
    private  Databasehelper mdatabasehelper;

    public LoadDatabase(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        AlertDialog.Builder d = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogueview = inflater.inflate(R.layout.alertdialogue_progressdialogue,null);
        d.setTitle("Loading Database...");
        d.setView(dialogueview);
        alertDialog = d.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        mdatabasehelper = new Databasehelper(context);
        try{
            mdatabasehelper.createDatabase();
        } catch (IOException e) {

            throw new Error("Database not found");
        }
        mdatabasehelper.close();
        return null;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        alertDialog.dismiss();
        MainActivity.openDatabase();
    }
}
