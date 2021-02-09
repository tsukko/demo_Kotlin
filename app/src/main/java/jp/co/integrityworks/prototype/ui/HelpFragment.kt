package jp.co.integrityworks.prototype.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.co.integrityworks.prototype.R
import jp.co.integrityworks.prototype.ui.base.BaseFragment
import kotlinx.android.synthetic.main.activity_in_case_of.*

// ヘルプ画面（定義なし）
class HelpFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_help, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //タイトル名
        activity?.anyChanceToolbar?.title = getString(R.string.title_help)
    }
}
