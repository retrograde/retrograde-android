/*
 * RetrogradeServiceTimberTree.kt
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

package com.codebutler.retrograde.app.shared.logging

import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.Process
import com.codebutler.retrograde.ipc.IRetrogradeIpcService
import org.jetbrains.anko.coroutines.experimental.bg
import timber.log.Timber

class RetrogradeServiceTimberTree(val context: Context) : Timber.DebugTree() {

    private val myProcessName = (context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
            .runningAppProcesses.find { it.pid == Process.myPid() }!!
            .processName.substringAfter(':')

    private var service: IRetrogradeIpcService? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, binder: IBinder) {
            service = IRetrogradeIpcService.Stub.asInterface(binder)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            service = null
        }
    }

    init {
        val intent = Intent(context, RetrogradeLogService::class.java)
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun log(priority: Int, tag: String?, message: String, error: Throwable?) {
        bg {
            val tagWithProcess = "$myProcessName:$tag"
            if (error != null) {
                service?.writeToLog(priority, tagWithProcess, message, error.toString())
            } else {
                service?.writeToLog(priority, tagWithProcess, message, null)
            }
        }
    }
}
