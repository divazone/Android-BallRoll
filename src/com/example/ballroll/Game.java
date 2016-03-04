package com.example.ballroll;

import android.app.Service;
import java.util.List;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import java.util.ArrayList;
import android.hardware.SensorManager;

class holl{
	int left;
	int top;
	
	public void setplace(int x,int y) {
		left = x;
		top = y;
	}
}

class wall{
	int left;
	int top;
	
	public void setplace(int x,int y) {
		left = x;
		top = y;
	}
}

public class Game extends Activity implements SensorEventListener {
	
	private int				screenWidth, screenHeight;	
	MyView						view;
	Handler						handler	= new Handler();
	Thread						mainLoop;
	Bitmap						bitmap;
	Bitmap						bitmap2;
	Bitmap						bitmap3;
	Bitmap                      bitmap4;
	private static final int	GALLERY	= 0;
	private static final int	MANU02	= 1;
	private static final int	MANU03	= 2;
	int							left	= 20, top = 20;
	Paint						paint	= new Paint();
	long						time;
	int							tx, ty;
	Context						context;
	private SensorManager		senorManager;
	int							winlose	= 0;
	int                         draw =0;
	long                        time1;
	long                        time2;
	long                        time3;
	static ArrayList<holl> Holl = new ArrayList<holl>();
	static ArrayList<wall> Wall = new ArrayList<wall>();

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(GALLERY, MANU02, Menu.NONE, "結束").setShortcut('1', 's').setIcon(R.drawable.ball40x40);
		menu.add(GALLERY, MANU03, Menu.NONE, "重新開始").setShortcut('2', 'f').setIcon(R.drawable.ball40x40);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getGroupId()) {
			case GALLERY:
				String itemid = Integer.toString(item.getItemId());
				String title = item.getTitle().toString();
				if (item.getItemId() == 1) {// 遊戲結束

					Game.this.finish();
				}
				if (item.getItemId() == 2) {// 遊戲重開
					top = 10;
					left = 10;
					winlose = 0;
					time1=System.currentTimeMillis();
				}
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	class MyView extends View implements Runnable {

		BitmapDrawable	background;
		
		public MyView(Context context) {
			super(context);
			setFocusable(true);
			requestFocus();

			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			screenWidth = dm.widthPixels;
			screenHeight = dm.heightPixels;
			
			bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ball40x40);
			bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.don40x40);
			bitmap3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.end40x40);
			bitmap4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.wall40x40);
			paint.setColor(Color.BLUE);
			paint.setTextSize(24);
			
			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.floor);
			background = new BitmapDrawable(context.getResources(), bitmap);
			background.setBounds(0, 0, screenWidth, screenHeight);
			
			tx = bitmap.getWidth();
			ty = bitmap.getHeight() / 2;
			mainLoop = new Thread(this);
			mainLoop.start();
		}

		@Override
		protected void onDraw(Canvas canvas) {
			Log.d("TEST", "onDraw");
			if (canvas != null) {
				background.draw(canvas);
				paint.setTextSize(32);
				
				//畫出終點
				while (draw < Holl.size() )
				{
					canvas.drawBitmap(bitmap2, Holl.get(draw).left, Holl.get(draw).top, null);	
					draw++;
				}
				draw=0;
				//
				
				//畫出牆壁
				while (draw < Wall.size() )
				{
					canvas.drawBitmap(bitmap4, Wall.get(draw).left, Wall.get(draw).top, null);	
					draw++;
				}
				
				draw=0;
				//
				
				canvas.drawBitmap(bitmap3, 130, 300, null);
				if (winlose != 3) {
					canvas.drawBitmap(bitmap, left, top, null);

				}

				//障礙物設置
				while (draw < Holl.size() )
				{
					if ((left >= Holl.get(draw).left -20 && left <= Holl.get(draw).left+20) && (top >= Holl.get(draw).top-20 && top <= Holl.get(draw).top+20)) {
						winlose = 1;
					}	
					
					draw++;
				}
				draw=0;
				//
					
				//判斷是否過關
				if ((left >= 110 && left <= 150) && (top >= 270 && top <= 330)) {
					winlose = 2;
				}

				if (winlose == 1) {
					top = 10;
					left = 10;
					winlose = 0;
				}
				if (winlose == 2) {
					top = -100;
					left = 100;
					time3=time2;
	
					winlose = 3;
				}
				if (winlose == 3) {
					time2=time3;
				    time1=System.currentTimeMillis();
				    paint.setTextSize(32);
				    canvas.drawText("恭喜過關!!", 30, 120, paint);
				    canvas.drawText("請按返回鍵回主畫面", 30, 150, paint);
				}

				paint.setTextSize(24);
				long now = System.currentTimeMillis();
				
				canvas.drawText("time:"+time2/1000+"秒", 130, 30, paint);
				time2=(now-time1);
			}
		}

		public void run() {
			while (true) {
				handler.post(new Runnable() {
					public void run() {
						invalidate();
					}
				});
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		List<Sensor> sensors = senorManager.getSensorList(Sensor.TYPE_ORIENTATION);
		if (sensors.size() > 0) {
			senorManager.registerListener(this, sensors.get(0), SensorManager.SENSOR_DELAY_FASTEST);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		senorManager.unregisterListener(this);
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//加入洞
		holl a = new holl();
		a.setplace(250, 60);
		Holl.add(a);
		
		holl b = new holl();
		b.setplace(100, 150);
		Holl.add(b);
		
		holl c = new holl();
		c.setplace(200, 70);
		Holl.add(c);
		
		holl d = new holl();
		d.setplace(150, 350);
		Holl.add(d);
		
		holl e = new holl();
		e.setplace(50, 280);
		Holl.add(e);
		
		//加入牆壁(三個for)
		for (int x = 320; x<=1920;x+=40)
			for(int y =0 ; y<=440; y+=40){
				wall f = new wall();
				f.setplace(x, y);
				Wall.add(f);
			}
		
		for (int x = 0; x<= 360;x+=40)
			for(int y = 480 ; y<=1920; y+=40){
				wall f = new wall();
				f.setplace(x, y);
				Wall.add(f);
			}
		
		for (int x = 360; x<= 1920;x+=40)
			for(int y = 480 ; y<=1920; y+=40){
				wall f = new wall();
				f.setplace(x, y);
				Wall.add(f);
			}
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		senorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		time1=System.currentTimeMillis();
		view = new MyView(this);
		setContentView(view);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mainLoop.interrupt();
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (winlose == 0) {
			if (left == 280 || left == 0 || top == 440 || top == 0) {
				//donothing
			}
			if (event.values[2] >0&&event.values[2] < 5) {
				if (left > 0) {
					left = left - 1;
				}	
			}
			else if (event.values[2] >5&&event.values[2] < 15) {
				if (left > 0) {
					left = left - 3;
				}
			}
			else if (event.values[2] >15&&event.values[2] < 30) {
				if (left > 0) {
					left = left - 6;
				}
			}
				
			if (event.values[2] <0&&event.values[2] > -5) {
				if (left < 280) {
					left = left + 1;
				}		
			}
			else if (event.values[2] <-5&&event.values[2] > -15) {
				if (left < 280) {
					left = left + 3;
				}			
			}
			else if (event.values[2] <-15&&event.values[2] > -30) {
				if (left < 280) {
					left = left + 6;
				}
			}
			
			if (event.values[1] >0&&event.values[1] < 5) {
				if (top >0) {
					top = top - 1;
				}
			}
			else if (event.values[1] >5&&event.values[1] < 15) {
				if (top > 0) {
					top = top - 3;
				}
			}
			else if (event.values[1] >15&&event.values[1] < 30) {
				if (top > 0) {
					top = top - 6;
				}
			}
			if (event.values[1] <0&&event.values[1] > -5) {
				if (top < 440) {
					top = top + 1;
				}
			}
			else if (event.values[1] <-5&&event.values[1] > -15) {
				if (top < 440) {
					top = top + 3;
				}		
			}
			else if (event.values[1] <-15&&event.values[1] > -30) {
				if (top < 440) {
					top = top + 6;
				}
			}
		}
	}
}