package jp.co.integrityworks.prototype.ui.contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import jp.co.integrityworks.prototype.R
import jp.co.integrityworks.prototype.databinding.ContactListFragmentBinding
import jp.co.integrityworks.prototype.db.entity.Person
import jp.co.integrityworks.prototype.ui.base.BaseFragment
import jp.co.integrityworks.prototype.viewmodel.PersonListViewModel
import kotlinx.android.synthetic.main.activity_in_case_of.*
import kotlinx.android.synthetic.main.contact_list_fragment.*


/** ご連絡先画面
 * マイページ画面（ホーム画面？）から遷移する
 * ご連絡先画面（ContactInfoFragment）からは、ご連絡設定画面（ContactSettingFragment）に遷移する
 */
class ContactInfoFragment : BaseFragment() {

    private var mContactAdapter: ContactAdapter? = null

    private var mBinding: ContactListFragmentBinding? = null

    private val mContactInfoClickCallback = object : ContactInfoCallback {
        override fun onClick(person: Person) {
            goNextPage(person)
        }

        override fun onSettingRelationship(relationship: Int): String {
            return when (relationship) {
                0 -> getString(R.string.text_contractor)
                1 -> getString(R.string.text_relationship_wife)
                2 -> getString(R.string.text_relationship_child)
                3 -> getString(R.string.text_relationship_parent)
                4 -> getString(R.string.text_relationship_etc)
                else -> getString(R.string.text_none)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.contact_list_fragment, container, false)

        mContactAdapter = ContactAdapter(mContactInfoClickCallback)
        mBinding!!.contactList.adapter = mContactAdapter

        return mBinding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModel = ViewModelProviders.of(this).get(PersonListViewModel::class.java)
        subscribeUi(viewModel.persons)

        //タイトル名
        activity?.anyChanceToolbar?.title = getString(R.string.title_contact)

        // アクション定義
        addContactImageView.setOnClickListener { goNextFragment(EditPersonInfoFragment()) }
        //右下に設置された上に戻るボタン
        contactInfoGoToTopImageView?.setOnClickListener { contactInfoScrollView.smoothScrollTo(0, 0) }
        //スクロールが一番上にあるときは、戻るボタンは表示しない
        contactInfoScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            when (scrollY) {
                0 -> {
                    contactInfoGoToTopImageView?.isClickable = false
                    contactInfoGoToTopImageView?.visibility = View.INVISIBLE
                }
                else -> {
                    contactInfoGoToTopImageView?.isClickable = true
                    contactInfoGoToTopImageView?.visibility = View.VISIBLE
                }
            }
        }
    }

    // プロフィール編集画面
    private fun goNextPage(person: Person) {
        val bundle = Bundle()
        bundle.putSerializable("Person", person)
        val fragment = ContactSettingFragment()
        fragment.arguments = bundle
        goNextFragment(fragment)
    }

    private fun subscribeUi(liveData: LiveData<List<Person>>) {
        liveData.observe(viewLifecycleOwner, Observer { myPersons ->
            if (myPersons != null) {
                mContactAdapter!!.setContactList(myPersons)
            }
            mBinding!!.executePendingBindings()
        })
    }
}
