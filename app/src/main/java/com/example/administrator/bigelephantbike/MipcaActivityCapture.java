package com.example.administrator.bigelephantbike;

import java.io.IOException;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import camera.CameraManager;
import decoding.CaptureActivityHandler;
import decoding.InactivityTimer;
import view.ViewfinderView;

/**
 * Initial the camera
 * @author YunXiang.Tang
 */
public class MipcaActivityCapture extends Activity implements Callback {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private Button lightBtn;

	private boolean isValidatePhone =false;
	private boolean isValidateId = false;
	// 实例化AlertDailog.Builder对象
	//private AlertDialog.Builder builder = new AlertDialog.Builder(MipcaActivityCapture.this);

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d("className",MipcaActivityCapture.this.toString());
		String name =getIntent().getStringExtra("name");
		Log.d("className",name);
		if(name.equals("phone")){
			isValidatePhone =true;
		}else if(name.equals("id")){
			isValidatePhone=true;
			isValidateId =true;
		}

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_capture);
		Log.d("hh","扫描Create");
		//ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

		lightBtn =(Button)findViewById(R.id.light_btn);
		lightBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CameraManager.get().flashHandler();
			}
		});
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}
	
	/**
	 * ����ɨ����
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		if (resultString.equals("")) {
			Toast.makeText(MipcaActivityCapture.this, "Scan failed!", Toast.LENGTH_SHORT).show();
		}else {
			//当用户没有验证手机的时候，跳转到验证手机的页面
			// 设置显示信息
			if(!isValidatePhone){
				MyAlertDialog myAlertDialog =new MyAlertDialog(MipcaActivityCapture.this,"请验证手机","确定");
				myAlertDialog.show();
				myAlertDialog.setClicklistener(new MyAlertDialog.ClickListenerInterface() {
					@Override
					public void doConfirm() {
						Intent intent = new Intent(MipcaActivityCapture.this, ValidatePhoneActivity.class);
						startActivity(intent);
						MipcaActivityCapture.this.finish();
					}
				});
			}else if(!isValidateId){
				final Dialog myDialog = new AlertDialog.Builder(MipcaActivityCapture.this).create();
				myDialog.show();
				myDialog.getWindow().setContentView(R.layout.validate_id_dialog);
				myDialog.getWindow()
						.findViewById(R.id.validate_id_btn)
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								startActivity(new Intent(MipcaActivityCapture.this,ValidateIdActivity.class));
							}
						});
				}else{
				startActivity(new Intent(MipcaActivityCapture.this,ChargeActivity.class));
			}

			//AlertDialog.Builder builder = new AlertDialog.Builder(MipcaActivityCapture.this);

				/**builder.setMessage("身份认证未通过，解锁失败，请先进行身份认证").setPositiveButton("验证身份",
						new DialogInterface.OnClickListener() {
							// 单击事件
							public void onClick(DialogInterface dialog, int which) {
								Intent intent = new Intent(MipcaActivityCapture.this, ValidatePhoneActivity.class);
								startActivity(intent);
								MipcaActivityCapture.this.finish();
							}
						});
				// 创建对话框
				AlertDialog ad = builder.create();
				// 显示对话框
				ad.show();*/
			}
			Intent resultIntent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("result", resultString);
//			bundle.putParcelable("bitmap", barcode);
			resultIntent.putExtras(bundle);
			this.setResult(RESULT_OK, resultIntent);
			
//			Toast.makeText(MipcaActivityCapture.this, "resultString"+resultString,Toast.LENGTH_LONG).show();
		}
	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

}