package com.nullcognition.appengineandgcmexamples;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by ersin on 13/03/15 at 3:20 AM
 */

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

   @Override
   public void onReceive(Context context, Intent intent){
	  // Explicitly specify that GcmIntentService will handle the intent.
	  ComponentName comp = new ComponentName(context.getPackageName(), GcmIntentService.class.getName());
	  // Start the service, keeping the device awake while it is launching.
	  startWakefulService(context, (intent.setComponent(comp)));
	  setResultCode(Activity.RESULT_OK);
   }
}
