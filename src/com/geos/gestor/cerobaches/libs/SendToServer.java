package com.geos.gestor.cerobaches.libs;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class SendToServer {
	private static final String LOG_TAG_GET = "SEND GET";
	private static final String LOG_TAG_GET_ERROR = "SEND GET ERROR";
	private static final String LOG_TAG_POST = "SEND POST";
	private static final String LOG_TAG_POST_ERROR = "SEND POST ERROR";
	
	private static final String BASE_URL = "http://rest.bs.geos-it.mx/";
	public static final String LOGIN_URL = BASE_URL + "Account/LogOn";
	public static final String LOGOUT_URL = BASE_URL + "Account/LogOff";
	public static final String SOLICITUDES_URL = BASE_URL + "Solicitud";
	public static final String NUEVA_SOLICITUD_URL = BASE_URL + "Solicitud/Create";
	
	
	private static SendToServer instance;
	
	private HttpClient client;
	
	public SendToServer() {
		super();
		
		client = new DefaultHttpClient();
	}
	
	public static final SendToServer getInstance(){
		if(instance == null){
			instance = new SendToServer();
		}
		
		return instance;
	}
	
	public ArrayList<SendParams> createParams(SendParams... params){
		ArrayList<SendParams> listParams = new ArrayList<SendParams>();
		
		if(params != null){
			for(int i=0; i<params.length; i++){
				listParams.add(params[i]);
			}
		}
		
		return listParams;
	}
	
	public void sendByPost(Context context, ArrayList<SendParams> params, String url, SendToServerListener listener){
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		
		for(int i=0; i<params.size(); i++){
			SendParams p = params.get(i);
			
			if(p.type == 0){
				builder.addTextBody(p.name_param, p.param);
			}else if(p.type == 1){
				builder.addPart(p.name_param,  new FileBody(p.file));
			}
		}
		 
        HttpEntity entity = builder.build();
        
        Log.i("SERVER", url);
        new SendPostTask(context, listener, new HttpPost(url)).execute(entity);
	}
	
	public void sendByGet(Context context, ArrayList<SendParams> params, String url, SendToServerListener listener){
		String new_url = createGetUrl(url, params);
		HttpGet get = new HttpGet(new_url);
		Log.i("URLBASE", new_url);
        new SendGetTask(context, listener, get).execute();
	}
	
	public String createGetUrl(String url, ArrayList<SendParams> params){
		if(!url.endsWith("?"))
	        url += "?";

	    List<NameValuePair> new_params = new LinkedList<NameValuePair>();

	    if(params != null){
			for(int i=0; i<params.size(); i++){
				SendParams p = params.get(i);
				new_params.add(new BasicNameValuePair(p.name_param, p.param));
			}
		}

	    String paramString = URLEncodedUtils.format(new_params, "utf-8");
	    
	    return url += paramString;
	}
	
	public class SendPostTask extends AsyncTask<HttpEntity, Void, String>{
		
		private SendToServerListener listener;
		private HttpPost httppost;
		private Context context;
		private ProgressDialog dialog;
		
		public SendPostTask(Context context, SendToServerListener listener, HttpPost httppost){
			this.listener = listener;
			this.httppost = httppost;
			this.context = context;
		}
		
		@Override
		protected void onPreExecute(){
			if(context != null){
				dialog = ProgressDialog.show(context, "Please wait ...", "Enviando datos...", true);
			}
		}

		@Override
		protected String doInBackground(HttpEntity... params) {
			// TODO Auto-generated method stub
			String result = null;
			HttpResponse response = null;
			
			try{
				httppost.setEntity(params[0]);
		        response = client.execute(httppost);
		        result = parseResponce(response);
		        
		        Log.i(LOG_TAG_POST,""+response.getStatusLine().getStatusCode());
		        Log.i(LOG_TAG_POST,""+result);
			}catch(ClientProtocolException e){
				Log.d(LOG_TAG_POST_ERROR, e.getMessage(),e);
			}catch (IOException e) {
	            Log.d(LOG_TAG_POST_ERROR, e.getMessage(),e);
	        }
			
			return result;
		}
		
		@Override
		protected void onPostExecute(String result){
			if(result != null){
				listener.success(result);
			}else{
				listener.error(result);
			}
			
			if(dialog != null)
				dialog.dismiss();
		}
	}
	
	public class SendGetTask extends AsyncTask<Void, Void, String>{
		
		private SendToServerListener listener;
		private HttpGet httpget;
		private Context context;
		private ProgressDialog dialog;
		
		public SendGetTask(Context context, SendToServerListener listener, HttpGet httpget){
			this.listener = listener;
			this.httpget = httpget;
			this.context = context;
		}
		
		@Override
		protected void onPreExecute(){
			if(context != null){
				dialog = ProgressDialog.show(context, "Please wait ...", "Enviando datos ...", true);
			}
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String result = null;
			HttpResponse response = null;
			
			try{
		        response = client.execute(httpget);
		        result = parseResponce(response);
		        
		        Log.i(LOG_TAG_GET,""+response.getStatusLine().getStatusCode());
		        Log.i(LOG_TAG_GET,""+result);
			}catch(ClientProtocolException e){
				Log.d(LOG_TAG_GET_ERROR, e.getMessage(),e);
			}catch (IOException e) {
	            Log.d(LOG_TAG_GET_ERROR, e.getMessage(),e);
	        }
			
			return result;
		}
		
		@Override
		protected void onPostExecute(String result){
			if(result != null){
				listener.success(result);
			}else{
				listener.error(result);
			}
			
			if(dialog != null)
				dialog.dismiss();
		}
	}
	
	public String parseResponce(HttpResponse response){
		StringBuffer sb = new StringBuffer();
		
		try{
			BufferedReader in = new BufferedReader(
		        		new InputStreamReader(response.getEntity().getContent()));
	        String line = "";
	        String NL = System.getProperty("line.separator");
	        while ((line = in.readLine()) != null) {
	            sb.append(line);
	            sb.append(NL);
	        }
	        in.close();
		}catch(ClientProtocolException e){
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return sb.toString();
	}
	
	public static class SendParams {
		public int type;
		public String name_param;
		public String param;
		public File file;
		
		public SendParams(String name_param, String param){
			this.name_param = name_param;
			this.param = param;
			this.type = 0;
		}
		
		public SendParams(String name_param, File param){
			this.name_param = name_param;
			this.file = param;
			this.type = 1;
		}
	}
	
	public static abstract class SendToServerListener {
		public abstract void error(String msg);
		public abstract void success(String val);
		public abstract void onfinal();
	}
}
