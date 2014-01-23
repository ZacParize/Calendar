/*
 * Created by IntelliJ IDEA.
 * User: mba
 * Date: 13.11.13
 * Time: 00:45
 */

package com.example;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Plane
{
    private FloatBuffer mVertexBuffer;
    private FloatBuffer mTexBuffer;
    private ByteBuffer  mIndexBuffer;

    public Plane(int type)
    {
        float vertices[] = new float[12];
        float texture[] = new float[8];
        byte indices[] = new byte[6];

        switch(type)
        {
            case 0:
                vertices[ 0] =  1.5f; vertices[ 1] = -2.0f; vertices[ 2] =  -0.5f;
                vertices[ 3] =  1.5f; vertices[ 4] =  2.0f; vertices[ 5] =  -0.5f;
                vertices[ 6] = -1.5f; vertices[ 7] =  2.0f; vertices[ 8] =  -0.5f;
                vertices[ 9] = -1.5f; vertices[10] = -2.0f; vertices[11] =  -0.5f;
                break;
            case 1:
                vertices[ 0] = -1.5f; vertices[ 1] = -2.0f; vertices[ 2] =  -0.5f;
                vertices[ 3] = -1.5f; vertices[ 4] =  2.0f; vertices[ 5] =  -0.5f;
                vertices[ 6] = -1.5f; vertices[ 7] =  2.0f; vertices[ 8] =  0.5f;
                vertices[ 9] = -1.5f; vertices[10] = -2.0f; vertices[11] =  0.5f;
                break;
            case 2:
                vertices[ 0] =  1.5f; vertices[ 1] = -2.0f; vertices[ 2] =   0.5f;
                vertices[ 3] =  1.5f; vertices[ 4] =  2.0f; vertices[ 5] =   0.5f;
                vertices[ 6] =  1.5f; vertices[ 7] =  2.0f; vertices[ 8] =  -0.5f;
                vertices[ 9] =  1.5f; vertices[10] = -2.0f; vertices[11] =  -0.5f;
                break;
            case 3:
                vertices[ 0] =  1.5f; vertices[ 1] =  2.0f; vertices[ 2] =  -0.5f;
                vertices[ 3] =  1.5f; vertices[ 4] =  2.0f; vertices[ 5] =   0.5f;
                vertices[ 6] = -1.5f; vertices[ 7] =  2.0f; vertices[ 8] =   0.5f;
                vertices[ 9] = -1.5f; vertices[10] =  2.0f; vertices[11] =  -0.5f;
                break;
            case 4:
                vertices[ 0] =  1.5f; vertices[ 1] = -2.0f; vertices[ 2] =   0.5f;
                vertices[ 3] =  1.5f; vertices[ 4] = -2.0f; vertices[ 5] =  -0.5f;
                vertices[ 6] = -1.5f; vertices[ 7] = -2.0f; vertices[ 8] =  -0.5f;
                vertices[ 9] = -1.5f; vertices[10] = -2.0f; vertices[11] =   0.5f;
                break;
        }

        indices[0] = 0; indices[1] = 3; indices[2] = 1;
        indices[3] = 1; indices[4] = 3; indices[5] = 2;

        texture[0] = 2.0f; texture[1] = 2.7f;
        texture[2] = 2.0f; texture[3] = 0.0f;
        texture[4] = 0.0f; texture[5] = 0.0f;
        texture[6] = 0.0f; texture[7] = 2.7f;

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        ByteBuffer tbb = ByteBuffer.allocateDirect(texture.length * 4);
        tbb.order(ByteOrder.nativeOrder());
        mTexBuffer = tbb.asFloatBuffer();
        mTexBuffer.put(texture);
        mTexBuffer.position(0);

        mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
        mIndexBuffer.put(indices);
        mIndexBuffer.position(0);
    }

    public void draw(GL10 gl)
    {
        gl.glFrontFace(GL10.GL_CW);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexBuffer);
        gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_BYTE, mIndexBuffer);
    }
}
