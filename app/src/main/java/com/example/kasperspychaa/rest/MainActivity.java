package com.example.kasperspychaa.rest;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class MainActivity extends ActionBarActivity {

    String command = "na 10 m";
    String fileName = "file.txt";
    String path = "ls";
    FloatingActionButton fab_done;
    FloatingActionButton fab_mic;
    EditText editText;
    EditText editText1;
    EditText editText2;
    String micResult;
    String URL="192.168.1.1:8080";
    Button button1;
    Button button2;
    int strefa = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Intent recIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        fab_mic = (FloatingActionButton) findViewById(R.id.mic);
        fab_done = (FloatingActionButton) findViewById(R.id.send);
        editText = (EditText) findViewById(R.id.EditCommand);
        editText1 = (EditText) findViewById(R.id.EditFile);
        editText2 = (EditText) findViewById(R.id.EditPath);
        button1 = (Button) findViewById(R.id.button1);

        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new HttpPostTask().execute();
                fileName = editText1.getText().toString();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new HttpPostTask().execute();
                path = editText2.getText().toString();
            }
        });

        fab_done.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new HttpPostTask().execute();
                command = editText.getText().toString();
            }
        });

        fab_mic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                recIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                try {
                    startActivityForResult(recIntent, 1);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(), "Oops! Your device doesn't support Speech to Text",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editUrl = (EditText) dialogView.findViewById(R.id.EditUrl);

        dialogBuilder.setTitle("SET URL");
        dialogBuilder.setMessage("Enter ip with port below:");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                URL=editUrl.getText().toString();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            showChangeLangDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private class HttpPostTask extends AsyncTask<Void, Void, Greeting> {
        @Override
        protected Greeting doInBackground(Void... params) {
            try {
                final String url = "http://" + URL + "/greeting";

                Greeting greeting = new Greeting();
                greeting.setCommand(command);
                greeting.setFileName(fileName);
                greeting.setPath(path);

                RestTemplate restTemplate = new RestTemplate();

                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

                return restTemplate.postForObject(url, greeting, Greeting.class);
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Greeting greeting) {
            Toast.makeText(MainActivity.this, "sent", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1: {
                if (resultCode == Activity.RESULT_OK && null != data) {
                    micResult = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
                    editText.setText(micResult);
                }
                break;
            }
        }
    }

}