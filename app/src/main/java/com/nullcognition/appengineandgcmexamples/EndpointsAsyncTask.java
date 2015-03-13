package com.nullcognition.appengineandgcmexamples;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.nullcognition.endpointmodule.myApi.MyApi;

import java.io.IOException;

import hugo.weaving.DebugLog;

/**
 * Created by ersin on 13/03/15 at 1:29 AM
 */


class EndpointsAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {

   private static MyApi myApiService = null;
   private Context context;
   public boolean isLocal = false;

   @Override
   @DebugLog
   protected String doInBackground(Pair<Context, String>... params){
	  if(myApiService == null){  // Only do this once

		 MyApi.Builder builder;

		 if(isLocal){

			builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
			  // options for running against local devappserver
			  // - 10.0.2.2 is localhost's IP address in Android emulator
			  // - turn off compression when running against local devappserver
			  .setRootUrl("http://10.0.2.2:8080/_ah/api/")
			  .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {

				 @Override
				 public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException{
					abstractGoogleClientRequest.setDisableGZipContent(true);
				 }
			  });
		 }
		 else{
			builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null) //
			  .setRootUrl("https://appengine-project-id.appspot.com/_ah/api/"); // TODO - change this to a real project instance id
		 }

		 myApiService = builder.build();
	  }

	  context = params[0].first;
	  String name = params[0].second;

	  try{
		 return myApiService.sayHi(name)
							.execute()
							.getData();
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
