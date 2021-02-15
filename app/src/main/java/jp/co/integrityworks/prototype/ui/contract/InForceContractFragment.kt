package jp.co.integrityworks.prototype.ui.contract

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import jp.co.integrityworks.prototype.R
import jp.co.integrityworks.prototype.databinding.InForceContractFragmentBinding
import jp.co.integrityworks.prototype.db.entity.Insurance
import jp.co.integrityworks.prototype.ui.base.BaseFragment
import jp.co.integrityworks.prototype.ui.insurance.InsuranceDetailFragment
import jp.co.integrityworks.prototype.viewmodel.InsuranceListViewModel
import kotlinx.android.synthetic.main.activity_in_case_of.*
import kotlinx.android.synthetic.main.in_force_contract_fragment.*

//あなたの保有契約画面（保有契約一覧画面）
class InForceContractFragment : BaseFragment() {
    private var mInForceContractAdapter: InForceContractAdapter? = null
    private var mBinding: InForceContractFragmentBinding? = null

    private val mInForceContractCallback = object : InForceContractCallback {
        override fun onClick(insurance: Insurance) {
            goContractDetailPage(insurance)
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.in_force_contract_fragment, container, false)

        mInForceContractAdapter = InForceContractAdapter(mInForceContractCallback)
        mBinding!!.inForceContractList.adapter = mInForceContractAdapter

        return mBinding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //タイトル名
        activity?.anyChanceToolbar?.title = getString(R.string.title_my_contract)

        val viewModel = ViewModelProviders.of(this).get(InsuranceListViewModel::class.java)
        subscribeUi(viewModel.insurances)

        //右下に設置された上に戻るボタン
        contractGoToTopImageView?.setOnClickListener { contractScrollView.smoothScrollTo(0, 0) }
        //スクロールが一番上にあるときは、戻るボタンは表示しない
        contractScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            when (scrollY) {
                0 -> {
                    contractGoToTopImageView?.isClickable = false
                    contractGoToTopImageView?.visibility = View.INVISIBLE
                }
                else -> {
                    contractGoToTopImageView?.isClickable = true
                    contractGoToTopImageView?.visibility = View.VISIBLE
                }
            }
        }

        addContractImageView2.setOnClickListener { goNextFragment(AddContractFragment()) }
    }

    //契約詳細
    private fun goContractDetailPage(insurance: Insurance) {
        val bundle = Bundle()
        bundle.putSerializable("Insurance", insurance)
        // TODO どうやって値を持つかは別途
        bundle.putString("KEY_CONTRACTOR_ID", "O000000001")
        val fragment = InsuranceDetailFragment()
        fragment.arguments = bundle
        goNextFragment(fragment)
    }

    private fun subscribeUi(liveData: LiveData<List<Insurance>>) {
        // Update the mList when the data changes
        liveData.observe(viewLifecycleOwner, Observer { myInsurances ->
            if (myInsurances != null) {
                mInForceContractAdapter!!.setContactList(myInsurances)
            }
            mBinding!!.executePendingBindings()
        })
    }
}
