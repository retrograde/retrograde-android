/*
 * GDriveGameLibraryProvider.kt
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

import android.content.Context
import android.net.Uri
import com.codebutler.odyssey.lib.library.GameLibraryFile
import com.codebutler.odyssey.lib.library.db.entity.Game
import com.codebutler.odyssey.lib.library.provider.GameLibraryProvider
import com.gojuno.koptional.None
import com.gojuno.koptional.Optional
import com.google.api.services.drive.Drive
import io.reactivex.Completable
import io.reactivex.Single
import java.io.File
import java.io.FileOutputStream
import javax.inject.Provider
import kotlin.coroutines.experimental.buildSequence

private typealias GDriveFile = com.google.api.services.drive.model.File

class GDriveGameLibraryProvider(
        private val context: Context,
        private val driveProvider: Provider<Optional<Drive>>) : GameLibraryProvider {

    override val name: String = context.getString(R.string.gdrive_google_drive)

    override val uriSchemes = listOf("gdrive")

    override val prefsFragmentClass = GDrivePreferenceFragment::class.java

    override fun listFiles(): Single<Iterable<GameLibraryFile>> = Single.fromCallable {
        val folderId = getFolderId() ?: return@fromCallable listOf<GameLibraryFile>()
        val driveClient = driveProvider.get().toNullable() ?: return@fromCallable listOf<GameLibraryFile>()
        listDriveFiles(driveClient, folderId)
                .filter { file -> file.getSize() != null }
                .map { file ->
                    GameLibraryFile(
                            name = file.name,
                            size = file.getSize(),
                            uri = Uri.Builder()
                                    .scheme(uriSchemes.first())
                                    .authority(file.id)
                                    .build())
                }
                .asIterable()
    }

    override fun getGameRom(game: Game): Single<File> {
        val gamesCacheDir = File(context.cacheDir, "gdrive-games")
        gamesCacheDir.mkdirs()
        val gameFile = File(gamesCacheDir, game.fileName)
        if (gameFile.exists()) {
            return Single.just(gameFile)
        }
        val driveClient = driveProvider.get().toNullable() ?: throw IllegalStateException()
        return Single.fromCallable {
            FileOutputStream(gameFile).use { stream ->
                driveClient.files()
                        .get(game.fileUri.authority)
                        .executeMediaAndDownloadTo(stream)
            }
            gameFile
        }
    }

    override fun getGameSave(game: Game): Single<Optional<ByteArray>> = Single.just(None) // FIXME

    override fun setGameSave(game: Game, data: ByteArray): Completable = Completable.complete() // FIXME

    private fun getFolderId(): String? {
        val prefs = context.getSharedPreferences(GDrivePreferenceFragment.PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(GDrivePreferenceFragment.PREF_KEY_FOLDER_ID, null)
    }

    private fun listDriveFiles(driveClient: Drive, folderId: String): Sequence<GDriveFile> {
        return buildSequence {
            var pageToken: String? = null
            do {
                val result = driveClient.files().list()
                        .setQ("'$folderId' in parents")
                        .setFields("nextPageToken, files(id, name, mimeType, size)")
                        .setPageToken(pageToken)
                        .execute()
                for (file in result.files) {
                    if (file.mimeType == "application/vnd.google-apps.folder") {
                        yieldAll(listDriveFiles(driveClient, file.id))
                    } else {
                        yield(file)
                    }
                }
                pageToken = result.nextPageToken
            } while (pageToken != null)
        }
    }
}
