package com.example.ballroll;


import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class BallRoll extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ball_roll);
	}

	public void button1_Click(View view) {
		
		Intent intent = new Intent();
		intent.setClass(BallRoll.this, Game.class);
		startActivity(intent);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
	}
	//start_game
	public void button2_Click(View view) {
  	AlertDialog.Builder builder = new AlertDialog.Builder(this); 
      builder.setTitle("關於");  
      
      builder.setMessage("版本: 0.05BETA\n作者: 金瑋榮 胡秉丞 林育堯");
      
      builder.setPositiveButton("確定",
      		new DialogInterface.OnClickListener() { 
        public void onClick(DialogInterface  
                             dialoginterface, int i) { 
          
        } 
      }); 
      builder.show();  
  }
	//about
	public void button3_Click(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this); 
			builder.setTitle("確認")
	          .setMessage("確認結束本程式?")
	          .setPositiveButton("確認",
	        		new DialogInterface.OnClickListener() { 
	                public void onClick(DialogInterface  
	                               dialoginterface, int i) { 
	                   finish();  
	                } 
	          }) 
	          .setNegativeButton("取消", null) 
	          .show();
	}
	//exit_game

}
