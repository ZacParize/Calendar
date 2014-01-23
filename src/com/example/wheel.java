/*
 * Created by IntelliJ IDEA.
 * User: mba
 * Date: 10.11.13
 * Time: 07:26
 */
 
package com.example;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;

public class wheel extends Activity {
    private GLSurfaceView mGLSurfaceView;
    View startLogo;
    View introText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        //mGLSurfaceView = (TouchSurfaceView)findViewById(R.id.glsurfaceview);
        mGLSurfaceView.requestFocus();
        mGLSurfaceView.setFocusableInTouchMode(true);

        //startLogo = findViewById(R.id.start_logo);
        //introText = findViewById(R.id.introduction);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run() {
                startLogo.setVisibility(View.INVISIBLE);
                introText.setVisibility(View.VISIBLE);
            }
        }, 1000);
        introText.setOnClickListener(mVisibleListener);
    }
    View.OnClickListener mVisibleListener = new View.OnClickListener() {
        public void onClick(View v) {
            introText.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}