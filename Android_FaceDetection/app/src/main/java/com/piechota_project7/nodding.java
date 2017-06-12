package com.piechota_project7;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class nodding extends Activity {
	private Camera varCam;
	private com.piechota_project7.cameraActivity varPreview;
	private Button capture;
	private ImageButton	switchCam;
	private Context myContext;
	private LinearLayout camPreview;
	private TextView nod;

	private boolean cameraFront = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		// Get Permissions
		ActivityCompat.requestPermissions(this,
				new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
						android.Manifest.permission.CAMERA},
				0);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		myContext = this;
		initialize();
	}

	private int frontCam() {
		int cameraId = -1;
		// Find Front Facing Camera
		int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; i++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
				cameraId = i;
				cameraFront = true;
				break;
			}
		}
		return cameraId;
	}
	// Find Back Camera
	private int backCam() {
		int cameraId = -1;

		// Return # of Cameras
		int numberOfCameras = Camera.getNumberOfCameras();

		for (int i = 0; i < numberOfCameras; i++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
				cameraId = i;
				cameraFront = false;
				break;
			}
		}
		return cameraId;
	}

	public void onResume() {
		super.onResume();

		if (!hasCamera(myContext)) {
			Toast toast = Toast.makeText(myContext, "Sorry, you don't have a camera!", Toast.LENGTH_LONG);
			toast.show();
			finish();
		}
		//Check for Front Facing Camera
		if (varCam == null) {
			if (frontCam() < 0) {
				Toast.makeText(this, "No front facing camera present.", Toast.LENGTH_LONG).show();
				switchCam.setVisibility(View.GONE);
			}			
			varCam = Camera.open(backCam());

			varPreview.refreshCamera(varCam);
		}
	}

	public void initialize() {
		camPreview = (LinearLayout) findViewById(R.id.camera_preview);

		varPreview = new com.piechota_project7.cameraActivity(myContext, varCam, this);
		camPreview.addView(varPreview);

		nod = (TextView) findViewById(R.id.textView);

		switchCam = (ImageButton) findViewById(R.id.button_ChangeCamera);
		switchCam.setOnClickListener(switchCamListener);
	}

	// Check for Front and Back Facing Cameras AKA 2
	OnClickListener switchCamListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int camerasNumber = Camera.getNumberOfCameras();
			if (camerasNumber > 1) {
				releaseCam();
				whichCam();
			}
			else {
				Toast toast = Toast.makeText(myContext, "Sorry, you only have one camera!", Toast.LENGTH_LONG);
				toast.show();
			}
		}
	};

	// Change Camera Orientation based of Camera ID
	public void whichCam() {
		//if the camera preview is the front
		if (cameraFront) {
			int cameraId = backCam();
			if (cameraId >= 0) {

				varCam = Camera.open(cameraId);				
				//mPicture = getPictureCallback();
				varPreview.refreshCamera(varCam);
			}
		} else {
			int cameraId = frontCam();
			if (cameraId >= 0) {

				varCam = Camera.open(cameraId);
				//mPicture = getPictureCallback();
				varPreview.refreshCamera(varCam);
			}
		}
	}

	// stop camera when paused
	@Override
	protected void onPause() {
		super.onPause();
		releaseCam();
	}

	//check for camera
	private boolean hasCamera(Context context) {

		if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			return true;
		} else {
			return false;
		}
	}

	// stop camera
	private void releaseCam() {

		if (varCam != null) {
			varCam.release();
			varCam = null;
		}
	}
}