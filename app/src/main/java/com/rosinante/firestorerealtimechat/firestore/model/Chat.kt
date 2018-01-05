package com.rosinante.firestorerealtimechat.firestore.model

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import com.rosinante.firestorerealtimechat.chatabstract.AbstractChat

import java.util.Date

/*
 * Created by Rosinante24 on 04/01/18.
 */
@IgnoreExtraProperties
class Chat : AbstractChat {
    private var mName: String? = null
    private var mMessage: String? = null
    private var mUid: String? = null

    @ServerTimestamp
    private var timestamp: Date? = null

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

    override fun equals(any: Any?): Boolean {
        if (this === any) return true
        if (any == null || javaClass != any.javaClass) return false

        val chat = any as Chat?

        return (timestamp == chat!!.timestamp
                && mUid == chat.mUid
                && (if (mName == null) chat.mName == null else mName == chat.mName)
                && if (mMessage == null) chat.mMessage == null else mMessage == chat.mMessage)
    }

    override fun hashCode(): Int {
        var result = if (mName == null) 0 else mName!!.hashCode()
        result = 31 * result + if (mMessage == null) 0 else mMessage!!.hashCode()
        result = 31 * result + mUid!!.hashCode()
        result = 31 * result + timestamp!!.hashCode()
        return result
    }

    override fun toString(): String {
        return "Chat{" +
                "mName='" + mName + '\'' +
                ", mMessage='" + mMessage + '\'' +
                ", mTimestamp=" + timestamp + '\'' +
                ", mUid='" + mUid + '\'' +
                '}'
    }
}
