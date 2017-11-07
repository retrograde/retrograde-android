package com.codebutler.odyssey.app.feature.game

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GLRenderer2d : GLSurfaceView.Renderer {
    private var square: Square? = null

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        square?.draw()
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
    }
}