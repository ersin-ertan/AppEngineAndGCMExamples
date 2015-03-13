package com.nullcognition.appengineandgcmexamples; /**
 * Created by ersin on 13/03/15 at 2:53 AM
 */

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.nullcognition.endpointwithgcm.registration.Registration;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import hugo.weaving.DebugLog;


class GcmRegistrationAsyncTask extends AsyncTask<Void, Void, String> {

   private static Registration regService = null;
   private GoogleCloudMessaging gcm;
   private Context              context;
   public boolean isLocal = false;

   private static final String SENDER_ID = "1234567890"; // TODO: change to your own sender ID to Google Developers Console project number

   public GcmRegistrationAsyncTask(Context context){
	  this.context = context;
   }

   @Override
   @DebugLog
   protected String doInBackground(Void... params){
	  if(regService == null){

		 Registration.Builder builder;

		 if(isLocal){
			builder = new Registration.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
			  // Need setRootUrl and setGoogleClientRequestInitializer only for local testing,
			  // otherwise they can be skipped
			  .setRootUrl("http://10.0.2.2:8080/_ah/api/")
			  .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {

				 @Override
				 public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException{
					abstractGoogleClientRequest.setDisableGZipContent(true);
				 }
			  });
		 }

		 else{
			builder = new Registration.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null) //
			  .setRootUrl("https://app-engine-project/_ah/api/"); // TODO - input app engine project id
		 }
		 // end of optional local run code

		 regService = builder.build();
	  }

	  String msg = "";
	  try{
		 if(gcm == null){
			gcm = GoogleCloudMessaging.getInstance(context);
		 }
		 String regId = gcm.register(SENDER_ID);
		 msg = "Device registered, registration ID=" + regId;

		 // You should send the registration ID to your server over HTTP,
		 // so it can use GCM/HTTP or CCS to send messages to your app.
		 // The request to your server should be authenticated if your app
		 // is using accounts.
		 regService.register(regId)
				   .execute();

	  }
	  catch(IOException ex){
		 ex.printStackTrace();
		 msg = "Error: " + ex.getMessage();
	  }
	  return msg;
   }

   @Override
   protected void onPostExecute(String msg){
	  Toast.makeText(context, msg, Toast.LENGTH_LONG)
		   .show();
	  Logger.getLogger("REGISTRATION")
			.log(Level.INFO, msg);
   }
}
