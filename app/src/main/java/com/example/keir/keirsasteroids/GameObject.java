package com.example.keir.keirsasteroids;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.opengles.GL10;

public class GameObject {
    private FloatBuffer vertexBuffer;
    private FloatBuffer normalBuffer;
    private FloatBuffer texturecoordsBuffer;
    private ShortBuffer indexBuffer;
    private int[] textureId = new int[1];
    public GameObject() {
    }
    public void draw(GL10 gl) {
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId[0]);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texturecoordsBuffer); gl.glDrawElements(GL10.GL_TRIANGLES, indexBuffer.capacity(),
                GL10.GL_UNSIGNED_SHORT, indexBuffer);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    }
    public void loadMesh(float [] vertices, float [] normals,
                         float [] texturecoords, short [] faces){
        ByteBuffer byteBuf;
        byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuf.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
        byteBuf = ByteBuffer.allocateDirect(normals.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        normalBuffer = byteBuf.asFloatBuffer();
        normalBuffer.put(normals);
        normalBuffer.position(0);
        byteBuf = ByteBuffer.allocateDirect(texturecoords.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        texturecoordsBuffer = byteBuf.asFloatBuffer();
        texturecoordsBuffer.put(texturecoords);
        texturecoordsBuffer.position(0);
        byteBuf = ByteBuffer.allocateDirect(faces.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        indexBuffer = byteBuf.asShortBuffer();
        indexBuffer.put(faces);
        indexBuffer.position(0);
    }
    public void loadTexture(GL10 gl, int texture) {
        InputStream imagestream = GameView.context.getResources().openRawResource(texture);
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(imagestream);
        }catch(Exception e){
        }finally {
            try {
                imagestream.close();
            } catch (IOException e) {
            }
        }
        gl.glGenTextures(1, textureId, 0);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId[0]);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
    }
}