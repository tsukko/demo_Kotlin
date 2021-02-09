package jp.co.integrityworks.prototype.ui.creditcard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import jp.co.integrityworks.prototype.R
import jp.co.integrityworks.prototype.databinding.CreditCardDetailFragmentBinding
import jp.co.integrityworks.prototype.db.entity.Insurance
import jp.co.integrityworks.prototype.ui.base.BaseFragment
import kotlinx.android.synthetic.main.activity_in_case_of.*
import kotlinx.android.synthetic.main.credit_card_detail_fragment.*

//〇〇詳細画面
class CreditCardDetailFragment : BaseFragment() {

    private var mBinding: CreditCardDetailFragmentBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.credit_card_detail_fragment, container, false)
        mBinding?.insurance = arguments?.getSerializable("Insurance") as Insurance?

        return mBinding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //タイトル名
        activity?.anyChanceToolbar?.title = getString(R.string.title_creditcard)

        //右下に設置された上に戻るボタン
        cardDetailGoToTopImageView?.setOnClickListener { cardDetailScrollView.smoothScrollTo(0, 0) }
        //スクロールが一番上にあるときは、戻るボタンは表示しない
        cardDetailScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            when (scrollY) {
                0 -> {
                    cardDetailGoToTopImageView?.isClickable = false
                    cardDetailGoToTopImageView?.visibility = View.INVISIBLE
                }
                else -> {
                    cardDetailGoToTopImageView?.isClickable = true
                    cardDetailGoToTopImageView?.visibility = View.VISIBLE
                }
            }
        }

//キャンセル
        //写真の追加
        //写真の削除
        //上記内容で保存
    }

}
