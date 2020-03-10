package com.example.mymusicplayer;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.entity.Music;

public class MainActivity extends Activity {

	ListView listView_music;
	ImageView button_play, button_stop, button_pause, button_next,
			button_previous;

	ArrayList<Music> musicList;
	MediaPlayer mp;
	int index = 0;
	boolean isPlaying = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initUI();
		initData();
		mp = new MediaPlayer();
		initMediaPlayer();
		listView_music.setAdapter(new MyListAdapter());
		setListener();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// 初始化UI（组件通过ID绑定UI组件）
	public void initUI() {

		listView_music = (ListView) findViewById(R.id.listView_music);
		button_play = (ImageView) findViewById(R.id.button_play);
		button_pause = (ImageView) findViewById(R.id.button_pause);
		button_stop = (ImageView) findViewById(R.id.button_stop);
		button_next = (ImageView) findViewById(R.id.button_next);
		button_previous = (ImageView) findViewById(R.id.button_previous);

	}

	// 初始化数据源
	public void initData() {
		// 创建一个列表存储音乐类对象
		musicList = new ArrayList<Music>();
		// 获取系统的内容接收器 ，查询系统中的媒体库
		ContentResolver cr = getContentResolver();
		// 设置查询路径为外围设备的所有媒体音频
		Cursor cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

		// 将查询结果通过循环创建对应MUsic对象并存入到列表中，完成数据源准备
		while (cursor.moveToNext()) {

			Music music = new Music();
			music.setName(cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
			music.setSinger(cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
			music.setData(cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.DATA)));

			musicList.add(music);

		}

	}

	// 设置监听
	public void setListener() {

		listView_music.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				index = arg2;
				initMediaPlayer();
				play();

			}
		});

	}

	// 自定义适配器

	class MyListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return musicList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return musicList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			View view = getLayoutInflater().from(MainActivity.this).inflate(
					R.layout.item, null);

			TextView textView_name = (TextView) view
					.findViewById(R.id.textView_name);
			TextView textView_singer = (TextView) view
					.findViewById(R.id.textView_singer);

			textView_name.setText(musicList.get(position).getName());
			textView_singer.setText(musicList.get(position).getSinger());

			return view;
		}

	}

	// 初始化音频播放器
	public void initMediaPlayer() {

		try {
			mp.reset();
			mp.setDataSource(musicList.get(index).getData());
			mp.prepare();

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 播放
	public void play() {
		mp.start();
	}

	public void playMusic(View v) {
		if (isPlaying == false) {
			initMediaPlayer();
		}
		play();
		isPlaying = true;
	}

	public void pauseMusic(View v) {
		mp.pause();
	}

	public void stopMusic(View v) {

		mp.stop();
		initMediaPlayer();
	}

	public void nextMusic(View v) {
		if (index >= musicList.size() - 1) {
			index = 0;
		} else

		{
			index++;
		}
		initMediaPlayer();
		play();

	}

	public void lastMusic(View v) {
		if (index <= 0) {
			index = musicList.size() - 1;
		} else {
			index--;
		}
		initMediaPlayer();
		play();

	}

}
