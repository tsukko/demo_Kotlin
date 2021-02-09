package jp.co.integrityworks.prototype.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import jp.co.integrityworks.prototype.BasicApp
import jp.co.integrityworks.prototype.DataRepository
import jp.co.integrityworks.prototype.api.ApiClientManager
import jp.co.integrityworks.prototype.api.response.PoliciesResponse
import jp.co.integrityworks.prototype.db.entity.Insurance
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

class InsuranceListViewModel(application: Application) : AndroidViewModel(application) {
    private val compositeSubscription = CompositeSubscription()
    private val mRepository: DataRepository
    private val mObservableInsurances: MediatorLiveData<List<Insurance>> = MediatorLiveData()

    // TODO 最後のcompositeSubscription.clear()ってどこかでする？

    val insurances: LiveData<List<Insurance>>
        get() = mObservableInsurances

    init {
        mObservableInsurances.value = null

        mRepository = (application as BasicApp).repository!!

        val insurances = mRepository.insurances
        mObservableInsurances.addSource(insurances) {
            mObservableInsurances.value = it
            loadPolicies("O000000001")
        }
    }

    private fun loadPolicies(id: String) {
        compositeSubscription.clear()
        compositeSubscription.add(
                ApiClientManager.apiClient.getPolicies(id)
                        .subscribeOn(rx.schedulers.Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext {
                            Log.d("debug", "doOnNext")
                            setData(it[0])
                        }
                        .doOnError {
                            // java.net.ConnectException から
                            // java.lang.IllegalStateException: Exception thrown on Scheduler.Worker thread. Add `onError` handling.
                            // が発生したケースなど
                            Log.d("debug", "doOnError:")
                        }
                        .onErrorReturn {
                            error()
                        }
                        .doOnCompleted {
                            Log.d("debug", "doOnCompleted")
                        }
                        .subscribe())
    }

    // Mockサーバが起動していないなど、データが取得できない場合の処理
    private fun error(): ArrayList<PoliciesResponse>? {
        Log.d("debug", "onErrorReturn")
        return null
    }

    private fun setData(policiesResponse: PoliciesResponse) {
        val data = ArrayList<Insurance>()
        for (pList in policiesResponse.policyList) {
            val sampleInsurance = Insurance(
                    accidentTypeId = 1,
                    policyInfoNo = pList.policyInfo.policyInfoNo,
                    insuranceCompanyCode = pList.policyInfo.insuranceCompanyCode,
                    insuranceCompanyName = pList.policyInfo.insuranceCompanyName,
                    insuranceTypeCode = pList.policyInfo.insuranceTypeCode,
                    insuranceTypeName = pList.policyInfo.insuranceTypeName,
                    insuranceTradeName = pList.policyInfo.insuranceTradeName,
                    policyNumber = pList.policyInfo.policyNumber,
                    policyNumberBranch = pList.policyInfo.policyNumberBranch,
                    maturityDate = pList.policyInfo.maturityDate,
                    carName = pList.automobileInfo.carName,
                    registNumber = pList.automobileInfo.registNumber
            )
            data.add(sampleInsurance)
        }

        // TODO 内部DBへの保存とか必要かどうか
        // サーバから取得したデータで更新
//        mRepository.replaceInsurance(data)
        mObservableInsurances.postValue(data)
    }

    fun addInsurance(insurance: Insurance) {
        // TODO 非同期実行の方法を要検討
        Completable.fromAction { mRepository.addInsurance(insurance) }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }
}
