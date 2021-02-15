package jp.co.integrityworks.prototype

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import jp.co.integrityworks.prototype.db.AppDatabase
import jp.co.integrityworks.prototype.db.entity.Insurance
import jp.co.integrityworks.prototype.db.entity.Person

/**
 * Repository handling the work with products and comments.
 */
class DataRepository private constructor(private val mDatabase: AppDatabase) {
    private val mObservablePersons: MediatorLiveData<List<Person>> = MediatorLiveData()
    val persons: LiveData<List<Person>>
        get() = mObservablePersons
    private val mObservableInsurances: MediatorLiveData<List<Insurance>>
    val insurances: LiveData<List<Insurance>>
        get() = mObservableInsurances

    init {
        // DBに持っているデータを設定する
        mObservablePersons.addSource(mDatabase.personDao().loadAllPersons()
        ) {
            if (mDatabase.databaseCreated.value != null) {
                mObservablePersons.postValue(it)
            }
        }
        mObservableInsurances = MediatorLiveData()
        mObservableInsurances.addSource(mDatabase.insuranceDao().loadAllInsurances()
        ) {
            if (mDatabase.databaseCreated.value != null) {
                mObservableInsurances.postValue(it)
            }
        }
    }

    fun insertPerson(person: Person) {
        return mDatabase.personDao().insertPerson(person)
    }

    fun updatePerson(person: Person): Int {
        return mDatabase.personDao().updatePerson(person)
    }

    fun deletePerson(person: Person) {
        return mDatabase.personDao().deletePerson(person)
    }

    fun addInsurance(insurance: Insurance) {
        return mDatabase.insuranceDao().insert(insurance)
    }

    fun replaceInsurance(insurances: List<Insurance>) {
        return mDatabase.insuranceDao().insertAll(insurances)
    }

    companion object {

        private var sInstance: DataRepository? = null

        fun getInstance(database: AppDatabase?): DataRepository? {
            if (sInstance == null || database != null) {
                synchronized(DataRepository::class.java) {
                    if (sInstance == null) {
                        sInstance = DataRepository(database!!)
                    }
                }
            }
            return sInstance
        }
    }
}
