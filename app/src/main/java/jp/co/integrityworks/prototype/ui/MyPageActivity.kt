package jp.co.integrityworks.prototype.ui

import android.Manifest.permission.CALL_PHONE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import jp.co.integrityworks.prototype.R
import jp.co.integrityworks.prototype.util.ConstantValue.Companion.REQUEST_READ_CONTACTS
import kotlinx.android.synthetic.main.activity_mypage.*

// ログイン後のマイページ
class MyPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        val sb = SpannableStringBuilder()
        sb.append(getString(R.string.app_title1))
        val start = sb.length
        sb.append(getString(R.string.app_title2))
        sb.setSpan(RelativeSizeSpan(0.7f), start, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        myPageTitleTextView.text = sb

        val prefs = getSharedPreferences(getString(R.string.preferences_key), MODE_PRIVATE)
        val name: String? = prefs.getString(getString(R.string.preferences_key_name), "none")
        nameTextView.text = getString(R.string.text_name_title, name)

        // TODO 画面遷移を考える
        confirmButtonLayout.setOnClickListener { goNextPage("InForceContractFragment") }
        inCaseButton.setOnClickListener { goNextPage("InCaseOfFragment") }
        importantContactButton.setOnClickListener { goNextPage("ImportantMessageFragment") }
        contactSettingButton.setOnClickListener { goNextPage("ContactInfoFragment") }
        emergencyContactButton.setOnClickListener { callEmergencyContact() }
    }

    private fun goNextPage(pageName: String) {
//        val cls = Class.forName(getPackageName() + "." + pageName)
//        val intent = Intent(application, cls)
        val intent = Intent(application, MainActivity::class.java)
        intent.putExtra("START_FRAGMENT", pageName)
        startActivity(intent)
    }

    private fun callEmergencyContact() {
        if (!mayRequestContacts()) {
            return
        }
        call()
    }

    private fun call() {
        try {
            // TODO dummy phone Number
            val phoneNumber = "+81-123-0000-9999"
            val uri = getString(R.string.text_call_number, phoneNumber)
            AlertDialog.Builder(this).apply {
                setMessage(getString(R.string.text_call_message, uri))
                setPositiveButton(android.R.string.ok) { _, _ ->
                    val intent = Intent(Intent.ACTION_CALL, Uri.parse(uri))
                    //音声発信はいきなり行わないで、電話番号を設定するだけでダイヤル画面へ遷移させる方法
                    //                    val i = Intent(Intent.ACTION_DIAL, uri)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
                setNegativeButton(android.R.string.cancel, null)
                show()
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    // 以下、パーミッション許可まわり
    private fun mayRequestContacts(): Boolean {
        if (checkSelfPermission(CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        if (shouldShowRequestPermissionRationale(CALL_PHONE)) {
            Log.i("debug", "This is sample log. shouldShowRequestPermissionRationale CALL_PHONE3")
        }
        requestPermissions(arrayOf(CALL_PHONE), REQUEST_READ_CONTACTS)
        return false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callEmergencyContact()
            } else {
                showWarningDialog()
            }
        }
    }

    private fun showWarningDialog() {
        if (shouldShowRequestPermissionRationale(CALL_PHONE)) {
            // 何もしない
        } else {
            AlertDialog.Builder(this)
                    .setTitle("パーミッション取得エラー")
                    .setMessage("今後は許可しないが選択されました。アプリ設定＞権限をチェックしてください（権限をON/OFFすることで状態はリセットされます）")
                    .setPositiveButton(android.R.string.ok) { _, _ -> openSettings() }
                    .setNegativeButton(android.R.string.cancel, null)
                    .create()
                    .show()
        }
    }

    private fun openSettings() {
        val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }
}
