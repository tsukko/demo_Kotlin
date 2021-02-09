package jp.co.integrityworks.prototype.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import jp.co.integrityworks.prototype.R
import jp.co.integrityworks.prototype.db.entity.Person
import jp.co.integrityworks.prototype.ui.base.BaseFragment
import jp.co.integrityworks.prototype.ui.contact.EditPersonInfoFragment
import jp.co.integrityworks.prototype.ui.contract.AddContractFragment
import jp.co.integrityworks.prototype.viewmodel.PersonListViewModel
import kotlinx.android.synthetic.main.activity_in_case_of.*
import kotlinx.android.synthetic.main.fragment_home.*

//TODO マイページ設定画面（ホーム？）
class HomeFragment : BaseFragment() {
    private var mPerson: Person? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel = ViewModelProviders.of(this).get(PersonListViewModel::class.java)
        subscribeUi(viewModel.persons)

        //タイトル名
        activity?.anyChanceToolbar?.title = getString(R.string.title_my_page_setting)

        //アクション定義
        //プロフィール登録
        profileRegistrationLinearLayout.setOnClickListener { goProfileRegistrationPage() }
        //通知先設定
        settingNoticeLinearLayout.setOnClickListener { goSettingNoticePage() }
        //契約追加
        homeAddContractLinearLayout.setOnClickListener { goNextFragment(AddContractFragment()) }
    }

    private fun goProfileRegistrationPage() {
        val bundle = Bundle()
        bundle.putSerializable("Person", mPerson)
        val fragment = EditPersonInfoFragment()
        fragment.arguments = bundle
        goNextFragment(fragment)
    }

    //TODO
    private fun goSettingNoticePage() {
    }

    private fun subscribeUi(liveData: LiveData<List<Person>>) {
        // Update the mList when the data changes
        liveData.observe(viewLifecycleOwner, Observer { myList ->
            if (myList != null) {
                mPerson = myList[0]
            }
        })
    }
}
