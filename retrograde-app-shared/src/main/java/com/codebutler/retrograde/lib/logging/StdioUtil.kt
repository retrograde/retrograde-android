/*
 * StdioUtil.kt
 *
 * Copyright (C) 2018 Retrograde Project
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

package com.codebutler.retrograde.lib.logging

import android.content.Context
import android.system.OsConstants
import com.codebutler.retrograde.lib.binding.LibRetrograde
import org.jetbrains.anko.coroutines.experimental.bg
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.io.PipedInputStream
import java.io.PipedOutputStream
import java.io.PrintStream

object StdioUtil {

    fun redirectJavaStdio() {
        val stdoutStream = PipedOutputStream()
        val stderrStream = PipedOutputStream()

        System.setOut(PrintStream(stdoutStream, true, "utf-8"))
        System.setErr(PrintStream(stderrStream, true, "utf-8"))

        bg {
            InputStreamReader(PipedInputStream(stdoutStream)).useLines { lines ->
                for (line in lines) {
                    Timber.tag("stdout").d(line)
                }
            }
        }

        bg {
            InputStreamReader(PipedInputStream(stderrStream)).useLines { lines ->
                for (line in lines) {
                    Timber.tag("stderr").e(line)
                }
            }
        }
    }

    fun redirectNativeStdio(context: Context) {
        val stdoutFile = File(context.noBackupFilesDir, "stdout.log")
        val stderrFile = File(context.noBackupFilesDir, "stderr.log")

        stdoutFile.delete()
        stderrFile.delete()

        val mode = OsConstants.S_IRUSR or OsConstants.S_IWUSR

        LibRetrograde.mkfifo(stdoutFile.path, mode)
        LibRetrograde.mkfifo(stderrFile.path, mode)

        bg {
            InputStreamReader(FileInputStream(stdoutFile)).useLines { lines ->
                for (line in lines) {
                    Timber.tag("stdout").d(line)
                }
            }
        }

        bg {
            InputStreamReader(FileInputStream(stderrFile)).useLines { lines ->
                for (line in lines) {
                    Timber.tag("stderr").e(line)
                }
            }
        }

        LibRetrograde.redirectStdio(stdoutFile.path, stderrFile.path)
    }
}
