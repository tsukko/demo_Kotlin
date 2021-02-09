package jp.co.integrityworks.prototype.ui.contact

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import jp.co.integrityworks.prototype.R
import jp.co.integrityworks.prototype.databinding.ContactSettingFragmentBinding
import jp.co.integrityworks.prototype.db.entity.Person
import jp.co.integrityworks.prototype.ui.base.BaseFragment
import jp.co.integrityworks.prototype.viewmodel.PersonListViewModel
import kotlinx.android.synthetic.main.activity_in_case_of.*
import kotlinx.android.synthetic.main.contact_setting_contract_area.*
import kotlinx.android.synthetic.main.contact_setting_fragment.*

/** ご連絡先設定画面
 * ご連絡先画面から遷移する
 *
 */
class ContactSettingFragment : BaseFragment() {
    private var mBinding: ContactSettingFragmentBinding? = null
    private var mModel: PersonListViewModel? = null

    private val mContactSettingClickCallback = object : ContactSettingClickCallback {
        override fun onClickDel(person: Person) {
            goDelFamilyPage(person)
        }

        override fun onClickEdit(person: Person) {
            goEditPersonPage(person)
        }

        override fun onClickEditContact(person: Person) {
            goEditContactPage(person)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.contact_setting_fragment, container, false)
        mBinding?.person = arguments?.getSerializable("Person") as Person
        mBinding?.callback = mContactSettingClickCallback
        return mBinding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //タイトル名
        activity?.anyChanceToolbar?.title = getString(R.string.title_contact_setting)

        mModel = ViewModelProviders.of(this).get(PersonListViewModel::class.java)
        subscribeUi(mModel?.persons!!)

        //アクション定義
        contactSettingGoToTopImageView?.setOnClickListener { contactSettingScrollView.smoothScrollTo(0, 0) }
        contactSettingScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            when (scrollY) {
                0 -> {
                    contactSettingGoToTopImageView?.isClickable = false
                    contactSettingGoToTopImageView?.visibility = View.INVISIBLE
                }
                else -> {
                    contactSettingGoToTopImageView?.isClickable = true
                    contactSettingGoToTopImageView?.visibility = View.VISIBLE
                }
            }
        }

        //ご連絡方法のチェックがない場合は非表示にする
        contactSettingShowContactSwitch?.setOnCheckedChangeListener { _, isChecked -> detailContactArea.visibility = if (isChecked) View.VISIBLE else View.GONE }
    }

    //本人、家族情報編集画面
    private fun goEditPersonPage(person: Person) {
        val bundle = Bundle()
        bundle.putSerializable("Person", person)
        val fragment = EditPersonInfoFragment()
        fragment.arguments = bundle
        goNextFragment(fragment)
    }

    //家族情報削除画面
    private fun goDelFamilyPage(person: Person) {
        val listener = DialogInterface.OnClickListener { _, _ ->
            mModel?.deletePerson(person)
            parentFragmentManager.popBackStack()
        }
        showDelDialog(listener)
    }

    //連絡先編集画面（共通）
    private fun goEditContactPage(person: Person) {
        val bundle = Bundle()
        bundle.putSerializable("Person", person)
        val fragment = EditContactFragment()
        fragment.arguments = bundle
        goNextFragment(fragment)
    }

    // ViewModelでPersonが編集されたときにに反映できるようにする
    private fun subscribeUi(liveData: LiveData<List<Person>>) {
        liveData.observe(viewLifecycleOwner, Observer { myList ->
            if (myList != null) {
                // TODO personsの中から、選択されたpersonを更新したい。設計がよくない
                (myList.indices).forEach {
                    if (myList[it].id == mBinding?.person?.id) {
                        mBinding?.person = myList[it]
                        return@forEach
                    }
                }
            }
        })
    }
}
