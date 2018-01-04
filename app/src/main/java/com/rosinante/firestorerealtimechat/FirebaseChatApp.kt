package com.rosinante.firestorerealtimechat

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex

/**
 * Created by irfanirawansukirman on 04/11/17.
 */
class FirebaseChatApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}
