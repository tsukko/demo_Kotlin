package jp.co.integrityworks.prototype.ui.contact

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import jp.co.integrityworks.prototype.R
import jp.co.integrityworks.prototype.databinding.EditPersonFragmentBinding
import jp.co.integrityworks.prototype.db.entity.Person
import jp.co.integrityworks.prototype.ui.base.BaseFragment
import jp.co.integrityworks.prototype.viewmodel.PersonListViewModel
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_in_case_of.*
import kotlinx.android.synthetic.main.edit_person_fragment.*

/**
 * プロフィール登録画面（家族情報の編集画面）
 *
 **/
class EditPersonInfoFragment : BaseFragment() {
    private var mBinding: EditPersonFragmentBinding? = null
    private var actionCreate = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.edit_person_fragment, container, false)

        // 新規作成時、getSerializableがnullとなる。代わりに登録用のDummyPersonを用意する
        mBinding?.person = arguments?.getSerializable("Person") as Person?
                ?: createDummyPerson()
        return mBinding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //タイトル名
        activity?.anyChanceToolbar?.title = getString(R.string.title_add_profile)

        // TODO もっとわかりやすく
        // 新規作成時も、DummyPerson（relationship = 1）を作っているので、何かしらの値が存在している
        // リストの最初（0）が relationship = 1 なので、-1 で合わせている
        if (mBinding?.person?.relationship != 0) {
            relationshipSpinner.setSelection(mBinding?.person?.relationship!! - 1)
        }

        // アクション定義
        //キャンセル
        personCancelTextEdit.setOnClickListener { fragmentManager?.popBackStack() }
        //保存
        personSaveButton.setOnClickListener { saveContact() }
    }

    private fun createDummyPerson(): Person {
        val dummy = Person()
        actionCreate = true
        return dummy
    }

    private fun saveContact() {
        val listener = DialogInterface.OnClickListener { _, _ -> saveData(mBinding?.person!!) }
        showSaveDialog(listener)
    }

    // TODO ここはViewModelへ写す
    private fun saveData(person: Person) {
        val sampleEntity = crateSaveData(person)
        val model: PersonListViewModel = ViewModelProviders.of(this).get(PersonListViewModel::class.java)
        Completable.fromAction {
            if (actionCreate) {
                model.insertPerson(sampleEntity)
            } else {
                model.updatePerson(sampleEntity)
            }
        }
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

    private fun crateSaveData(person: Person): Person {
        if (person.relationship == 0) {
            person.address1 = address1TextView.text.toString()
            person.address2 = address2TextView.text.toString()
            person.address3 = address3TextView.text.toString()
        } else {
            person.relationship = relationshipSpinner.selectedItemPosition + 1
        }
        person.nameKanji = personNameKanjiTextView.text.toString()
        person.nameKana = personNameKanaTextView.text.toString()
        person.birthday = personBirthdayTextView.text.toString()

        return person
    }
}
