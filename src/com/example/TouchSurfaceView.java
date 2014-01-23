/*
 * Created by IntelliJ IDEA.
 * User: mba
 * Date: 13.11.13
 * Time: 17:31
 */

package com.example;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import java.io.IOException;
import java.io.InputStream;

public class TouchSurfaceView extends GLSurfaceView {
    public TouchSurfaceView(Context context) {
        super(context);
        mRenderer = new CylinderRenderer(context);
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
    public TouchSurfaceView(Context context, AttributeSet attr) {
        super(context, attr);
        mRenderer = new CylinderRenderer(context);
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onTrackballEvent(MotionEvent e) {
        float TRACKBALL_SCALE_FACTOR = 36.0f;
        mRenderer.mAngleX += e.getX() * TRACKBALL_SCALE_FACTOR;
        requestRender();
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                dx = x - mPreviousX;
                mRenderer.touchRotate(dx);
                break;

            case MotionEvent.ACTION_UP:
                mRenderer.beginRotate(dx);
                break;
        }
        mPreviousX = x;
        return true;
    }

    private class CylinderRenderer implements GLSurfaceView.Renderer {
        public CylinderRenderer(Context context) {
            mCylinder = new Cylinder();
            mPlane = new Plane(0);
            mPlane1 = new Plane(1);
            mPlane2 = new Plane(2);
            mPlane3 = new Plane(3);
            mPlane4 = new Plane(4);
            mContext = context;
            dx = 0;
            touch = true;
        }

        public void beginRotate(float dX) {
            touch = false;
            dx = dX * 1.5f; // 1.5f - need testing
        }

        public void touchRotate(float dX) {
            touch = true;
            dx = 0;
            mAngleX += dX * (180.0f / 320);
            requestRender();
        }

        public void onDrawFrame(GL10 gl) {
            if(!touch && dx == 0.0f)
                return;

            gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
            gl.glHint(GL10.GL_POINT_SMOOTH_HINT, GL10.GL_NICEST);
            gl.glHint(GL10.GL_POLYGON_SMOOTH_HINT, GL10.GL_NICEST);
            gl.glHint(GL10.GL_LINE_SMOOTH_HINT, GL10.GL_NICEST);

            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();
            gl.glTranslatef(0, 0.3f, -2.5f);
            gl.glScalef(1.0f, 1.0f, 0.5f);

            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

            gl.glActiveTexture(GL10.GL_TEXTURE0);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureID[1]);

            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);

            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

            mPlane.draw(gl);  // back
            mPlane1.draw(gl); // left
            mPlane2.draw(gl); // right
            mPlane3.draw(gl); // top
            mPlane4.draw(gl); // bot

            gl.glActiveTexture(GL10.GL_TEXTURE0);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureID[0]);

            mAngleX += dx;
            dx *= 0.85; // 0.85 - rotation slowdown
            if(Math.abs(dx) < 0.5f) // stop rotation if too slow
                dx = 0.0f;
            gl.glRotatef(mAngleX, 0, 1, 0);

            mCylinder.draw(gl); // wheel

            requestRender();
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            gl.glViewport(0, 0, width, height);
            float ratio = (float) width / height;
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //gl.glDisable(GL10.GL_DITHER);  for low quality, high performance
            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
            gl.glHint(GL10.GL_POINT_SMOOTH_HINT, GL10.GL_NICEST);
            gl.glHint(GL10.GL_POLYGON_SMOOTH_HINT, GL10.GL_NICEST);
            gl.glHint(GL10.GL_LINE_SMOOTH_HINT, GL10.GL_NICEST);

            gl.glClearColor(1.0f, 1.0f, 1.0f, 1);
            gl.glEnable(GL10.GL_CULL_FACE);
            gl.glEnable(GL10.GL_DEPTH_TEST);
            gl.glEnable(GL10.GL_TEXTURE_2D);
            gl.glShadeModel(GL10.GL_SMOOTH);

            int[] textures = new int[2];
            gl.glGenTextures(2, textures, 0);

            mTextureID = new int[2];
            mTextureID[0] = textures[0];
            gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureID[0]);

            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);

            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

            gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_REPLACE);

            /*InputStream is = mContext.getResources().openRawResource(R.raw.wheel_tex2);*/
            Bitmap bitmap = null;
            /*try {
                bitmap = BitmapFactory.decodeStream(is);
            } finally {
                try {
                    is.close();
                } catch(IOException e) {
                    // Ignore.
                }
            }*/

            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
            bitmap.recycle();

            mTextureID[1] = textures[1];
            gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureID[1]);

            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

            gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_REPLACE);

            /*is = mContext.getResources().openRawResource(R./*raw.wall_tex./wall_tex);*/
            Bitmap bitmap2 = null;
            /*try {
                bitmap2 = BitmapFactory.decodeStream(is);
            } finally {
                try {
                    is.close();
                } catch(IOException e) {
                    // Ignore.
                }
            }*/

            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap2, 0);
            bitmap2.recycle();
            requestRender();
        }

        private Cylinder mCylinder;
        private Plane mPlane, mPlane1, mPlane2, mPlane3, mPlane4;
        private Context mContext;
        private int mTextureID[];
        public float mAngleX;
        private float dx;
        private boolean touch;
    }

    private CylinderRenderer mRenderer;
    private float mPreviousX;
    private float dx;
}
