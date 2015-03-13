package com.nullcognition.appengineandgcmexamples;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class ActivityMain extends ActionBarActivity {

   @InjectView(R.id.postEditText)
   EditText postEditText;

   @Override
   protected void onCreate(Bundle savedInstanceState){
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_main);
	  ButterKnife.inject(this);
   }


   @Override
   public boolean onCreateOptionsMenu(Menu menu){
	  // Inflate the menu; this adds items to the action bar if it is present.
	  getMenuInflater().inflate(R.menu.activity_main_menu, menu);
	  return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item){
	  // Handle action bar item clicks here. The action bar will
	  // automatically handle clicks on the Home/Up button, so long
	  // as you specify a parent activity in AndroidManifest.xml.
	  int id = item.getItemId();

	  //noinspection SimplifiableIfStatement
	  if(id == R.id.action_servlet){
		 new ServletPostAsyncTask().execute(new Pair<Context, String>(this, postEditText.getText()
																						.toString()));
		 return true;
	  }
	  else if(id == 0){}

	  return super.onOptionsItemSelected(item);
   }
}
