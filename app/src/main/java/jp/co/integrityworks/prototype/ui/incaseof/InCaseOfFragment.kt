package jp.co.integrityworks.prototype.ui.incaseof

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import jp.co.integrityworks.prototype.R
import jp.co.integrityworks.prototype.databinding.InCaseOfFragmentBinding
import jp.co.integrityworks.prototype.db.entity.AccidentType
import jp.co.integrityworks.prototype.ui.base.BaseFragment
import jp.co.integrityworks.prototype.ui.insurance.InsuranceDetailFragment
import jp.co.integrityworks.prototype.viewmodel.AccidentTypeViewModel
import kotlinx.android.synthetic.main.activity_in_case_of.*
import kotlinx.android.synthetic.main.in_case_of_item.*

// もしもの場合画面
class InCaseOfFragment : BaseFragment() {
    private var mBinding: InCaseOfFragmentBinding? = null

    private val mDetailClickCallback = object : InCaseOfClickCallback {
        override fun onClick(accidentType: AccidentType) {
            val model = mBinding!!.accidentTypeViewModel
            model?.setVisible(accidentType)
            mBinding!!.accidentTypeViewModel = model
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.in_case_of_fragment, container, false)
        mBinding!!.callback = mDetailClickCallback

        return mBinding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //タイトル
        activity?.anyChanceToolbar?.title = getString(R.string.title_activity_in_case_of)

        // val factory = AccidentTypeViewModel.Factory(activity!!.application)
        val model = ViewModelProviders.of(this).get(AccidentTypeViewModel::class.java)
        mBinding!!.accidentTypeViewModel = model

        in_case_of_contract_overview?.setOnClickListener { goNextFragment(InsuranceDetailFragment()) }
    }
}
