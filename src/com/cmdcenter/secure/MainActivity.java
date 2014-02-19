package com.cmdcenter.secure;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	ConnectGetID connect1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final EditText et1 = (EditText)findViewById(R.id.editText1);
		final EditText et2 = (EditText)findViewById(R.id.editText2);
		final Button connect = (Button)findViewById(R.id.button1);
		connect.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String user = et1.getText().toString();
				String password = et2.getText().toString();
				ArrayList<String> list= new ArrayList<String>();
				list.add(0,user);
				list.add(1,password);
				connect1=new ConnectGetID();
				connect1.execute(list);
			}
		});
	}
public class ConnectGetID extends AsyncTask<ArrayList<String>, Void, String>
{
	public String user;
	protected String doInBackground(ArrayList<String>...para ) {
		String res;
		final ArrayList<String> details =para[0];
		user=details.get(0);
		String url = "http://s3cur3command.appspot.com/ClientAuthentication/"+details.get(0)+"/"+details.get(1);
		try 
		{
			
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
			HttpResponse response = httpClient.execute(get);
			res = EntityUtils.toString((response.getEntity()));
		}
		catch(ClientProtocolException e)
		{
			res="error";
		}
		catch(IOException e)
		{
			res="error";
		}
        
        return res;
    }


    protected void onPostExecute(String result) {
        if(result.contains("AuthenticationError"))
        {
        	Toast.makeText(getApplicationContext(),"Connection Failure",Toast.LENGTH_LONG).show();
        }
        else
        {
        	 Intent intent = new Intent(getApplicationContext(),ExecCmd.class );
        	 intent.putExtra("sysid", Integer.parseInt(result));
        	 intent.putExtra("user",user);
        	 startActivity(intent);
        }
    }
}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
