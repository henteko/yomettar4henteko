package com.henteko07.yometter4henteko;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Yomettar4hentekoActivity extends Activity {
	
	private Twitter twitter = null;
	private RequestToken requestToken = null;
	
	private String CONSUMER_KEY = "自分の";
	private String CONSUMER_SECRET = "自分の";
	
	private String nechatterStatus;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        //まぁ一応書いときます
	    SharedPreferences pref = getSharedPreferences("Twitter_seting",MODE_PRIVATE);
	    //これは必要だから消さないでね
		nechatterStatus  = pref.getString("status", "");
        
		
		Button loginbutton = (Button) findViewById(R.id.tweetLogin);
        loginbutton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) { 
        		
        		//もう設定されているか？
        		if(isConnected(nechatterStatus)){
        			
        			//disconnectTwitter();
					
        			Intent intent2 = new Intent(Yomettar4hentekoActivity.this, Tweet.class);
            		startActivityForResult(intent2, 0);
					
        		}else{
        			
					showToast("ログインしてね!!");
        		}
        		
        		
        	}
        });
        
        Button tlbutton = (Button) findViewById(R.id.tl);
        tlbutton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) { 
        		
        		//もう設定されているか？
        		if(isConnected(nechatterStatus)){
        			Intent intent2 = new Intent(Yomettar4hentekoActivity.this, TLActivity.class);
            		startActivityForResult(intent2, 0);
        		}else{
					showToast("ログインしてね！！");
        		}
			}
        });
        
        
        
        Button loginlogoutbutton = (Button) findViewById(R.id.loginlogout);
        loginlogoutbutton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View view) { 
        		disconnectTwitter();
				try {
					connectTwitter();
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
        	
        });
        
        
    }
    
    
    
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

		if(resultCode == RESULT_OK){
			super.onActivityResult(requestCode, resultCode, intent);

			AccessToken accessToken = null;

			try {
				accessToken = twitter.getOAuthAccessToken(
						requestToken,
						intent.getExtras().getString("oauth_verifier"));

				
		        SharedPreferences pref=getSharedPreferences("Twitter_seting",MODE_PRIVATE);

		        SharedPreferences.Editor editor=pref.edit();
		        editor.putString("oauth_token",accessToken.getToken());
		        editor.putString("oauth_token_secret",accessToken.getTokenSecret());
		        editor.putString("status","available");

		        editor.commit();
		        
		        //finish();
			} catch (TwitterException e) {
				showToast("エラーだよ!!");
			}
		}
	}
    
    
    
    
    private void connectTwitter() throws TwitterException{
		//参考:http://groups.google.com/group/twitter4j/browse_thread/thread/d18c179ba0d85351
		//英語だけど読んでね！
		ConfigurationBuilder confbuilder  = new ConfigurationBuilder(); 

		confbuilder.setOAuthConsumerKey(CONSUMER_KEY).setOAuthConsumerSecret(CONSUMER_SECRET); 

		twitter = new TwitterFactory(confbuilder.build()).getInstance();
		
		
		String CALLBACK_URL = "myapp://oauth";
		// requestTokenもクラス変数。
		try {
			requestToken = twitter.getOAuthRequestToken(CALLBACK_URL);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		  // 認証用URLをインテントにセット。
		  // TwitterLoginはActivityのクラス名。
		  Intent intent = new Intent(this, TwitterLogin.class);
		  intent.putExtra("auth_url", requestToken.getAuthorizationURL());

		  // アクティビティを起動
		  this.startActivityForResult(intent, 0);
		
	}
    
    
    
    final private boolean isConnected(String nechatterStatus){
		if(nechatterStatus != null && nechatterStatus.equals("available")){
			return true;
		}else{
			return false;
		}
	}
    
    
    public void disconnectTwitter(){
        SharedPreferences pref=getSharedPreferences("Twitter_seting",MODE_PRIVATE);

        SharedPreferences.Editor editor=pref.edit();
        editor.remove("oauth_token");
        editor.remove("oauth_token_secret");
        editor.remove("status");
        
        nechatterStatus = null;

        editor.commit();
        //finish();
	}
    
    public void showToast(String string) {
		// TODO Auto-generated method stub
    	Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
	}



}