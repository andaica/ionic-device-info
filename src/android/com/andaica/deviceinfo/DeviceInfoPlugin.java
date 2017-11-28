package com.andaica.deviceinfo;

import android.hardware.Camera;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DeviceInfoPlugin extends CordovaPlugin {
	public int NO_CAMERA = -101;
	private int mCameraId = NO_CAMERA;
	private Camera mCamera = null;
	JSONObject cameraInfo;

	@Override
	public boolean execute(String action, JSONArray args,
						   CallbackContext callbackContext) throws JSONException {
		if (action.equals("getDeviceInfo")) {
			JSONObject r = new JSONObject();
			getCameraInfo();
			r.put("version", android.os.Build.VERSION.RELEASE);
			r.put("model", android.os.Build.MODEL);
			r.put("product", android.os.Build.PRODUCT);
			r.put("manufacturer", android.os.Build.MANUFACTURER);
			r.put("serial", android.os.Build.SERIAL);
			r.put("cameraInfo", cameraInfo);
			callbackContext.success(r);

			return true;
		} else {
			callbackContext.error("Invalid action");
			return false;
		}
	}

	public void getCameraInfo() {
		cameraInfo = new JSONObject();
		try {
			mCameraId = getCameraId(Camera.CameraInfo.CAMERA_FACING_FRONT);
			if (mCameraId != NO_CAMERA) {
				mCamera = Camera.open(mCameraId);
				// Set camera parameters
				Camera.Parameters cp = mCamera.getParameters();
				cameraInfo.put("supportVideoStabilization", cp.isVideoStabilizationSupported());
			}
		} catch (RuntimeException ex) {
			this.releaseCamera();
			Log.e("cameraInfo", "Unable to open camera. Another application probably has a lock", ex);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public int getCameraId(int position) throws JSONException {
		// Find the total number of cameras available
		int mNumberOfCameras = Camera.getNumberOfCameras();
		this.cameraInfo.put("cameraNumber", mNumberOfCameras);
		int cameraId = NO_CAMERA;
		// Find the ID of the back-facing ("default") camera
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		for (int i = 0; i < mNumberOfCameras; i++) {
			Camera.getCameraInfo(i, cameraInfo);
			if(cameraInfo.facing == 0)
				this.cameraInfo.put("cam"+i+"facing", "back");
			else
				this.cameraInfo.put("cam"+i+"facing", "front");

			if (cameraInfo.facing == position)
				cameraId = i;
		}

		return cameraId;
	}

	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();
			mCamera.lock();
			mCamera.release();
			mCamera = null;
			mCameraId = NO_CAMERA;
		}
	}
}
