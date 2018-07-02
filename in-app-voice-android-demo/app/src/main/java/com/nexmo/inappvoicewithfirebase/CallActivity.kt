package com.nexmo.inappvoicewithfirebase

import android.os.Bundle
import com.nexmo.inappvoicewithfirebase.utils.Stitch
import com.nexmo.sdk.conversation.client.Call
import com.nexmo.sdk.conversation.client.CallEvent
import com.nexmo.sdk.conversation.client.ConversationClient
import com.nexmo.sdk.conversation.client.event.NexmoAPIError
import com.nexmo.sdk.conversation.client.event.RequestHandler
import com.nexmo.sdk.conversation.client.event.ResultListener
import com.nexmo.sdk.conversation.core.SubscriptionList
import kotlinx.android.synthetic.main.activity_call.*


class CallActivity : BaseActivity(), RequestHandler<Call> {
    private var currentCall: Call? = null
    private lateinit var client: ConversationClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call)

        client = Stitch.getInstance(this).conversationClient

        attachIncomingCallListener()
        callControlBtn.setOnClickListener { callPhone() }
    }

    private fun attachIncomingCallListener() {
        //Listen for incoming calls
        client.callEvent().add({ incomingCall ->
            logAndShow("answering Call")
            //Answer an incoming call
            incomingCall.answer(object : RequestHandler<Void> {
                override fun onError(apiError: NexmoAPIError) {
                    logAndShow("Error answer: " + apiError.message)
                }

                override fun onSuccess(result: Void) {
                    currentCall = incomingCall
                    attachCallStateListener(incomingCall)
                    showHangupButton()
                }
            })
        })
    }

    private fun attachCallStateListener(incomingCall: Call) {
        //Listen for incoming member events in a call
        val callEventListener = ResultListener<CallEvent> { message ->
            logAndShow("callEvent : state: " + message.state + " .content:" + message.toString())
        }
        incomingCall.event().add(callEventListener)
    }


    private fun callPhone() {
        val phoneNumber = phoneNumberInput.text.toString()

        client.callPhone(phoneNumber, object : RequestHandler<Call> {
            override fun onError(apiError: NexmoAPIError) {
                logAndShow("Cannot initiate call: " + apiError.message)
            }

            override fun onSuccess(result: Call) {
                currentCall = result
                showHangupButton()

                when (result.callState) {
                    Call.CALL_STATE.STARTED -> logAndShow("Started")
                    Call.CALL_STATE.RINGING -> logAndShow("Ringing")
                    Call.CALL_STATE.ANSWERED -> logAndShow("Answered")
                    else -> logAndShow("Error attaching call listener")
                }

            }
        })
    }

    private fun showHangupButton() {
        runOnUiThread {
            callControlBtn.text = "Hangup"
            callControlBtn.setOnClickListener { endCall() }
        }
    }

    private fun endCall() {
        currentCall?.hangup(object : RequestHandler<Void> {
            override fun onError(apiError: NexmoAPIError) {
                logAndShow("Cannot hangup: " + apiError.toString())
            }

            override fun onSuccess(result: Void) {
                logAndShow("Call completed.")
                runOnUiThread {
                    callControlBtn.text = "Call"
                    callControlBtn.setOnClickListener { callPhone() }
                }
            }
        })
    }

    override fun onSuccess(result: Call?) {
        callStatus.text = "In call with ${phoneNumberInput.text}"
        currentCall = result
        callControlBtn.setOnClickListener { endCall() }
    }


    override fun onError(apiError: NexmoAPIError?) {
        logAndShow(apiError?.message)
    }

}
