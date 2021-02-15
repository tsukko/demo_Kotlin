package jp.co.integrityworks.prototype.ui.insurance

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import jp.co.integrityworks.prototype.R
import jp.co.integrityworks.prototype.databinding.SelectInsuranceCompanyFragmentBinding
import jp.co.integrityworks.prototype.ui.base.BaseFragment
import jp.co.integrityworks.prototype.util.ConstantValue.Companion.CAMERA_PERMISSION_REQUEST_CODE
import jp.co.integrityworks.prototype.util.ConstantValue.Companion.REQUEST_GET_IMAGE
import kotlinx.android.synthetic.main.activity_in_case_of.*
import kotlinx.android.synthetic.main.select_insurance_company_fragment.*
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*


//〇〇を追加する画面
// ステップ２
// 〇〇会社、証券番号入力画面
class SelectInsuranceCompanyFragment : BaseFragment() {
    private var mBinding: SelectInsuranceCompanyFragmentBinding? = null
    private var mInsuranceType: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.select_insurance_company_fragment, container, false)
        mInsuranceType = arguments?.getString("InsuranceType")
        return mBinding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //タイトル名
        activity?.anyChanceToolbar?.title = getString(R.string.title_insurance_list)

        picturePathtextView.visibility = View.GONE
        pictureImageView.visibility = View.GONE

        addPictureView.setOnClickListener { selectPicture() }
        //次へ
        nextButton.setOnClickListener { goNextPage() }
        //戻る
        backButton.setOnClickListener { fragmentManager?.popBackStack() }
    }

    //契約を追加する画面
    private fun goNextPage() {
        if (policyNumberEditText.text.isBlank()) {
            showDialog("証券番号が入力されていません", DialogInterface.OnClickListener { _, _ ->
            })
            return
        }

        val bundle = Bundle()
        bundle.putString("InsuranceType", mInsuranceType)
        bundle.putString("InsuranceCompany", companySpinner.selectedItem.toString())
        bundle.putString("PolicyNumber", policyNumberEditText.text.toString())
        bundle.putString("PicFileName", picturePathtextView.text.toString())
        val fragment = ConfirmInsuranceFragment()
        fragment.arguments = bundle
        goNextFragment(fragment)
    }

    //TODO カメラ周りの処理をそのまま書いたので、後々整理したい
    private fun selectPicture() {
        // カメラ機能を実装したアプリが存在するかチェック
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).resolveActivity(activity?.packageManager!!)?.let {
            if (checkCameraPermission()) {
                takePicture()
            } else {
                grantCameraPermission()
            }
        } ?: showDialog("カメラを扱うアプリがありません", DialogInterface.OnClickListener { _, _ ->
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_GET_IMAGE && resultCode == RESULT_OK) {
            var capturedImage: Bitmap? = null
            data?.data?.let {
                // カメラで撮影した場合
                val stream: InputStream? = context?.contentResolver?.openInputStream(it)
                capturedImage = BitmapFactory.decodeStream(stream)
                picturePathtextView.text = it.toString()
            }
            if (capturedImage != null) {
                pictureImageView.visibility = View.VISIBLE
                pictureImageView.setImageBitmap(capturedImage)
                picturePathtextView.visibility = View.VISIBLE
            } else {
                pictureImageView.visibility = View.GONE
                picturePathtextView.text = ""
                picturePathtextView.visibility = View.GONE
            }
        }
    }

    // このメソッド使うときは、contextが存在する前提
    private fun checkCameraPermission(): Boolean {
        // パーミッションが許可されているか
        val permissions: Array<String> = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val denyPermissionList = arrayListOf<String>()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                denyPermissionList.add(permission)
            }
        }
        // すべて許可されている場合は、nullを返却
        if (denyPermissionList.size == 0) {
            return true
        }
        return false
    }

    private fun grantCameraPermission() =
            requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), CAMERA_PERMISSION_REQUEST_CODE)

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePicture()
            }
        }
    }

    private fun takePicture() {
        //SDK_INT < 19
//        val pickPhotoIntent = Intent(Intent.ACTION_GET_CONTENT)
//        pickPhotoIntent.type = "image/*"
        val pickPhotoIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/jpeg"
        }
        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            putExtra(MediaStore.EXTRA_OUTPUT, createSaveFileUri())
        }
        val chooserIntent = Intent.createChooser(pickPhotoIntent, "選択方法を選んでください").apply {
            putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(takePhotoIntent))
        }
        startActivityForResult(chooserIntent, REQUEST_GET_IMAGE)
    }

    private fun createSaveFileUri(): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.JAPAN).format(Date())
        val imageFileName = "sample_$timeStamp"

        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        if (!storageDir.exists()) {
            storageDir.mkdir()
        }
        val file = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
        )
        return FileProvider.getUriForFile(requireContext(), requireContext().packageName + ".provider", file)
    }
}
