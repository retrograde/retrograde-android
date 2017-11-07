package com.codebutler.odyssey.app.feature.game

import android.graphics.Bitmap
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import android.opengl.GLES20


class Square {
    private val bytesPerFloat: Int = 4
    private val vertexBuffer: FloatBuffer
    private val drawListBuffer: ShortBuffer
    private val uvBuffer: FloatBuffer
    private val vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "attribute vec2 a_TexCoordinate;" +
                    "varying vec2 v_TexCoordinate;" +
                    "void main() {" +
                    "  v_TexCoordinate = a_TexCoordinate;" +
                    "  gl_Position = vPosition;" +
                    "}"
    private val fragmentShaderCode =
            "precision mediump float;" +
                    "uniform sampler2D u_Texture;" +
                    "varying vec2 v_TexCoordinate;" +
                    "void main() {" +
                    "  gl_FragColor = texture2D(u_Texture, v_TexCoordinate);" +
                    "}"
    private val program: Int

    private val squareCoords = floatArrayOf(
            -1.0f,  1.0f, 0.0f, // top left
            -1.0f, -1.0f, 0.0f, // bottom left
             1.0f, -1.0f, 0.0f, // bottom right
             1.0f,  1.0f, 0.0f) // top right

    private val squareUvCoords = floatArrayOf(
            0.0f, 0.0f, // top left
            0.0f, 1.0f, // bottom left
            1.0f, 1.0f, // bottom right
            1.0f, 0.0f) // top right

    // number of coordinates per vertex in this array
    private val COORDS_PER_VERTEX = 3
    private val vertexCount: Int = squareCoords.size / COORDS_PER_VERTEX
    private val vertexStride: Int = COORDS_PER_VERTEX * 4 // 4 bytes per vertex

    private val drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3) // order to draw vertices

    public var bitmap: Bitmap? = null

    init {
        // initialize vertex byte buffer for shape coordinates
        val bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                squareCoords.size * 4)
        bb.order(ByteOrder.nativeOrder())
        vertexBuffer = bb.asFloatBuffer()
        vertexBuffer.put(squareCoords)
        vertexBuffer.position(0)

        // initialize byte buffer for the draw list
        val dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.size * 2)
        dlb.order(ByteOrder.nativeOrder())
        drawListBuffer = dlb.asShortBuffer()
        drawListBuffer.put(drawOrder)
        drawListBuffer.position(0)

        uvBuffer = ByteBuffer.allocateDirect(squareUvCoords.size * bytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer()
        uvBuffer.put(squareUvCoords).position(0)

        val vertexShader = GLRenderer2d.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode)
        val fragmentShader = GLRenderer2d.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode)

        // create empty OpenGL ES Program
        program = GLES20.glCreateProgram()

        // add the vertex shader to program
        GLES20.glAttachShader(program, vertexShader)

        // add the fragment shader to program
        GLES20.glAttachShader(program, fragmentShader)

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(program)
    }

    fun draw() {
        val b = bitmap ?: return
        val textureLocation = GLRenderer2d.loadTexture(b)

        // Add program to OpenGL ES environment
        GLES20.glUseProgram(program)

        // get handle to vertex shader's vPosition member
        val positionHandle = GLES20.glGetAttribLocation(program, "vPosition")

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(positionHandle)

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
                positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer)

        // Pass in the texture coordinate information
        val textureHandle = GLES20.glGetAttribLocation(program, "a_TexCoordinate");
        uvBuffer.position(0)
        GLES20.glVertexAttribPointer(textureHandle, 2, GLES20.GL_FLOAT, false,
                0, uvBuffer)

        GLES20.glEnableVertexAttribArray(textureHandle);

        // Draw the square
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES, drawOrder.size,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer)

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle)
    }
}