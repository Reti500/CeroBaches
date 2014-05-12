package com.geos.gestor.cerobaches.libs;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParseJson {
	
	public ParseJson(){
		super();
	}
	
	public HashMap<String, String> parse(String string_json, String[] vals){
		HashMap<String, String> json_parsed = new HashMap<String, String>();
		
		try{
			JSONObject json = new JSONObject(string_json);
			
			for(int i=0; i<vals.length; i++){
				String obj = json.isNull(vals[i]) != true ? json.getString(vals[i]) : null;
				json_parsed.put(vals[i], obj);
			}
			
		} catch (JSONException e){
			e.printStackTrace();
		}
		
		return json_parsed;
	}
	
	public HashMap<String, String> parseArray(JSONObject json, String head, String[] vals){
		HashMap<String, String> json_parsed = new HashMap<String, String>();
		
		try{
			JSONArray array = json.getJSONArray(head);
			
			for(int i=0; i<array.length(); i++){
				JSONObject ithem = array.getJSONObject(i);
				String obj = ithem.isNull(vals[i]) != true ? json.getString(vals[i]) : null;
				json_parsed.put(vals[i], obj);
			}
			
		} catch (JSONException e){
			e.printStackTrace();
		}
		
		return json_parsed;
	}
}
