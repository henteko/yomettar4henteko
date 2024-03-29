package com.henteko07.yometter4henteko;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Tweet extends Activity{
	
	private String CONSUMER_KEY = "自分の";
	private String CONSUMER_SECRET = "自分の";
	private String defoTEXT = null;
	private String mainTEXT = null;
	
	
	protected void onCreate(Bundle bundle) {

		super.onCreate(bundle);
        setContentView(R.layout.tweet);
        
	    
        
        //ハッシュタグ追加！
        defoTEXT = " #testTwitter ";
        
        final EditText edit = (EditText) findViewById(R.id.editText1);
        edit.setText(defoTEXT);
        edit.setMaxLines(3); 
        edit.setSelection(0);
        
        Button tweetbutton = (Button) findViewById(R.id.tweet);
        tweetbutton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // クリック時の処理
            	SpannableStringBuilder sb = (SpannableStringBuilder)edit.getText();
                mainTEXT = sb.toString();
                
                
                try {
					tweet();
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				finish();
				
				
            }
        });
        
	}
	
	
	public void tweet() throws TwitterException {
		// 記録していた設定ファイルを読み込む。
		SharedPreferences pref = getSharedPreferences("Twitter_seting",MODE_PRIVATE);

		// 設定ファイルからoauth_tokenとoauth_token_secretを取得。
		String oauthToken  = pref.getString("oauth_token", "");
		String oauthTokenSecret = pref.getString("oauth_token_secret", "");

		ConfigurationBuilder confbuilder  = new ConfigurationBuilder(); 

		confbuilder.setOAuthAccessToken(oauthToken) 
		.setOAuthAccessTokenSecret(oauthTokenSecret) 
		.setOAuthConsumerKey(CONSUMER_KEY) 
		.setOAuthConsumerSecret(CONSUMER_SECRET); 

		Twitter twitter = new TwitterFactory(confbuilder.build()).getInstance(); 

		// 任意の文字列を引数にして、ツイート。
		twitter.updateStatus(mainTEXT);
		
		Toast.makeText(this, "つぶやきました", Toast.LENGTH_SHORT).show();
	}
	
}