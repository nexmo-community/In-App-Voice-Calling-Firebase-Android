package com.nexmo.inappvoicewithfirebase.utils

import android.content.Context
import android.util.Log
import com.nexmo.sdk.conversation.client.ConversationClient


class Stitch private constructor(context: Context) {
    var conversationClient: ConversationClient

    init {
        conversationClient = ConversationClient.ConversationClientBuilder()
                .context(context.applicationContext)
                .logLevel(Log.DEBUG)
                .onMainThread(true)
                .build()
    }

    companion object : SingletonHolder<Stitch, Context>(::Stitch)
}
