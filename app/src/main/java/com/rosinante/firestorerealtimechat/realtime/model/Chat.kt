package com.rosinante.firestorerealtimechat.realtime.model

import com.google.firebase.database.IgnoreExtraProperties
import com.rosinante.firestorerealtimechat.chatabstract.AbstractChat

/**
 * Created by Rosinante24 on 04/01/18.
 */
@IgnoreExtraProperties
class Chat : AbstractChat {
    private var mName: String? = null
    private var mMessage: String? = null
    private var mUid: String? = null

    constructor() {
        // Needed for Firebase
    }

    constructor(name: String, message: String, uid: String) {
        mName = name
        mMessage = message
        mUid = uid
    }

    override fun getName(): String? {
        return mName
    }

    fun setName(name: String) {
        mName = name
    }

    override fun getMessage(): String? {
        return mMessage
    }

    fun setMessage(message: String) {
        mMessage = message
    }

    override fun getUid(): String? {
        return mUid
    }

    fun setUid(uid: String) {
        mUid = uid
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false

        val chat = o as Chat?

        return (mUid == chat!!.mUid
                && (if (mName == null) chat.mName == null else mName == chat.mName)
                && if (mMessage == null) chat.mMessage == null else mMessage == chat.mMessage)
    }

    override fun hashCode(): Int {
        var result = if (mName == null) 0 else mName!!.hashCode()
        result = 31 * result + if (mMessage == null) 0 else mMessage!!.hashCode()
        result = 31 * result + mUid!!.hashCode()
        return result
    }

    override fun toString(): String {
        return "Chat{" +
                "mName='" + mName + '\'' +
                ", mMessage='" + mMessage + '\'' +
                ", mUid='" + mUid + '\'' +
                '}'
    }
}
