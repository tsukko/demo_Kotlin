package jp.co.integrityworks.prototype.ui

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import kotlinx.android.synthetic.main.content_login.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class LoginActivityTest {

    private var mSenario: ActivityScenario<LoginActivity>? = null

    @Before
    fun setUp() {
        mSenario = ActivityScenario.launch(LoginActivity::class.java)
        mSenario?.moveToState(Lifecycle.State.CREATED)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun attemptLogin1() {
        mSenario?.onActivity { activity ->
            // 何もせずにログインボタンを押した状態
            activity.email_sign_in_button.performClick()
//TODO
//            assertThat()
        }
    }

    @Test
    fun attemptLogin2() {
        mSenario?.onActivity { activity ->
            // user id 未入力で弾かれる状態
            activity.password.setText("b")
            activity.email_sign_in_button.performClick()
//TODO
//            assertThat()
        }
    }

    @Test
    fun attemptLogin3() {
        mSenario?.onActivity { activity ->
            // ログインボタンを押した状態
            activity.userId.setText("a")
            activity.password.setText("b")
            activity.email_sign_in_button.performClick()
//TODO
//            assertThat()
        }
    }

    @Test
    fun goForgetInfo() {
        mSenario?.onActivity { activity ->
            // クロームで開くか確認ダイアログが表示される状態
            activity.forget_text.performClick()

//TODO 前面に出てくるFragmentからDialogを取得して、POSITIVEボタンを押して、Chromeが表示されていることを確認
//           val frg=  activity.supportFragmentManager.findFragmentById(0)
//
//            val sa :ShadowActivity = Shadows.shadowOf(activity)
//            val startIntent:Intent = sa.nextStartedActivity
//            val appName = startIntent?.component?.className
//            assertThat(appName).isEqualTo("com.google.android.apps.chrome.Main")
        }
    }
}