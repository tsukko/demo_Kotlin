package jp.co.integrityworks.prototype.ui

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    private var mSenario:ActivityScenario<MainActivity>? = null

    @Before
    fun setUp() {
        mSenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun cerate() {
        mSenario?.moveToState(Lifecycle.State.CREATED)
        mSenario?.onActivity { activity ->
            }
    }
}