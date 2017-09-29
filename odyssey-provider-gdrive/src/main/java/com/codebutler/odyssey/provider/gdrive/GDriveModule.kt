/*
 * GDriveModule.kt
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
import com.codebutler.odyssey.lib.library.provider.GameLibraryProvider
import com.gojuno.koptional.None
import com.gojuno.koptional.Optional
import com.gojuno.koptional.toOptional
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.extensions.android.json.AndroidJsonFactory
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import javax.inject.Provider

@Module
abstract class GDriveModule {
    @Module
    companion object {
        @Provides
        @JvmStatic
        fun drive(context: Context): Optional<Drive> {
            val googleAccount = GoogleSignIn.getLastSignedInAccount(context) ?: return None

            val credential = GoogleAccountCredential.usingOAuth2(context, listOf(DriveScopes.DRIVE))
            credential.selectedAccount = googleAccount.account

            val httpTransport = AndroidHttp.newCompatibleTransport()
            val jsonFactory = AndroidJsonFactory.getDefaultInstance()

            return Drive.Builder(httpTransport, jsonFactory, credential)
                    .setApplicationName(BuildConfig.APPLICATION_ID)
                    .build()
                    .toOptional()
        }

        @Provides
        @JvmStatic
        fun gdriveBrowser(driveProvider: Provider<Optional<Drive>>) = GDriveBrowser(driveProvider)

        @Provides
        @IntoSet
        @JvmStatic
        fun gdriveLibraryProvider(context: Context, driveProvider: Provider<Optional<Drive>>): GameLibraryProvider
                = GDriveGameLibraryProvider(context, driveProvider)
    }
}
