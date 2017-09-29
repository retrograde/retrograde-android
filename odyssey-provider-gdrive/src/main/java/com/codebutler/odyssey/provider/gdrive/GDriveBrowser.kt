/*
 * GDriveBrowser.kt
 *
 * Copyright (C) 2017 Odyssey Project
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
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.codebutler.odyssey.provider.gdrive

import com.gojuno.koptional.Optional
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import io.reactivex.Single
import javax.inject.Provider

class GDriveBrowser(private val driveProvider: Provider<Optional<Drive>>) {

    fun list(parentId: String = "root"): Single<List<File>> = Single.fromCallable {
        val drive = driveProvider.get().toNullable() ?: return@fromCallable listOf<File>()
        allPages(drive.files().list()
                .setQ("'$parentId' in parents")
                .setFields("nextPageToken, files(id, name, mimeType, size)"))
    }

    private fun allPages(query: Drive.Files.List): List<File> {
        val mutableList = mutableListOf<File>()
        var pageToken: String? = null
        do {
            val result = query.setPageToken(pageToken).execute()
            mutableList.addAll(result.files)
            pageToken = result.nextPageToken
        } while (pageToken != null)
        return mutableList.toList()
    }
}
