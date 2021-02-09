package jp.co.integrityworks.prototype.ui.insurance

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import jp.co.integrityworks.prototype.R
import jp.co.integrityworks.prototype.api.response.PolicyLifeResponse
import jp.co.integrityworks.prototype.databinding.InsuranceDetailFragmentBinding
import jp.co.integrityworks.prototype.db.entity.Insurance
import jp.co.integrityworks.prototype.ui.InquiriesFragment
import jp.co.integrityworks.prototype.ui.base.BaseFragment
import jp.co.integrityworks.prototype.util.ConstantValue
import jp.co.integrityworks.prototype.util.Decoder
import jp.co.integrityworks.prototype.viewmodel.InsuranceDetailViewModel
import kotlinx.android.synthetic.main.activity_in_case_of.*
import kotlinx.android.synthetic.main.insurance_detail_2policy_info.*
import kotlinx.android.synthetic.main.insurance_detail_7policyholder.*
import kotlinx.android.synthetic.main.insurance_detail_8agent.*
import kotlinx.android.synthetic.main.insurance_detail_fragment.*
import kotlinx.android.synthetic.main.select_insurance_company_fragment.*
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

//〇〇詳細画面
class InsuranceDetailFragment : BaseFragment(), OnMapReadyCallback {

    private var mBinding: InsuranceDetailFragmentBinding? = null
    private var mInsurance: Insurance? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.insurance_detail_fragment, container, false)
        mInsurance = arguments?.getSerializable("Insurance") as Insurance?

        return mBinding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //タイトル名、アイコン
        initDisplay()

        // TODO 引数チェック
        val factory = InsuranceDetailViewModel.Factory(
                requireActivity().application,
                arguments?.getString("KEY_CONTRACTOR_ID", ""),
                mInsurance)
        val viewModel = ViewModelProviders.of(this, factory)
                .get(InsuranceDetailViewModel::class.java)
        subscribeUi(viewModel.policyLife)

        //右下に設置された上に戻るボタン
        insuranceDetailGoToTopImageView?.setOnClickListener { insuranceDetailScrollView.smoothScrollTo(0, 0) }
        //スクロールが一番上にあるときは、戻るボタンは表示しない
        insuranceDetailScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            when (scrollY) {
                0 -> {
                    insuranceDetailGoToTopImageView?.isClickable = false
                    insuranceDetailGoToTopImageView?.visibility = View.INVISIBLE
                }
                else -> {
                    insuranceDetailGoToTopImageView?.isClickable = true
                    insuranceDetailGoToTopImageView?.visibility = View.VISIBLE
                }
            }
        }

        //〇〇会社へのご連絡画面
        securitiesInfoLayout.setOnClickListener { goNextFragment(ContactInsuranceCompanyFragment()) }
        //TODO 〇〇証書ダウンロード
        policyHolderImageView.setOnClickListener { }
        //TODO 〇〇証書アップロード
        imageView7_3.setOnClickListener { selectPicture() }
        //TODO 担当者へ発信
        imageView8_10.setOnClickListener { callEmergencyContact() }
        //担当者へメール
        imageView8_11.setOnClickListener { goNextFragment(InquiriesFragment()) }
        //TODO アプリを開く
        imageView8_12.setOnClickListener { }
    }

    private fun initDisplay() {

        when (mInsurance?.insuranceTypeCode) {
            "automobile" -> {
                activity?.anyChanceToolbar?.title = getString(R.string.title_car_insurance)
                insuranceDetailTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.automobile, 0, 0, 0)
            }
            "life" -> {
                activity?.anyChanceToolbar?.title = getString(R.string.title_life_insurance)
                insuranceDetailTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.life, 0, 0, 0)
            }
            "house" -> {
                activity?.anyChanceToolbar?.title = getString(R.string.title_fire_insurance)
                insuranceDetailTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.house, 0, 0, 0)
            }
            "accident" -> {
                activity?.anyChanceToolbar?.title = getString(R.string.title_accident_insurance)
                insuranceDetailTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.accident, 0, 0, 0)
            }
            "etc" -> {
                activity?.anyChanceToolbar?.title = getString(R.string.title_other_insurance)
                insuranceDetailTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.etc, 0, 0, 0)
            }
            // TODO 別のFragment.そもそも全部別にすべきかも
//            "creditcard" -> {
//                activity?.anyChanceToolbar?.title = getString(R.string.title_car)
//                insuranceDetailTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.creditcard, 0, 0, 0)
//            }
//            "warranty" -> {
//                activity?.anyChanceToolbar?.title = getString(R.string.title_fire_insurance)
//                insuranceDetailTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.warranty, 0, 0, 0)
//            }
            else -> {
                activity?.anyChanceToolbar?.title = getString(R.string.title_other_insurance)
                insuranceDetailTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.etc, 0, 0, 0)
            }
        }
    }

    private fun subscribeUi(liveData: LiveData<PolicyLifeResponse>) {
        liveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                mBinding?.policyLifeResponse = it

                val imageSrc = it.policyholderInfoList[0].policyholder.split(",")
                if (imageSrc.size == 2) {
                    policyHolderImageView.setImageBitmap(Decoder().decodeBase64(imageSrc[1]))
                }
            }
            mBinding?.executePendingBindings()
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        //  val mMap = googleMap
    }

    //////// TODO コピペコード
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
        if (requestCode == ConstantValue.REQUEST_GET_IMAGE && resultCode == Activity.RESULT_OK) {
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
            requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), ConstantValue.CAMERA_PERMISSION_REQUEST_CODE)

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        if (requestCode == ConstantValue.CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePicture()
            }
        } else if (requestCode == ConstantValue.REQUEST_READ_CONTACTS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callEmergencyContact()
            } else {
                showWarningDialog()
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
        startActivityForResult(chooserIntent, ConstantValue.REQUEST_GET_IMAGE)
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

    //////TODO ここもコピペコード
    private fun callEmergencyContact() {
        if (!mayRequestContacts()) {
            return
        }
        call()
    }

    private fun call() {
        try {
            // TODO dummy phone Number
            val phoneNumber = "+81-123-0000-9999"
            val uri = getString(R.string.text_call_number, phoneNumber)
            AlertDialog.Builder(requireContext()).apply {
                setMessage(getString(R.string.text_call_message, uri))
                setPositiveButton(android.R.string.ok) { _, _ ->
                    val intent = Intent(Intent.ACTION_CALL, Uri.parse(uri))
                    //音声発信はいきなり行わないで、電話番号を設定するだけでダイヤル画面へ遷移させる方法
                    //                    val i = Intent(Intent.ACTION_DIAL, uri)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
                setNegativeButton(android.R.string.cancel, null)
                show()
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    // 以下、パーミッション許可まわり
    private fun mayRequestContacts(): Boolean {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
            Log.i("debug", "This is sample log. shouldShowRequestPermissionRationale CALL_PHONE")
        }
        requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), ConstantValue.REQUEST_READ_CONTACTS)
        return false
    }

    private fun showWarningDialog() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
            // 何もしない
            Log.i("debug", "This is sample log. shouldShowRequestPermissionRationale CALL_PHONE2")
        } else {
            AlertDialog.Builder(requireContext())
                    .setTitle("パーミッション取得エラー")
                    .setMessage("今後は許可しないが選択されました。アプリ設定＞権限をチェックしてください（権限をON/OFFすることで状態はリセットされます）")
                    .setPositiveButton(android.R.string.ok) { _, _ -> openSettings() }
                    .setNegativeButton(android.R.string.cancel, null)
                    .create()
                    .show()
        }
    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity?.packageName, null)
        intent.data = uri
        startActivity(intent)
    }
}
