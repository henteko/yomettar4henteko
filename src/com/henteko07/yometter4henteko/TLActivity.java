package com.henteko07.yometter4henteko;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;


public class TLActivity extends ListActivity{
	
	private String CONSUMER_KEY = "自分の";
	private String CONSUMER_SECRET = "自分の";
	
	private Twitter TWITTER;
	private ResponseList<Status> homeTl = null;
	
	
	protected void onCreate(Bundle bundle) {

		super.onCreate(bundle);
        setContentView(R.layout.tl);
        
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

     	TWITTER = new TwitterFactory(confbuilder.build()).getInstance(); 
        
     	
        try {
			homeTl = get_tl();
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);
			 
		    for (Status status : homeTl) {
		        //つぶやきのユーザーIDの取得
		        String userName = status.getUser().getScreenName();
		        //つぶやきの取得
		        String tweet = status.getText();
		        adapter.add("ユーザーID：" + userName + "\r\n" + "tweet：" + tweet);
		    }
		    
		    //参考:http://techbooster.jpn.org/andriod/ui/636/
		    //参考:画像も表示する方法 http://techbooster.jpn.org/andriod/ui/1282/
		    setListAdapter(adapter);
		    
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        
	}
	
	
	public ResponseList<Status> get_tl() throws TwitterException {

		//参照:http://techbooster.jpn.org/andriod/mashup/5907/
		try {
	        //TLの取得
	        ResponseList<Status> Tl = TWITTER.getHomeTimeline();
	        return Tl;
		} catch (TwitterException e) {
			e.printStackTrace();
			if(e.isCausedByNetworkIssue()){
	        Toast.makeText(getApplicationContext(), "ネットワークに接続して下さい", Toast.LENGTH_LONG);
			}else{
				Toast.makeText(getApplicationContext(), "エラーが発生しました。", Toast.LENGTH_LONG);
			}
		}
		return null;
		
	}
	
}
