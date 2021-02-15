package jp.co.integrityworks.prototype.ui.insurance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import jp.co.integrityworks.prototype.R
import jp.co.integrityworks.prototype.databinding.SelectInsuranceTypeFragmentBinding
import jp.co.integrityworks.prototype.ui.base.BaseFragment
import kotlinx.android.synthetic.main.activity_in_case_of.*
import kotlinx.android.synthetic.main.select_insurance_type_fragment.*

//〇〇を追加する画面
//ステップ１
//〇〇種別を選択する画面（現状、自動車〇〇のみ）
class SelectInsuranceTypeFragment : BaseFragment() {
    private var mBinding: SelectInsuranceTypeFragmentBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.select_insurance_type_fragment, container, false)
        return mBinding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //タイトル名
        activity?.anyChanceToolbar?.title = getString(R.string.title_insurance_list)

        //〇〇契約を追加
        addCarInsuranceView.setOnClickListener { goNextPage("automobile") }
        addFireInsurance.setOnClickListener { goNextPage("house") }
        addLifeInsurance.setOnClickListener { goNextPage("life") }
        addAccidentInsurance.setOnClickListener { goNextPage("accident") }
        addEtcInsurance.setOnClickListener { goNextPage("etc") }
    }

    private fun goNextPage(type: String) {
        val bundle = Bundle()
        bundle.putString("InsuranceType", type)
        val fragment = SelectInsuranceCompanyFragment()
        fragment.arguments = bundle
        goNextFragment(fragment)
    }
}
