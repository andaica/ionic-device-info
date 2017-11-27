package com.andaica.deviceinfo;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DeviceInfoPlugin extends CordovaPlugin {
	
	@Override
	public boolean execute(String action, JSONArray args, 
			CallbackContext callbackContext) throws JSONException {
		if (action.equals("getDeviceInfo")) {
			JSONObject r = new JSONObject();
			r.put("version", android.os.Build.VERSION.RELEASE);
			r.put("model", android.os.Build.MODEL);
			r.put("manufacturer", android.os.Build.MANUFACTURER);
			r.put("serial", android.os.Build.SERIAL);
			callbackContext.success(r);
			
			return true;
		} else {
			callbackContext.error("Invalid action");
			return false;
		}
		
	}

}
