package jp.co.integrityworks.prototype.ui.base

import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import jp.co.integrityworks.prototype.R

open class BaseFragment : Fragment() {
    fun goNextFragment(nextFragment: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()
                ?.addToBackStack(null)
                ?.replace(R.id.my_nav_host_fragment, nextFragment)
                ?.commitAllowingStateLoss()
    }

    fun showRegisterDialog(listener: DialogInterface.OnClickListener) {
        showDialog(getString(R.string.text_confirm_registration), listener)
    }

    fun showSaveDialog(listener: DialogInterface.OnClickListener) {
        showDialog(getString(R.string.text_confirm_save), listener)
    }

    fun showDelDialog(listener: DialogInterface.OnClickListener) {
        showDialog(getString(R.string.text_confirm_delete), listener)
    }

    fun showDialog(msg: String, listener: DialogInterface.OnClickListener) {
        context?.let {
            AlertDialog.Builder(it).apply {
                setMessage(msg)
                setPositiveButton(getString(android.R.string.ok), listener)
                setNegativeButton(getString(android.R.string.cancel), null)
                show()
            }
        }
    }
}