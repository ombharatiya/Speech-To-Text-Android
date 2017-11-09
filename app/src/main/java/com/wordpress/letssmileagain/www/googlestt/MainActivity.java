package com.wordpress.letssmileagain.www.googlestt;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

/***
 * @Copyright OM Bharatiya
 *
 */

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import static android.R.attr.id;
import static android.R.attr.start;
import static com.wordpress.letssmileagain.www.googlestt.R.id.mic;

public class MainActivity extends AppCompatActivity {



    TextView txtView;
    ArrayList<String> result = null;
    ImageView mic, wp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtView  = (TextView) findViewById(R.id.textView);
        mic = (ImageView) findViewById(R.id.mic);
        wp = (ImageView) findViewById(R.id.whatsapp);

        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"Say Something...",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

                if(intent.resolveActivity(getPackageManager())!=null) {
                    startActivityForResult(intent, 5);
                }
                else {
                    Toast.makeText(v.getContext(),"Your Device Doesn't Support Speech Intent", Toast.LENGTH_SHORT).show();
                }
            }
        });

        wp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws NullPointerException {
                if(result!=null) {
                    Toast.makeText(v.getContext(),"To whatsapp...",Toast.LENGTH_SHORT).show();
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    Log.e("JUST FOR TESTING",result.get(0));
                    sendIntent.putExtra(Intent.EXTRA_TEXT, result.get(0));
                    sendIntent.setType("text/plain");
                    sendIntent.setPackage("com.whatsapp");
                    startActivity(sendIntent);
                }
                else{
                    Toast.makeText(v.getContext(),"First Say Something in the Mic!!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==5) {
            if(resultCode==RESULT_OK && data!=null) {
                result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                txtView.setText(result.get(0));
            }
        }
    }
}
