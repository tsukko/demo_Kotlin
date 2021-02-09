package jp.co.integrityworks.prototype.ui.contact

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import jp.co.integrityworks.prototype.R
import jp.co.integrityworks.prototype.databinding.EditContactFragmentBinding
import jp.co.integrityworks.prototype.db.entity.Person
import jp.co.integrityworks.prototype.ui.base.BaseFragment
import jp.co.integrityworks.prototype.viewmodel.PersonListViewModel
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_in_case_of.*
import kotlinx.android.synthetic.main.edit_contact_fragment.*

/**
 * ご連絡先登録画面
 * ご連絡先設定画面のご連絡先の編集を行うための画面
 *
 **/
class EditContactFragment : BaseFragment() {

    private var mBinding: EditContactFragmentBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.edit_contact_fragment, container, false)
        // TODO 登録画面の仕様が未決定のため、追加の時の画面は暫定
        mBinding?.person = arguments?.getSerializable("Person") as Person

        return mBinding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //タイトル名
        activity?.anyChanceToolbar?.title = getString(R.string.title_contact_adding)

        // アクション定義
        //キャンセル
        cancelContactTextEdit.setOnClickListener { fragmentManager?.popBackStack() }
        //保存
        saveContactButton.setOnClickListener { saveContact() }
    }

    //
    private fun saveContact() {
        val listener = DialogInterface.OnClickListener { _, _ ->
            saveData(mBinding?.person!!)
            fragmentManager?.popBackStack()
        }
        showSaveDialog(listener)
    }

    private fun saveData(person: Person) {
        person.tel = telTextView.text.toString()
        person.sms = smsTextView.text.toString()
        person.pcMail = pcMailTextView.text.toString()
        person.eMail = emailTextView.text.toString()

        val model: PersonListViewModel = ViewModelProviders.of(this).get(PersonListViewModel::class.java)
        Completable.fromAction { model.updatePerson(person) }
                .subscribeOn(Schedulers.io())
                .subscribe(object : CompletableObserver {
                    override fun onComplete() {
                        fragmentManager?.popBackStack()
                    }

                    override fun onSubscribe(d: Disposable) {
                        //fragmentManager?.popBackStack()
                    }

                    override fun onError(e: Throwable) {
                        //TODO エラーが発生しました
                        //fragmentManager?.popBackStack()
                    }
                })
    }
}
