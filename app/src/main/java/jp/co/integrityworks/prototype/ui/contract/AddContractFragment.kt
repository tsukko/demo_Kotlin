package jp.co.integrityworks.prototype.ui.contract

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import jp.co.integrityworks.prototype.R
import jp.co.integrityworks.prototype.databinding.AddContractFragmentBinding
import jp.co.integrityworks.prototype.ui.base.BaseFragment
import jp.co.integrityworks.prototype.ui.creditcard.CreditCardDetailFragment
import jp.co.integrityworks.prototype.ui.insurance.SelectInsuranceTypeFragment
import jp.co.integrityworks.prototype.ui.warranty.WarrantyDetailFragment
import kotlinx.android.synthetic.main.activity_in_case_of.*
import kotlinx.android.synthetic.main.add_contract_fragment.*

//契約を追加する画面
class AddContractFragment : BaseFragment() {
    private var mBinding: AddContractFragmentBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.add_contract_fragment, container, false)
        return mBinding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //タイトル名
        activity?.anyChanceToolbar?.title = getString(R.string.title_add_contract)

        //〇〇契約を登録
        addContractTextView.setOnClickListener { goNextFragment(SelectInsuranceTypeFragment()) }
        //クレカを登録
        addCardTextView.setOnClickListener { goNextFragment(CreditCardDetailFragment()) }
        //ワランティを登録
        addWarrantyTextView.setOnClickListener { goNextFragment(WarrantyDetailFragment()) }
    }
}
