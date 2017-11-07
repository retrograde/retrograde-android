package com.codebutler.odyssey.app.feature.game

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLUtils
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import com.codebutler.odyssey.app.feature.common.FpsCalculator


class GLRenderer2d : GLSurfaceView.Renderer {
    val fpsCalculator: FpsCalculator = FpsCalculator()
    private var square: Square? = null
    public var callback: ((Long) -> Unit)? = null

    fun setBitmap(bitmap: Bitmap) {
        square?.bitmap = bitmap
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        square?.draw()
        fpsCalculator.update()
        callback?.invoke(fpsCalculator.fps())
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height);
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        square = Square()
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
    }

    companion object {
        fun loadShader(type: Int, shaderCode: String): Int {
            // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
            // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
            val shader = GLES20.glCreateShader(type)

            // add the source code to the shader and compile it
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)

            return shader
        }

        fun loadTexture(bitmap: Bitmap): Int {
            val textureHandle = IntArray(1)

            GLES20.glGenTextures(1, textureHandle, 0)

            if (textureHandle[0] != 0) {
                val options = BitmapFactory.Options()
                options.inScaled = false   // No pre-scaling

                // Bind to the texture in OpenGL
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0])

                // Set filtering
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST)
                GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST)

                // Load the bitmap into the bound texture.
                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)

                // TODO should do this later?
//                Recycle the bitmap, since its data has been loaded into OpenGL.
//                bitmap.recycle()
            }

            if (textureHandle[0] == 0) {
                throw RuntimeException("Error loading texture.")
            }

            return textureHandle[0]
        }
    }
}