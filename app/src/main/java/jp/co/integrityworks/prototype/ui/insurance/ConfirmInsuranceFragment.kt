package jp.co.integrityworks.prototype.ui.insurance

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.transaction
import androidx.lifecycle.ViewModelProviders
import jp.co.integrityworks.prototype.R
import jp.co.integrityworks.prototype.databinding.ConfirmInsuranceFragmentBinding
import jp.co.integrityworks.prototype.db.entity.Insurance
import jp.co.integrityworks.prototype.ui.base.BaseFragment
import jp.co.integrityworks.prototype.ui.contract.InForceContractFragment
import jp.co.integrityworks.prototype.viewmodel.InsuranceListViewModel
import kotlinx.android.synthetic.main.activity_in_case_of.*
import kotlinx.android.synthetic.main.confirm_insurance_fragment.*

//〇〇を追加する画面
// ステップ３
// 確認画面
class ConfirmInsuranceFragment : BaseFragment() {

    private var mBinding: ConfirmInsuranceFragmentBinding? = null
    private var mInsuranceType: String? = null
    private var mPicturePath: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.confirm_insurance_fragment, container, false)

        mBinding?.insuranceCompany = arguments?.getString("InsuranceCompany")
        mBinding?.policyNumber = arguments?.getString("PolicyNumber")
        mPicturePath = arguments?.getString("PicFileName")
        mInsuranceType = arguments?.getString("InsuranceType")

        return mBinding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //タイトル名
        activity?.anyChanceToolbar?.title = getString(R.string.title_insurance_list)

        //次へ
        confirmAddButton?.setOnClickListener { goNextPage() }
        //戻る
        confirmBackButton?.setOnClickListener { fragmentManager?.popBackStack() }
    }

    //契約を追加する画面
    private fun goNextPage() {
        val listener = DialogInterface.OnClickListener { _, _ ->
            addData()
            fragmentManager?.transaction(allowStateLoss = true) {
                //TODO 適切な手法を考える
                //ステップ3から戻る
                fragmentManager?.popBackStack()
                //ステップ2から戻る
                fragmentManager?.popBackStack()
                //ステップ1から戻る
                fragmentManager?.popBackStack()
                //契約追加画面から戻る
                fragmentManager?.popBackStack()
                replace(R.id.my_nav_host_fragment, InForceContractFragment())
            }
        }
        showRegisterDialog(listener)
    }

    private fun addData() {
        val sampleEntity = Insurance(
                accidentTypeId = 1,
                policyInfoNo = "policyInfoNo",
                insuranceCompanyCode = "insuranceCompanyCode",
                insuranceCompanyName = companyNameView.text.toString(),
                insuranceTypeCode = mInsuranceType!!,
                insuranceTypeName = typeNameView.text.toString(),
                insuranceTradeName = tradeNameView.text.toString(),
                policyNumber = policyNumberView.text.toString(),
                policyNumberBranch = "policyNumberBranch",
                maturityDate = maturityDateView.text.toString(),
                carName = "アルフォード",
                registNumber = registerNumberView.text.toString()
        )
        val viewModel = ViewModelProviders.of(this).get(InsuranceListViewModel::class.java)
        viewModel.addInsurance(sampleEntity)
    }
}

