package com.piechota_project7;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import java.io.IOException;
import static com.piechota_project7.R.id.textView;

public class cameraActivity extends SurfaceView implements SurfaceHolder.Callback {
	private SurfaceHolder holder;
	private Camera Cam;
	private Activity a;
	private TextView text;

	public cameraActivity(Context context, Camera camera, Activity a) {
		super(context);
		this.a = a;
		text = (TextView)this.a.findViewById(textView);
		Cam = camera;
		holder = getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		try {
			if (Cam == null) {
				Cam.setPreviewDisplay(holder);
				Cam.startPreview();
				startFaceDetection();
			}
		} catch (IOException e) {}
	}

	public void refreshCamera(Camera camera) {
		if (holder.getSurface() == null) {
			return;
		}
		try {
			Cam.stopPreview();
		} catch (Exception e) {}
		setCamera(camera);
		try {
			Cam.setPreviewDisplay(holder);
			Cam.startPreview();
			startFaceDetection();
		} catch (Exception e) {}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		refreshCamera(Cam);
	}

	public void setCamera(Camera camera) {
		Cam = camera;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Cam.release();

	}

	public void startFaceDetection(){
		Camera.Parameters params = Cam.getParameters();

		int faceMax = 0;
		faceMax = params.getMaxNumDetectedFaces();

		if (faceMax > 0){
			FaceDetectionListener fDListener = new FaceDetectionListener();
			Cam.setFaceDetectionListener(fDListener);
			Cam.startFaceDetection();
		}
	}

	class FaceDetectionListener implements Camera.FaceDetectionListener {
		int x,y, count = 0;


		@Override
		public void onFaceDetection(Camera.Face[] faceFound, Camera camera) {

			if(faceFound.length > 0){
				if(count == 0){
					x = faceFound[0].rect.centerX();
					y = faceFound[0].rect.centerY();
					count++;
				}

				if(Math.abs(y - faceFound[0].rect.centerY()) >= 125){
					text.setText("Nod Value: 'No' ");

					x = faceFound[0].rect.centerX();
					y = faceFound[0].rect.centerY();
				}
				else if(Math.abs(x - faceFound[0].rect.centerX()) >= 75){
					text.setText("Nod Value: 'Yes' ");

					x = faceFound[0].rect.centerX();
					y = faceFound[0].rect.centerY();
				}
				else{
					x = faceFound[0].rect.centerX();
					y = faceFound[0].rect.centerY();
				}
			}
		}


	}
}


