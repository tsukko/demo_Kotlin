package jp.co.integrityworks.prototype.ui

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import jp.co.integrityworks.prototype.AppExecutors
import jp.co.integrityworks.prototype.R
import jp.co.integrityworks.prototype.db.AppDatabase
import jp.co.integrityworks.prototype.ui.contract.AddContractFragment
import jp.co.integrityworks.prototype.ui.contract.InForceContractFragment
import kotlinx.android.synthetic.main.activity_in_case_of.*

/**
 * マイページ画面から、保有契約やもしもの場合の画面へ遷移した際の
 * ベースとなるActivity
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_case_of)

        anyChanceToolbar.title = getString(R.string.title_activity_in_case_of)
        // オプションメニューの表示
        setSupportActionBar(anyChanceToolbar)
        // 戻るボタンの表示
        // TODO 戻る遷移の仕様があいまいなままにしている
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(true)
        }

        // 下部のナビゲージョンのアイテムを選択した時の挙動の定義
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener)

        // 入力するときなど、キーボードが表示された際に、下部のナビケージョンバーを非表示にする
        setKeyboardVisibilityListener(object : OnKeyboardVisibilityListener {
            override fun onVisibilityChanged(visible: Boolean) {
                bottomNavigation.visibility = if (visible) View.GONE else View.VISIBLE
            }
        })

        startFragment()
    }

    //TODO もっと簡潔にできそう。
    // onResumeで呼ぶとカメラやギャラリーアプリから戻ってきたときに表示させるFragmentを設定してあげる必要がある。
    private fun startFragment() {
        when (intent.getStringExtra("START_FRAGMENT")) {
            "InForceContractFragment" -> {
                // "契約内容を確認する"ボタンを押した場合
                bottomNavigation.menu.getItem(1).isChecked = true
                supportFragmentManager
                        .beginTransaction()
                        .add(R.id.my_nav_host_fragment, InForceContractFragment())
                        .commitAllowingStateLoss()
            }
            "InCaseOfFragment" -> {
                // "もしもの場合"ボタンを押した場合
                // TODO navigationで自動で開くようにしている(startDestination)けど、見直したい。
                // 他の画面に開いた時も、この画面の上に
                //なので従来形式に戻す
            }
            "ImportantMessageFragment" -> {
                //"重要なご連絡"ボタンを押した場合
                //TODO 未定義
            }
            "ContactInfoFragment" -> {
                // "ご連絡先の設定"ボタンを押した場合
                supportFragmentManager.beginTransaction()
                        .add(R.id.my_nav_host_fragment, InForceContractFragment())
                        .commitAllowingStateLoss()
            }
            else -> {
                println("else")
            }
        }
    }

    //ナビケーションアイテムを選択した際の挙動
    private var navigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navigationHome -> {
//                        my_nav_host_fragment.view?.let {
//                            Navigation.findNavController(it).navigate(R.id.action_to_incaseof)
//                        }
                        // TODO 正しい遷移か不明
                        val intent = Intent(application, MyPageActivity::class.java)
                        startActivity(intent)
                        finish()
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigationContract -> {
                        my_nav_host_fragment.view?.let {
                            Navigation.findNavController(it).navigate(R.id.action_to_contract)
                        }
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigationNotice -> {
                        my_nav_host_fragment.view?.let {
                            Navigation.findNavController(it).navigate(R.id.action_to_notice)
                        }
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigationHelp -> {
                        my_nav_host_fragment.view?.let {
                            Navigation.findNavController(it).navigate(R.id.action_to_help)
                        }
                        return@OnNavigationItemSelectedListener true
                    }
                }
                false
            }

    //オプションメニューのユーザ名を設定
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        val prefs = getSharedPreferences(getString(R.string.preferences_key), MODE_PRIVATE)
        val name: String? = prefs.getString(getString(R.string.preferences_key_name), "none")
        val menu0 = menu.findItem(R.id.action_none) as MenuItem
        menu0.title = getString(R.string.text_name_title, name)
        return true
    }

    //オプションメニューを選択した時の挙動
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings2 -> displayFragment(InForceContractFragment())
            R.id.action_settings3 -> displayFragment(AddContractFragment())
            R.id.action_settings4 -> displayFragment(HomeFragment())
            // TODO その他、各オプションメニュー のアクション
            R.id.action_settings5 -> { //プロフィールを追加する
            }
            R.id.action_settings6 -> { //通知先設定
            }
            R.id.action_logout -> actionLogout()
            android.R.id.home -> {
                // 一つ前のFragmentに戻るか、最後のFragmentの場合はトップページ（マイページ）画面に戻る
                if (supportFragmentManager.backStackEntryCount != 0) {
                    supportFragmentManager.popBackStack()
                    return true
                }
            }
        }
        return false
    }

    private fun displayFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.my_nav_host_fragment, fragment)
                .commitAllowingStateLoss()
    }

    //ログアウト処理
    private fun actionLogout() {
        AlertDialog.Builder(this).apply {
            setMessage(getString(R.string.message_logout))
            setPositiveButton(getString(android.R.string.ok)) { _, _ ->
                val prefs = getSharedPreferences(getString(R.string.preferences_key), MODE_PRIVATE)
                prefs.edit { clear() }

                // DBの再生性（Applicationを再起動する必要がある）
                if (context.getDatabasePath(AppDatabase.DATABASE_NAME).exists()) {
                    context.deleteDatabase(AppDatabase.DATABASE_NAME)
                }
                AppDatabase.getInstance(context, AppExecutors())

                // ログイン前画面に戻る
                val intent = Intent(application, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            setNegativeButton(getString(android.R.string.cancel), null)
            show()
        }
    }

    //TODO 設定値なども見直して、別クラスで実装する
    // 入力するときなど、キーボードが表示された際に、下部のナビケージョンバーを非表示にする
    interface OnKeyboardVisibilityListener {
        fun onVisibilityChanged(visible: Boolean)
    }

    private fun setKeyboardVisibilityListener(onKeyboardVisibilityListener: OnKeyboardVisibilityListener) {
        my_nav_host_fragment.view?.viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            private var alreadyOpen: Boolean = false
            private val defaultKeyboardHeightDP = 100
            private val EstimatedKeyboardDP = defaultKeyboardHeightDP + 48
            private val rect = Rect()
            override fun onGlobalLayout() {
                val estimatedKeyboardHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        EstimatedKeyboardDP.toFloat(),
                        my_nav_host_fragment.resources.displayMetrics).toInt()
                my_nav_host_fragment.view?.getWindowVisibleDisplayFrame(rect)
                if (my_nav_host_fragment.view?.rootView?.height != null) {
                    val heightDiff = my_nav_host_fragment.requireView().rootView.height - (rect.bottom - rect.top)
                    val isShown = heightDiff >= estimatedKeyboardHeight
                    if (isShown == alreadyOpen) {
                        Log.i("Keyboard state", "Ignoring global layout change...")
                        return
                    }
                    alreadyOpen = isShown
                    onKeyboardVisibilityListener.onVisibilityChanged(isShown)
                }
            }
        })
    }
}
