package com.cmdcenter.secure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ExecCmd extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.execmd);
		Intent intent = getIntent();
		int sysid = intent.getIntExtra("sysid", 0);
		final String user = intent.getStringExtra("user");
		final EditText et1 = (EditText)findViewById(R.id.editText1);
		final Button exec = (Button)findViewById(R.id.button1);
		exec.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String cmd = et1.getText().toString();
				ArrayList<String> list = new ArrayList<String>();
				list.add(0,user);
				list.add(1,cmd);
				ExecCmdTask ect =new ExecCmdTask(); 
				ect.execute(list);
			}
		});
	}
	public class ExecCmdTask extends AsyncTask<ArrayList<String>, Void, String>
	{
		protected String doInBackground(ArrayList<String>...para ) {
			String res;
			ArrayList<String> details =para[0];
			String url = "http://s3cur3command.appspot.com/CommandAcceptor/"+details.get(0)+"/"+details.get(1);
			try 
			{
				//List<NameValuePair> nvp = new ArrayList<NameValuePair>(2);
				//nvp.add(new BasicNameValuePair("user",details.get(0)));
				//nvp.add(new BasicNameValuePair("cmd",details.get(1)));
				HttpClient httpClient = new DefaultHttpClient();
				//HttpPost httppost = new HttpPost(url);
				HttpGet get = new HttpGet(url);
				HttpResponse response = httpClient.execute(get);
				res = EntityUtils.toString((response.getEntity()));
				Log.d("result","Result:"+res);
				Log.d("user","UserName:"+details.get(0));
			}
			catch(ClientProtocolException e)
			{
				res="error";
				e.printStackTrace();
			}
			catch(IOException e)
			{
				res="error";
				e.printStackTrace();
			}
	        
	        return res;
	    }


	    protected void onPostExecute(String result) {
	        if(result.contains("success"))
	        {
	        	Toast.makeText(getApplicationContext(),"Command Succesfully Sent",Toast.LENGTH_LONG).show();
	        }
	        else
	        {
	        	Toast.makeText(getApplicationContext(),"Command Failure",Toast.LENGTH_LONG).show();
	        }
	    }
	}
}
