package jp.co.integrityworks.prototype.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Paint
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.style.RelativeSizeSpan
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import jp.co.integrityworks.prototype.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.content_login.*

// ログイン画面
class LoginActivity : AppCompatActivity() {

    private var mAuthTask: UserLoginTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 既にログイン済みの場合、この画面を飛ばす
        val prefs = getSharedPreferences(getString(R.string.preferences_key), MODE_PRIVATE)
        if (prefs.getString(getString(R.string.preferences_key_id), null) != null) {
            val intent = Intent(application, MyPageActivity::class.java)
            startActivity(intent)
            finish()
        }

        val sb = SpannableStringBuilder()
        sb.append(getString(R.string.app_title1))
        val start = sb.length
        sb.append(getString(R.string.app_title2))
        sb.setSpan(RelativeSizeSpan(0.7f), start, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        loginTitleTextView.text = sb

        // kotlin nativeのサンプル
//        loginTitleTextView.text = createApplicationScreenMessage()

        // passwordのEditText への Enter キー入力を検知して処理を行う
        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        email_sign_in_button.setOnClickListener { attemptLogin() }

        //text文字列をリンクのように表示
        forget_text.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        forget_text.setOnClickListener { goForgetInfo() }
    }

    private fun attemptLogin() {
        if (mAuthTask != null) {
            return
        }

        // Reset errors.
        userId.error = null
        password.error = null

        // Store values at the time of the login attempt.
        val userIdStr = userId.text.toString()
        val passwordStr = password.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(passwordStr) || !isPasswordValid(passwordStr)) {
            password.error = getString(R.string.error_invalid_password)
            focusView = password
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(userIdStr)) {
            userId.error = getString(R.string.error_field_required)
            focusView = userId
            cancel = true
        } else if (!isUserIdValid(userIdStr)) {
            userId.error = getString(R.string.error_invalid_email)
            focusView = userId
            cancel = true
        }

        if (cancel) {
            focusView?.requestFocus()
        } else {
            showProgress(true)
            mAuthTask = UserLoginTask(userIdStr, passwordStr)
            mAuthTask!!.execute(null as Void?)
        }

    }

    private fun isUserIdValid(userId: String): Boolean {
        //TODO Validの内容を定義する
//        return userId.contains("@")
        return true
    }

    private fun isPasswordValid(password: String): Boolean {
        //TODO Validの内容を定義する
//        return password.length > 4
        return true
    }

    private fun showProgress(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        login_progress.visibility = if (show) View.VISIBLE else View.GONE
        // TODO ここで行うと次画面に遷移する寸前にボタンが推せるようになってしまう。
        setFormEnable(!show)
        login_progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        login_progress.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })
    }

    private fun setFormEnable(isEnabled: Boolean) {
        userId.isEnabled = isEnabled
        password.isEnabled = isEnabled
        email_sign_in_button.isEnabled = isEnabled
        forget_text.isEnabled = isEnabled
        sns_fb_button.isEnabled = isEnabled
        sns_tw_button.isEnabled = isEnabled
        sns_li_button.isEnabled = isEnabled
    }

    private fun goForgetInfo() {
        AlertDialog.Builder(this).apply {
            setMessage("Chromeで開きます")
            setPositiveButton(getString(android.R.string.ok)) { _, _ ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://localhost:3000"))
                intent.setClassName("com.android.chrome", "com.google.android.apps.chrome.Main")
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            setNegativeButton(getString(android.R.string.cancel), null)
            show()
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class UserLoginTask internal constructor(private val id: String, private val pass: String) : AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg params: Void): Boolean {
            // TODO: ログインチェックはAPIで行う
            try {
                // Simulate network access.
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                return false
            }

            return true
        }

        override fun onPostExecute(success: Boolean) {
            mAuthTask = null
            showProgress(false)

            if (success) {
                val prefs = getSharedPreferences(getString(R.string.preferences_key), MODE_PRIVATE)
                val e: SharedPreferences.Editor = prefs.edit()
                e.putString(getString(R.string.preferences_key_id), userId.toString())
                // TODO APIで返ってきたユーザ情報を取ってくる
                e.putString(getString(R.string.preferences_key_name), getString(R.string.test_name))
                e.apply()

                val intent = Intent(application, MyPageActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                password.error = getString(R.string.error_incorrect_password)
                password.requestFocus()
            }
        }

        override fun onCancelled() {
            mAuthTask = null
            showProgress(false)
        }
    }
}
