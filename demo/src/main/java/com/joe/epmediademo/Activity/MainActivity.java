package com.joe.epmediademo.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.joe.epmediademo.R;

import Jni.VideoUitls;
import VideoHandle.EpEditor;
import VideoHandle.OnEditorListener;

public class MainActivity extends AppCompatActivity {


	private String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById(R.id.bt_one).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,EditActivity.class));
			}
		});
		findViewById(R.id.bt_more).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,MergeActivity.class));
			}
		});
		findViewById(R.id.bt_exoplayer).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, ExoplayerSimpleActivity.class));
			}
		});
		findViewById(R.id.bt_mp4mixmp3).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String src = Environment.getExternalStorageDirectory()+ "/xijing/video/fc63b338-cb0e-497c-9f1b-b029219926e9.mp4";
				startMp4MixMp3(src);
			}
		});
		findViewById(R.id.bt_mp4mixmp4).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String src = Environment.getExternalStorageDirectory()+ "/xijing/mp4MixMp3.mp4";
				startMp4MixMp4(src);
			}
		});
		findViewById(R.id.bt_addwater).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String src = Environment.getExternalStorageDirectory()+ "/xijing/mp4MixMp4.mp4";
				startAddWater(src);
			}
		});
		findViewById(R.id.bt_addwater).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String src = Environment.getExternalStorageDirectory()+ "/xijing/mp4MixMp4.mp4";
				startAddWater(src);
			}
		});
		findViewById(R.id.bt_mp3fromMp4).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String src = Environment.getExternalStorageDirectory()+ "/xijing/video/fc63b338-cb0e-497c-9f1b-b029219926e9.mp4";
				startMp3fromMp4(src);
			}
		});
		findViewById(R.id.bt_mp3Silent).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String src = Environment.getExternalStorageDirectory()+ "/xijing/audioFromvideo.mp3";
				startMp3Silent(src);
			}
		});
		findViewById(R.id.bt_mp32WAV).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String src = Environment.getExternalStorageDirectory()+ "/xijing/video/dbd3b486-51ab-45c1-bedf-9cf02ab5f879.mp3";
				startMp32WAV(src);
			}
		});
		findViewById(R.id.bt_wav2Mp3).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String src = Environment.getExternalStorageDirectory()+ "/xijing/dubedit/longMp32WAV.wav";
				startWav2Mp3(src);
			}
		});
		findViewById(R.id.bt_pic2Mp4).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String pic = Environment.getExternalStorageDirectory()+ "/xijing/trim/videoCover.jpg";
				startPicMp32Mp4(pic);
			}
		});
		findViewById(R.id.bt_gif2Mp4).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String pic = Environment.getExternalStorageDirectory()+ "/xijing/a6e983bd-e988-487b-99dd-a4e8fb95c649.gif";
				startGifMp32Mp4(pic);
			}
		});

		if (ActivityCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, permissions, 1234);
		}
	}

	private void startGif2Mp4(String srcPath) {
		String destPath = Environment.getExternalStorageDirectory()+ "/xijing/gif2Mp4.mp4";
		String mp3 = Environment.getExternalStorageDirectory()+ "/xijing/video/dbd3b486-51ab-45c1-bedf-9cf02ab5f879.mp3";
		String str = "-r 25 -loop 1 -i "+srcPath+" -pix_fmt yuv420p -vcodec libx264 -b:v 600k -r:v 25 -preset medium -crf 30 -s 720x576 -vframes 250 "+destPath;
		Log.d("xijingLog", "startGif2Mp4 ="+srcPath);
		runCmd(str, srcPath, destPath);
	}

	private void startGifMp32Mp4(String srcPath) {
		String destPath = Environment.getExternalStorageDirectory()+ "/xijing/gif2Mp4.mp4";
		String mp3 = Environment.getExternalStorageDirectory()+ "/xijing/video/dbd3b486-51ab-45c1-bedf-9cf02ab5f879.mp3";
		String str = "-y -loop 1 -i "+srcPath+" -i "+mp3+" -pix_fmt yuv420p " +
				"-vcodec libx264 -b:v 600k -r:v 25 -preset medium -crf 30 -s 720x576 -vframes 250 -r 25 "+destPath;
		Log.d("xijingLog", "startGifMp32Mp4 ="+srcPath);
		runCmd(str, srcPath, destPath);
	}

	private void startPicMp32Mp4(String srcPath) {
		String destPath = Environment.getExternalStorageDirectory()+ "/xijing/pic2Mp4.mp4";
		String mp3 = Environment.getExternalStorageDirectory()+ "/xijing/video/dbd3b486-51ab-45c1-bedf-9cf02ab5f879.mp3";
		String str = "-y -f image2 -i "+srcPath+" -i "+mp3+" -pix_fmt yuv420p " +
				"-vcodec libx264 -b:v 600k -r:v 25 -preset medium -crf 30 " +
				"-s 854x480 -vframes 250 -r 25 "+destPath;
		Log.d("xijingLog", "startPicMp32Mp4 ="+srcPath);
		runCmd(str, srcPath, destPath);
	}

	private void startWav2Mp3(String srcPath) {
		String destPath = Environment.getExternalStorageDirectory()+ "/xijing/longWav2Mp3.mp3";
		String str = "-y -i "+srcPath+" -vn -ar 24000 -ac 2 -b:a 64k "+destPath;
		Log.d("xijingLog", "startWav2Mp3 ="+str);
		runCmd(str, srcPath, destPath);
	}

	private void startMp32WAV(String srcPath) {
		String destPath = Environment.getExternalStorageDirectory()+ "/xijing/longMp32WAV.wav";
//		String str = "-y -i "+srcPath+" -f wav "+destPath;
		String str = "-y -i "+srcPath+" -acodec pcm_s16le -ac 1 -ar 16000 "+destPath;
		Log.d("xijingLog", "startMp32WAV ="+str);
		runCmd(str, srcPath, destPath);
	}

	private void startMp3Silent(String srcPath) {
		String destPath = Environment.getExternalStorageDirectory()+ "/xijing/audioFirst.mp3";
		String str = "-y -i "+srcPath+" -af volume=enable='between(t,0,9.85)':volume=0,volume=enable='between(t,11.096,11.134)':volume=0,volume=enable='between(t,12.985,13.325)':volume=0,volume=enable='between(t,14.609,15.667)':volume=0,volume=enable='between(t,17.442,17.706)':volume=0,volume=enable='between(t,19.217,19.64)':volume=0,volume=enable='between(t,21.71,21.71)':volume=0,volume=enable='between(t,23.524,23.675)':volume=0,volume=enable='between(t,25.05,26.885)':volume=0,volume=enable='between(t,27.6,27.84)':volume=0,volume=enable='between(t,29.719,29.756)':volume=0,volume=enable='between(t,32.287,32.476)':volume=0,volume=enable='between(t,34.327,34.327)':volume=0,volume=enable='between(t,36.43,43.053)':volume=0,volume=enable='between(t,43.997,53.035)':volume=0 "+destPath;
		Log.d("xijingLog", "startMp3Silent ="+str);
		runCmd(str, srcPath, destPath);
	}

	private void startMp3fromMp4(String srcPath) {
		String destPath = Environment.getExternalStorageDirectory()+ "/xijing/audioFromvideo.mp3";
		String str = "-y -i "+srcPath+" -f mp3 -vn "+destPath;
		Log.d("xijingLog", "startMp3fromMp4 ="+str);
		runCmd(str, srcPath, destPath);
	}

	private void startMp4MixMp3(String srcPath) {
		String destPath = Environment.getExternalStorageDirectory()+ "/xijing/mp4MixMp3.mp4";
		String str = "-y -i "+ srcPath +
				" -i /storage/emulated/0/xijing/dubedit/longMp32WAV.wav -filter_complex " +
				"[0:a]aformat=sample_fmts=fltp:sample_rates=44100:channel_layouts=stereo,volume=0[a0];" +
				"[1:a]aformat=sample_fmts=fltp:sample_rates=44100:channel_layouts=stereo,volume=1[a1];" +
				"[a0][a1]amix=inputs=2:duration=first[aout] -map [aout] -ac 2 -c:v copy -map 0:v:0 " +
				"-preset superfast "+destPath;
		runCmd(str, srcPath, destPath);
	}

	private void startMp4MixMp4(String srcPath) {
		String destPath = Environment.getExternalStorageDirectory()+ "/xijing/mp4MixMp4.mp4";
		String str = "-y -i "+ srcPath +
				" -i /storage/emulated/0/xijing/1.mp4 -filter_complex " +
				"[1:v]scale=274:218:force_original_aspect_ratio=decrease[ckout];" +
				"[0:v][ckout]overlay=x=972:y=136:enable='between(t,0,8)' "+destPath;
		runCmd(str, srcPath, destPath);
	}

	private void startAddWater(String srcPath) {
		String destPath = Environment.getExternalStorageDirectory()+ "/xijing/watermark.mp4";
		String str = "-y -i "+ srcPath +
				" -i "+Environment.getExternalStorageDirectory()+"/xijing/video/icon_watermark.png " +
				"-safe 0 -filter_complex [0:v]scale=iw:ih[outv0];scale=256:68[outv1];" +
				"[outv0][outv1]overlay=984:612 "+destPath;
		runCmd(str, srcPath, destPath);
	}

	int progress = 0;
	private void runCmd(String str, String videoPath, final String destPath){
		EpEditor.execCmd(str, VideoUitls.getDuration(videoPath), new OnEditorListener() {
			@Override
			public void onSuccess() {
				Log.d("xijingLog", Thread.currentThread()+"  编辑完成 "+destPath);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						++ progress;
//						if(progress == 1){
//							startMp4MixMp4(destPath);
//						} else if(progress == 2){
//							startAddWater(destPath);
//						}

					}
				});
				Log.d("xijingLog", Thread.currentThread()+"  编辑完成 =");
			}

			@Override
			public void onFailure() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(MainActivity.this, "编辑失败", Toast.LENGTH_SHORT).show();
					}
				});
				Log.d("xijingLog", Thread.currentThread()+"  编辑失败 =");
			}

			@Override
			public void onProgress(float v) {

				Log.d("xijingLog", Thread.currentThread()+"  onProgress ="+(int) (v * 100));
			}
		});
	}
}
