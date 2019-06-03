package com.example.weather;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    EditText url_text;

    String url_string;

    ProgressBar progress;

    TextView display_result;
    TextView errormessage;

    Toolbar tool_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        url_text=(EditText)findViewById(R.id.text_query);

        display_result=(TextView)findViewById(R.id.display_result);
        errormessage=(TextView)findViewById(R.id.display_error);

        progress=(ProgressBar)findViewById(R.id.progress_bar);

        //tool_bar=(Toolbar)findViewById(R.id.toolbar);
        //setSupportActionBar(tool_bar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.menu1:
                break;
            case R.id.menu2:
                break;
        }

        return true;
    }

    private void showJSONData(){
        errormessage.setVisibility(View.INVISIBLE);
        display_result.setVisibility(View.VISIBLE);

    }

    private void showerrormessage(){
        errormessage.setVisibility(View.VISIBLE);
        display_result.setVisibility(View.INVISIBLE);
    }

    //method to do something when button is clicked
    public void search(View view){
        url_string=url_text.getText().toString();

        Uri buildUri = Uri.parse("https://api.github.com/search/repositories").buildUpon()
                        .appendQueryParameter("q", url_string)
                        .appendQueryParameter("sort","stars")
                        .build();

        URL url = null;

        try{
            url = new URL(buildUri.toString());
        }catch(MalformedURLException e){
            e.printStackTrace();
        }

        new urltask().execute(url);

    }

    public class urltask extends AsyncTask<URL, Void, String>{

        protected void onPreExecute(){
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchurls = urls[0];
            String githubsearchresults=null;

            try{
                HttpURLConnection urlconnection = (HttpURLConnection) searchurls.openConnection();

                try{
                    InputStream istream = urlconnection.getInputStream();

                    Scanner scanner=new Scanner(istream);
                    scanner.useDelimiter("\\A");

                    boolean scanner_next=scanner.hasNext();

                    if(scanner_next){
                        return scanner.next();
                    }
                    else{
                        return null;
                    }

                }finally{
                    urlconnection.disconnect();
                }

            }catch(IOException e){
                e.printStackTrace();
            }

            return githubsearchresults;
        }

        protected void onPostExecute(String searchresults){
            progress.setVisibility(View.INVISIBLE);

            if(searchresults!=null && !searchresults.equals("")){
                showJSONData();
                display_result.setText(searchresults);
            }else{
                showerrormessage();
            }
        }
    }

    protected void onStart(){
        Log.i("START","The Activity have been started");
        super.onStart();
    }

    protected void onResume(){
        Log.i("RESUME","The Activity have been resumed");
        super.onResume();
    }

    protected void onStop(){
        Log.i("Stop","The Activity have been Stopped");
        super.onStop();
    }

    protected void onPause(){
        Log.i("Pause","The Activity have been paued");
        super.onPause();
    }

    protected void onDestroy(){
        Log.i("Destroy","The Activity have been destroyed");
        super.onDestroy();
    }
}
