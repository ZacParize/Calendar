/*
 * Created by IntelliJ IDEA.
 * User: mba
 * Date: 11.11.13
 * Time: 22:30
 */
package com.example;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

public class Cylinder
{
    private final static int verticesCount = 20;
    private final static int indicesCount = verticesCount * 2;

    private FloatBuffer mVertexBuffer;
    private FloatBuffer mTexBuffer;
    private ByteBuffer  mIndexBuffer;

    public Cylinder()
    {
        float vertices[] = new float[(verticesCount + 1) * 12];
        float texture[] = new float[verticesCount * 8 + 1];

        float tx = 1.0f, dx = 1.0f/verticesCount;
        for(int i = 0; i < verticesCount * 8; i+=8)
        {
            texture[i]   = tx;    texture[i+1] = 1.0f;
            texture[i+2] = tx;    texture[i+3] = 0.0f;
            texture[i+4] = tx;    texture[i+5] = 0.0f;
            texture[i+6] = tx-dx; texture[i+7] = 1.0f;
            tx-=dx;
        }
        byte indices[] = new byte[indicesCount * 2];

        double theta = (Math.PI / 180) * (360 / verticesCount);

        for (int j = 0; j <= verticesCount ; j++)
        {
            int t = j * 12;
            vertices[t]     = (float)(1.0f * Math.cos(theta * j));
            vertices[t + 1] = -1.15f;
            vertices[t + 2] = (float)(1.0f * Math.sin(theta * j));

            vertices[t + 3] = (float)(1.0f * Math.cos(theta * j));
            vertices[t + 4] = 1.15f;
            vertices[t + 5] = (float)(1.0f * Math.sin(theta * j));

            vertices[t + 6] = (float)(1.0f * Math.cos(theta * j));
            vertices[t + 7] = 1.15f;
            vertices[t + 8] = (float)(1.0f * Math.sin(theta * j));

            vertices[t + 9] = (float)(1.0f * Math.cos(theta * (j + 1)));
            vertices[t + 10] = -1.15f;
            vertices[t + 11] = (float)(1.0f * Math.sin(theta * (j + 1)));
        }

        int k = 0, kk = 0;
        for(int i = 0; i < indicesCount; i+=3)
        {
            indices[k + i]     = (byte)(i+kk);     // 0
            indices[k + i + 1] = (byte)(i+kk + 2); // 2
            indices[k + i + 2] = (byte)(i+kk + 1); // 1

            indices[k + i + 3] = (byte)(i+kk + 6); // 1
            indices[k + i + 4] = (byte)(i+kk + 1); // 2
            indices[k + i + 5] = (byte)(i+kk + 4); // 4
            k+=3;    kk++;
        }

        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 2);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        ByteBuffer tbb = ByteBuffer.allocateDirect(texture.length * 2);
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
        gl.glDrawElements(GL10.GL_TRIANGLES, indicesCount, GL10.GL_UNSIGNED_BYTE, mIndexBuffer);
    }
}
