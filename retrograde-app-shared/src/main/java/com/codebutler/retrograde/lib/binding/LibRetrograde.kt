/*
 * LibRetrograde.kt
 *
 * Copyright (C) 2017 Retrograde Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.codebutler.retrograde.lib.binding

import com.codebutler.retrograde.common.jna.UnsignedInt
import com.codebutler.retrograde.lib.retro.LibRetro
import com.sun.jna.Library
import com.sun.jna.Native

interface LibRetrograde : Library {

    companion object {
        private val INSTANCE = Native.loadLibrary("retrograde", LibRetrograde::class.java)

        fun setLogCallback(cb: LibRetro.retro_log_printf_t) {
            INSTANCE.retrograde_set_log_callback(cb)
        }

        fun getRetroLogPrintf() = INSTANCE.retrograde_get_retro_log_printf()

        fun redirectStdio(stdoutPath: String, stderrPath: String) {
            INSTANCE.retrograde_redirect_stdio(stdoutPath, stderrPath)
        }

        fun mkfifo(path: String, mode: Int) {
            val result = INSTANCE.retrograde_mkfifo(path, UnsignedInt(mode.toLong()))
            if (result != 0) {
                throw Exception("mkfifo failed: $result")
            }
        }
    }

    fun retrograde_set_log_callback(cb: LibRetro.retro_log_printf_t)

    fun retrograde_get_retro_log_printf(): LibRetro.retro_log_printf_t

    fun retrograde_redirect_stdio(stdoutPath: String, stderrPath: String)

    fun retrograde_mkfifo(path: String, mode: UnsignedInt): Int
}
