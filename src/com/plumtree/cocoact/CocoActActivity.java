package com.plumtree.cocoact;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class CocoActActivity extends Activity {
	
	protected CCGLSurfaceView _glSurfaceView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        _glSurfaceView = new CCGLSurfaceView(this);
        setContentView(_glSurfaceView);
    }
    
    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    	
    	CCDirector.sharedDirector().attachInView(_glSurfaceView);
    	CCDirector.sharedDirector().setDisplayFPS(true);
    	CCDirector.sharedDirector().setAnimationInterval(1.0f / 60.0f);
    	
    	CCScene scene = GameLayer.scene();
    	CCDirector.sharedDirector().runWithScene(scene);
    }
    
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	
    	CCDirector.sharedDirector().pause();
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	
    	CCDirector.sharedDirector().resume();
    }
    
    @Override
    protected void onStop() {
    	// TODO Auto-generated method stub
    	super.onStop();
    	
    	CCDirector.sharedDirector().end();
    }
}