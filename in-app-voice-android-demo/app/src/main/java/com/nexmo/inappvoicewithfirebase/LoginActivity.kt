package com.nexmo.inappvoicewithfirebase

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import com.nexmo.inappvoicewithfirebase.networking.UserJWT
import com.nexmo.inappvoicewithfirebase.networking.retrofitService
import com.nexmo.sdk.conversation.client.ConversationClient
import com.nexmo.sdk.conversation.client.User
import com.nexmo.sdk.conversation.client.event.NexmoAPIError
import com.nexmo.sdk.conversation.client.event.RequestHandler
import com.nexmo.inappvoicewithfirebase.utils.Stitch
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : BaseActivity(), RequestHandler<User>, Callback<UserJWT> {
    private lateinit var client: ConversationClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        client = Stitch.getInstance(this).conversationClient

        loginBtn.setOnClickListener {
            login()
        }

    }

    private fun login() {
        showProgress(true)
        retrofitService.getJWT().enqueue(this)
    }

    private fun goToCallActivity() {
        showProgress(true)
        startActivity(
                Intent(this, CallActivity::class.java)
        )
    }

    private fun showProgress(visible: Boolean) {
        val visibility = if (visible) View.VISIBLE else View.INVISIBLE
        runOnUiThread {
            progressBar.visibility = visibility
        }
    }

    override fun onError(apiError: NexmoAPIError?) {
        showProgress(false)
        logAndShow(apiError?.message)
    }

    override fun onSuccess(result: User?) {
        goToCallActivity()
    }

    override fun onResponse(call: Call<UserJWT>?, response: Response<UserJWT>?) {
        val jwt = response?.body()?.user_jwt
        client.login(jwt, this)
    }

    override fun onFailure(call: Call<UserJWT>?, t: Throwable?) {
        showProgress(false)
        logAndShow(t?.message)
    }


}
