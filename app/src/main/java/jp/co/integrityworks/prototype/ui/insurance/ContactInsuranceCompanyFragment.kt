package jp.co.integrityworks.prototype.ui.insurance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import jp.co.integrityworks.prototype.R
import jp.co.integrityworks.prototype.databinding.ContactInsuranceCompanyFragmentBinding
import jp.co.integrityworks.prototype.ui.InquiriesFragment
import jp.co.integrityworks.prototype.ui.base.BaseFragment
import jp.co.integrityworks.prototype.ui.contract.AddContractFragment
import kotlinx.android.synthetic.main.activity_in_case_of.*
import kotlinx.android.synthetic.main.contact_insurance_company_fragment.*

//保〇〇会社へのご連絡画面
class ContactInsuranceCompanyFragment : BaseFragment() {
    private var mBinding: ContactInsuranceCompanyFragmentBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.contact_insurance_company_fragment, container, false)
        return mBinding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //タイトル名
        activity?.anyChanceToolbar?.title = getString(R.string.title_contact_company)

        //担当者へ発信
        telStaffImageView.setOnClickListener { goNextFragment(AddContractFragment()) }
        //担当者へメール
        mailStaffImageView.setOnClickListener { goNextFragment(InquiriesFragment()) }
        //wwwサイトを開く
        openSiteImageView.setOnClickListener { goNextFragment(InquiriesFragment()) }
        //アプリを開く
        openAppImageView.setOnClickListener { goNextFragment(InquiriesFragment()) }
    }
}
