package com.nullcognition.appengineandgcmexamples;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hugo.weaving.DebugLog;

/**
 * Created by ersin on 13/03/15 at 12:50 AM
 */


class ServletPostAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {

   private Context context;
   public boolean isLocal = false;

   @Override
   @DebugLog
   protected String doInBackground(Pair<Context, String>... params){
	  context = params[0].first;
	  String name = params[0].second;

	  HttpClient httpClient = new DefaultHttpClient();
	  HttpPost httpPost;

	  if(isLocal){
		 httpPost = new HttpPost("http://10.0.2.2:8080/hello"); // 10.0.2.2 is localhost's IP address in Android emulator
	  }
	  else{httpPost = new HttpPost("http://android-app-backend.appspot.com/hello");} // TODO - change this to an actual project instance name

	  try{
		 // Add name data to request
		 List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		 nameValuePairs.add(new BasicNameValuePair("name", name));
		 httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		 // Execute HTTP Post Request
		 HttpResponse response = httpClient.execute(httpPost);
		 if(response.getStatusLine()
					.getStatusCode() == 200){
			return EntityUtils.toString(response.getEntity());
		 }
		 return "Error: " + response.getStatusLine()
									.getStatusCode() + " " + response.getStatusLine()
																	 .getReasonPhrase();

	  }
	  catch(ClientProtocolException e){
		 return e.getMessage();
	  }
	  catch(IOException e){
		 return e.getMessage();
	  }
   }

   @Override
   protected void onPostExecute(String result){
	  Toast.makeText(context, result, Toast.LENGTH_LONG)
		   .show();
   }
}
