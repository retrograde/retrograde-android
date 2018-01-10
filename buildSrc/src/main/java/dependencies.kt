/* ktlint-disable no-multi-spaces max-line-length */
object deps {
    object android {
        val compileSdkVersion   = 27
        val targetSdkVersion    = 27
        val minSdkVersion       = 23
        val buildToolsVersion   = "27.0.2"
    }

    object versions {
        val arch            = "1.0.0"
        val autoDispose     = "0.5.1"
        val dagger          = "2.13"
        val gms             = "11.6.2"
        val googleApiClient = "1.23.0"
        val kotlin          = "1.2.10"
        val koptional       = "1.2.0"
        val moshi           = "1.5.0"
        val retrofit        = "2.3.0"
        val support         = "27.0.2"
        val okHttp          = "3.9.1"
    }

    object libs {
        val ankoCoroutines                  = "org.jetbrains.anko:anko-coroutines:0.10.3"
        val archLifecycleCommonJava8        = "android.arch.lifecycle:common-java8:${versions.arch}"
        val archPaging                      = "android.arch.paging:runtime:1.0.0-alpha4-1"
        val autoDispose                     = "com.uber.autodispose:autodispose:${versions.autoDispose}"
        val autoDisposeAndroid              = "com.uber.autodispose:autodispose-android:${versions.autoDispose}"
        val autoDisposeAndroidArch          = "com.uber.autodispose:autodispose-android-archcomponents:${versions.autoDispose}"
        val autoDisposeAndroidArchKotlin    = "com.uber.autodispose:autodispose-android-archcomponents-kotlin:${versions.autoDispose}"
        val autoDisposeAndroidKotlin        = "com.uber.autodispose:autodispose-android-kotlin:${versions.autoDispose}"
        val autoDisposeKotlin               = "com.uber.autodispose:autodispose-kotlin:${versions.autoDispose}"
        val crashlytics                     = "com.crashlytics.sdk.android:crashlytics:2.8.0@aar"
        val dagger                          = "com.google.dagger:dagger:${versions.dagger}"
        val daggerAndroid                   = "com.google.dagger:dagger-android:${versions.dagger}"
        val daggerAndroidProcessor          = "com.google.dagger:dagger-android-processor:${versions.dagger}"
        val daggerAndroidSupport            = "com.google.dagger:dagger-android-support:${versions.dagger}"
        val daggerCompiler                  = "com.google.dagger:dagger-compiler:${versions.dagger}"
        val gmsAuth                         = "com.google.android.gms:play-services-auth:${versions.gms}"
        val googleApiClient                 = "com.google.api-client:google-api-client:${versions.googleApiClient}"
        val googleApiClientAndroid          = "com.google.api-client:google-api-client-android:${versions.googleApiClient}"
        val googleApiServicesDrive          = "com.google.apis:google-api-services-drive:v3-rev92-1.23.0"
        val jna                             = "net.java.dev.jna:jna:4.5.0@aar"
        val koptional                       = "com.gojuno.koptional:koptional:${versions.koptional}"
        val koptionalRxJava2                = "com.gojuno.koptional:koptional-rxjava2-extensions:${versions.koptional}"
        val kotlinStdlib                    = "org.jetbrains.kotlin:kotlin-stdlib-jre7:${versions.kotlin}"
        val kotlinxCoroutinesAndroid        = "org.jetbrains.kotlinx:kotlinx-coroutines-android:0.20"
        val moshi                           = "com.squareup.moshi:moshi:${versions.moshi}"
        val moshiKotlin                     = "com.squareup.moshi:moshi-kotlin:${versions.moshi}"
        val okHttp3                         = "com.squareup.okhttp3:okhttp:${versions.okHttp}"
        val okHttp3Logging                  = "com.squareup.okhttp3:logging-interceptor:${versions.okHttp}"
        val picasso                         = "com.squareup.picasso:picasso:2.5.2"
        val retrofit                        = "com.squareup.retrofit2:retrofit:${versions.retrofit}"
        val retrofitConverterMoshi          = "com.squareup.retrofit2:converter-moshi:${versions.retrofit}"
        val retrofitRxJava2                 = "com.squareup.retrofit2:adapter-rxjava2:${versions.retrofit}"
        val roomCompiler                    = "android.arch.persistence.room:compiler:${versions.arch}"
        val roomRuntime                     = "android.arch.persistence.room:runtime:${versions.arch}"
        val roomRxJava2                     = "android.arch.persistence.room:rxjava2:${versions.arch}"
        val rxAndroid2                      = "io.reactivex.rxjava2:rxandroid:2.0.1"
        val rxJava2                         = "io.reactivex.rxjava2:rxjava:2.1.7"
        val rxPermissions2                  = "com.tbruyelle.rxpermissions2:rxpermissions:0.9.4@aar"
        val rxPreferences                   = "com.f2prateek.rx.preferences2:rx-preferences:2.0.0-RC3"
        val rxRelay2                        = "com.jakewharton.rxrelay2:rxrelay:2.0.0"
        val supportAppCompatV7              = "com.android.support:appcompat-v7:${versions.support}"
        val supportLeanbackV17              = "com.android.support:leanback-v17:${versions.support}"
        val supportPaletteV7                = "com.android.support:palette-v7:${versions.support}"
        val supportPrefLeanbackV17          = "com.android.support:preference-leanback-v17:${versions.support}"
        val supportRecyclerViewV7           = "com.android.support:recyclerview-v7:${versions.support}"
        val timber                          = "com.jakewharton.timber:timber:4.6.0"
    }
}
