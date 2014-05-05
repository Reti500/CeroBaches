package com.geos.gestor.cerobaches.libs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;


import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class Comunicador {

	public static abstract class ResponseListener {
		public abstract void error(String msg);
		public abstract void success(String val);
		public abstract void onfinal();
	}
	
	private static HttpClient client;
	
	private static final String LOG_TAG = "comunicador";
	
	/**
	 * Sirve para construir las urls
	 * @param url_base
	 * @param url_params
	 * @return
	 */
	
	public final static String build_url(String url_base, String ...url_params) {
		StringBuilder sb = new StringBuilder(url_base);
		for (String s : url_params) {
			sb.append(s);
			sb.append('/');
		}
		String res =sb.substring(0, sb.length()-1);
		Log.i("Build_url", res);
		return res;
	}
	///////MD5
	/*
	 * Crea una cadena md5 a partir de una cadena original
	 */
	public static final String md5String( String s){
		try {
			s = new String(s.getBytes("ascii"),"ascii");
	        // Create MD5 Hash
	        MessageDigest digest = MessageDigest
	                .getInstance("MD5");
	        digest.update(s.getBytes());
	        byte messageDigest[] = digest.digest();

	        // Create Hex String
	        StringBuffer hexString = new StringBuffer();
	        for (int i = 0; i < messageDigest.length; i++) {
	            String h = Integer.toHexString(0xFF & messageDigest[i]);
	            while (h.length() < 2)
	                h = "0" + h;
	            hexString.append(h);
	        }
	        return hexString.toString();

	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    return null;
	}
	public Comunicador() {
		if (client == null)  {
			client = new DefaultHttpClient();
		}
//		HttpParams httpparams = client.getParams();
        //httpparams.setParameter(CoreProtocolPNames.USER_AGENT, "android");
	} 
	//////////////////////////////Peticiones
	/*
	 * Petici��n GET con cabeceras
	 */
	public final synchronized String executeHttpGet (
			String URL, 
			HashMap<String, String> headersMap) throws IOException {
		return executeHttpGet(URL, headersMap, null);
	}
	
	/*
	 * Petici��n GET con cabeceras y parametros
	 */
	public final synchronized String executeHttpGet (
			String URL, 
			HashMap<String, String> headersMap,
			HashMap<String, String> params) 
			throws IOException {
        StringBuffer sbURL = new StringBuffer(URL);
        if(params != null){
        	Set<Entry<String, String>> entrySet = params.entrySet();
            if (entrySet.size() > 0) {
            	sbURL.append('?');
    	        for (Entry<String, String> key : entrySet) {
    	        	sbURL.append(key.getKey());
    	        	sbURL.append('=');
    	        	sbURL.append(URLEncoder.encode(key.getValue(), "UTF-8"));
    	        	sbURL.append('&');
    	        	//URL += key.getKey()+"="+key.getValue()+"&";
    	        }
    	        URL = sbURL.substring(0, sbURL.length()-1);
            }
        }
        
        Log.d(LOG_TAG, "GET "+URL);
        StringBuffer sb = new StringBuffer();
        HttpGet request = new HttpGet(URL);
        //request.setHeader("Content-Type", "text/plain; charset=utf-8");
        if (headersMap != null){
		    Set<Entry<String, String>> entrySet = headersMap.entrySet();
		    for (Entry<String, String> key : entrySet) {
	        	request.setHeader(key.getKey(), key.getValue());
	        	Log.i(LOG_TAG,"header: "+key.getKey()+" : "+key.getValue());
	        }
	    }
        HttpResponse response = client.execute(request);
        BufferedReader in = 
        		new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String line = "";
        String NL = System.getProperty("line.separator");
        while ((line = in.readLine()) != null) {
            sb.append(line);
            sb.append(NL);
        }
        in.close();
        return sb.toString();
	}
	
	/*
	 * Petici��n POST con cabeceras
	 */
	public final synchronized String executeHttpPost(
			String URL, 
			HashMap<String, String> headersMap) 
			throws ClientProtocolException, IOException {
		return executeHttpPost(URL, headersMap, null);
	}
	/*
	 * Petici��n POST con cabeceras y parametros
	 */
	public final synchronized String executeHttpPost(
			String URL, 
			HashMap<String, String> headersMap,
			List<NameValuePair> paramsList) 
			throws ClientProtocolException, IOException {
	    // Create a new HttpClient and Post Header
	    HttpPost httppost = new HttpPost(URL);
	    if (headersMap != null) {
		    Set<Entry<String, String>> entrySet = headersMap.entrySet();
		    for (Entry<String, String> key : entrySet) {
	        	httppost.addHeader(key.getKey(), key.getValue());
	        	Log.i(LOG_TAG,key.getKey()+ " : " +key.getValue());
	        }
	    }
	    
	    if (paramsList != null) {
	    	/*Log.i(TAG,""+paramsList.get(0));
		    Log.i(TAG,""+paramsList.get(1));
		    Log.i(TAG,""+paramsList.get(2));
		    Log.i(TAG,""+paramsList.get(3));*/
	    	httppost.setEntity(new UrlEncodedFormEntity(paramsList,HTTP.UTF_8));
	    }
        // Execute HTTP Post Request
        HttpResponse response = client.execute(httppost);
        Log.i(LOG_TAG,""+response.getStatusLine().getStatusCode());

        BufferedReader in = 
        		new BufferedReader(
        				new InputStreamReader(response.getEntity().getContent()));
        String line = "";

        String NL = System.getProperty("line.separator");

	    StringBuffer sb = new StringBuffer();
        while ((line = in.readLine()) != null) {
            sb.append(line);
            sb.append(NL);
        }
        in.close();
        Log.i(LOG_TAG,"respuesta"+sb.toString());
	    return sb.toString();
	}
	
	public final void get(final String URL, 
			final HashMap<String, String> headersMap,
			final HashMap<String, String> params, 
			final ResponseListener response){
		
		new AsyncTask<Void, Void, String>() {
			
			Exception error=null;
			
			@Override
			protected String doInBackground(Void... p) {
				String result=null;
				try {
					result = executeHttpGet(URL, headersMap, params);
				} catch (ClientProtocolException e) {
					error = e;
					e.printStackTrace();
				} catch (IOException e) {
					error = e;
					e.printStackTrace();
				} 
				return result;
			}
			
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				Log.i(LOG_TAG,"response: "+result);
				if (response != null) {
					if (error == null){
						response.success(result);
						response.onfinal();
					}
					else {
						response.error(error.getMessage());
						response.onfinal();
					}
				}
			}
			
		}.execute((Void) null);
	}
	
	public final void post(final String URL, 
			final HashMap<String, String> headersMap,
			final List<NameValuePair> paramsList, 
			final ResponseListener response){	
		new AsyncTask<Void, Void, String>() {
			Exception error = null;
			@Override
			protected String doInBackground(Void... p) {
				String result = null;
				try {
					result = executeHttpPost(URL, headersMap, paramsList);
				} catch (ClientProtocolException e) {
					error = e;
					e.printStackTrace();
				} catch (IOException e) {
					error = e;
					e.printStackTrace();
				}
				return result;
			}
			
			@Override
			protected void onPostExecute(String result) {
				if (response != null) {
					if (error == null){
						response.success(result);
						response.onfinal();
					}
					else {
						response.error(error.toString());
						response.onfinal();
					}
				}
				Log.i(LOG_TAG,"response: "+result);
			}
			
		}.execute((Void) null);
	}
	
	
	
	public static final void showMessage(Context c, int id) throws NotFoundException {
		String er_message;
		er_message = c.getResources().getString(id);
		Toast.makeText(c, er_message, Toast.LENGTH_LONG).show();
	}
	
}
