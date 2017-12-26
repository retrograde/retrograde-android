/*
 * RetrogradeLogService.kt
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

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.codebutler.retrograde.ipc.IRetrogradeIpcService
import timber.log.Timber

class RetrogradeLogService : Service() {

    private val binder = object : IRetrogradeIpcService.Stub() {
        override fun writeToLog(priority: Int, tag: String, message: String, error: String?) {
            if (error != null) {
                Timber.tag(tag).log(priority, Throwable(error), message)
            } else {
                Timber.tag(tag).log(priority, message)
            }
        }
    }

    override fun onBind(intent: Intent): IBinder = binder
}
