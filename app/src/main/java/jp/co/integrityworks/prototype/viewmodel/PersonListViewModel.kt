package jp.co.integrityworks.prototype.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import jp.co.integrityworks.prototype.BasicApp
import jp.co.integrityworks.prototype.DataRepository
import jp.co.integrityworks.prototype.db.entity.Person
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

class PersonListViewModel(application: Application) : AndroidViewModel(application) {
    private val mRepository: DataRepository

    private val mObservablePersons: MediatorLiveData<List<Person>> = MediatorLiveData()

    /**
     * Expose the LiveData Products query so the UI can observe it.
     */
    val persons: LiveData<List<Person>>
        get() = mObservablePersons

    init {
        // set by default null, until we get data from the database.
        mObservablePersons.value = null

        mRepository = (application as BasicApp).repository!!
        val persons = mRepository.persons
        mObservablePersons.addSource(persons) { mObservablePersons.setValue(it) }
    }

    fun insertPerson(person: Person) {
        // java.lang.IllegalStateException: Cannot access database on the main thread since it may potentially lock the UI for a long period of time.
        return mRepository.insertPerson(person)
    }

    fun updatePerson(person: Person): Int {
        // java.lang.IllegalStateException: Cannot access database on the main thread since it may potentially lock the UI for a long period of time.
        return mRepository.updatePerson(person)
    }

    fun deletePerson(person: Person) {
        // TODO 非同期実行の方法を要検討
        Completable.fromAction { mRepository.deletePerson(person) }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

}

