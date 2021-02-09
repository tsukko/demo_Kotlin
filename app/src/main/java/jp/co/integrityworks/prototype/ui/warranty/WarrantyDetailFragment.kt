package jp.co.integrityworks.prototype.ui.warranty

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import jp.co.integrityworks.prototype.R
import jp.co.integrityworks.prototype.databinding.WarrantyDetailFragmentBinding
import jp.co.integrityworks.prototype.db.entity.Insurance
import jp.co.integrityworks.prototype.ui.base.BaseFragment
import kotlinx.android.synthetic.main.activity_in_case_of.*
import kotlinx.android.synthetic.main.warranty_detail_fragment.*

//〇〇詳細画面
class WarrantyDetailFragment : BaseFragment() {

    private var mBinding: WarrantyDetailFragmentBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.warranty_detail_fragment, container, false)
        mBinding?.insurance = arguments?.getSerializable("Insurance") as Insurance?

        return mBinding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //タイトル名
        activity?.anyChanceToolbar?.title = getString(R.string.title_warranty)

        //右下に設置された上に戻るボタン
        warrantyDetailGoToTopImageView?.setOnClickListener { warrantyDetailScrollView.smoothScrollTo(0, 0) }
        //スクロールが一番上にあるときは、戻るボタンは表示しない
        warrantyDetailScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            when (scrollY) {
                0 -> {
                    warrantyDetailGoToTopImageView?.isClickable = false
                    warrantyDetailGoToTopImageView?.visibility = View.INVISIBLE
                }
                else -> {
                    warrantyDetailGoToTopImageView?.isClickable = true
                    warrantyDetailGoToTopImageView?.visibility = View.VISIBLE
                }
            }
        }

//キャンセル
        //写真の追加
        //写真の削除
        //上記内容で保存
    }

}
