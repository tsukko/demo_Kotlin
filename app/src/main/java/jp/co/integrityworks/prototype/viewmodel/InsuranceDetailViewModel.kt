package jp.co.integrityworks.prototype.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import jp.co.integrityworks.prototype.api.ApiClientManager
import jp.co.integrityworks.prototype.api.response.PolicyLifeResponse
import jp.co.integrityworks.prototype.db.DataGenerator
import jp.co.integrityworks.prototype.db.entity.Insurance
import rx.subscriptions.CompositeSubscription

class InsuranceDetailViewModel(application: Application, contractorId: String?, insurance: Insurance?) : AndroidViewModel(application) {
    private val compositeSubscription = CompositeSubscription()
    private val mObsPolicyLife: MediatorLiveData<PolicyLifeResponse> = MediatorLiveData()

    val policyLife: LiveData<PolicyLifeResponse>
        get() = mObsPolicyLife

    init {
        mObsPolicyLife.value = null

        // TODO PoCとして、APIでとれない時に、固定値を表示させている
        val dummyDate: PolicyLifeResponse = DataGenerator.generatePolicyLife(insurance)
        mObsPolicyLife.value = dummyDate

        loadPolicyLife(contractorId?:"O000000001", insurance?.policyInfoNo?:"1")
    }

    // 詳細取得
    private fun loadPolicyLife(id: String, policyInfoNo: String) {
        compositeSubscription.clear()
        compositeSubscription.add(
                ApiClientManager.apiClient.getLifePolicies(id, policyInfoNo)
                        .subscribeOn(rx.schedulers.Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext {
                            Log.d("debug", "getLifePolicies doOnNext:")
                            setData(it[0])
                        }
                        .doOnError {
                            // java.net.ConnectException から
                            // java.lang.IllegalStateException: Exception thrown on Scheduler.Worker thread. Add `onError` handling.
                            // が発生したケースなど
                            Log.d("debug", "getLifePolicies doOnError:")
                        }
                        .onErrorReturn {
                            error()
                        }
                        .doOnCompleted {
                            Log.d("debug", "getLifePolicies doOnCompleted")
                        }
                        .subscribe())
    }

    // Mockサーバが起動していないなど、データが取得できない場合の処理
    private fun error(): ArrayList<PolicyLifeResponse>? {
        Log.d("debug", "getLifePolicies onErrorReturn")
        return null
    }

    private fun setData(policyLifeResponse: PolicyLifeResponse) {
        mObsPolicyLife.postValue(policyLifeResponse)
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val mApplication: Application, private val contractorId: String?, val insurance: Insurance?) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return InsuranceDetailViewModel(mApplication, contractorId, insurance) as T
        }
    }
}
