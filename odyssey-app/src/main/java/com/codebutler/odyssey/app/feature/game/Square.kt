package com.codebutler.odyssey.app.feature.game

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import android.opengl.GLES20



class Square {
    private val vertexBuffer: FloatBuffer
    private val drawListBuffer: ShortBuffer
    private val vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}"
    private val fragmentShaderCode =
            "precision mediump float;" +
                    "void main() {" +
                    "  gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);" +
                    "}"
    private val program: Int

    private val squareCoords = floatArrayOf(
            -1.0f,  1.0f, 0.0f, // top left
            -1.0f, -1.0f, 0.0f, // bottom left
             1.0f, -1.0f, 0.0f, // bottom right
             1.0f,  1.0f, 0.0f) // top right

    // number of coordinates per vertex in this array
    private val COORDS_PER_VERTEX = 3
    private val vertexCount: Int = squareCoords.size / COORDS_PER_VERTEX
    private val vertexStride: Int = COORDS_PER_VERTEX * 4 // 4 bytes per vertex

    private val drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3) // order to draw vertices

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

        // Draw the square
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES, drawOrder.size,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer)

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle)
    }
}