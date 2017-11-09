package com.wordpress.letssmileagain.www.googlestt;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.R.attr.data;
import static android.R.id.message;

/***
 * @Copyright OM Bharatiya
 *
 */


public class MainActivity extends AppCompatActivity {



    TextView txtView;
    ArrayList<String> result = null;
    ImageView mic, wp, twtr, more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtView  = (TextView) findViewById(R.id.textView);
        mic = (ImageView) findViewById(R.id.mic);
        wp = (ImageView) findViewById(R.id.whatsapp);
        twtr = (ImageView) findViewById(R.id.twitter);
        more = (ImageView) findViewById(R.id.more);

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
//                    Log.e("JUST FOR TESTING",result.get(0));
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

        twtr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(result!=null) {
                    Intent tweetIntent = new Intent(Intent.ACTION_SEND);
                    tweetIntent.putExtra(Intent.EXTRA_TEXT, result.get(0));
                    tweetIntent.setType("text/plain");

                    PackageManager packManager = getPackageManager();
                    List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

                    boolean resolved = false;
                    for (ResolveInfo resolveInfo : resolvedInfoList) {
                        if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")) {
                            tweetIntent.setClassName(
                                    resolveInfo.activityInfo.packageName,
                                    resolveInfo.activityInfo.name);
                            resolved = true;
                            break;
                        }
                    }
                    if (resolved) {
                        startActivity(tweetIntent);
                    } else {
                        Intent i = new Intent();
                        i.putExtra(Intent.EXTRA_TEXT, result.get(0));
                        i.setAction(Intent.ACTION_VIEW);
                        i.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + urlEncode(result.get(0))));
                        startActivity(i);
                        Toast.makeText(v.getContext(), "Twitter app isn't found", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(v.getContext(),"First Say Something in the Mic!!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(result!=null) {
                    Toast.makeText(v.getContext(),"Choose an app...",Toast.LENGTH_SHORT).show();
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, result.get(0).substring(0, 10));
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, result.get(0));
                    startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));

                }
                else {
                Toast.makeText(v.getContext(),"First Say Something in the Mic!!",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.v("UTF ENCODING EXCEPTION", "UTF-8 should always be supported", e);
            return "";
        }
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
