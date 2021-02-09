package jp.co.integrityworks.prototype

import android.app.Application
import jp.co.integrityworks.prototype.db.AppDatabase

/**
 * Android Application class. Used for accessing singletons.
 */
class BasicApp : Application() {

    private var mAppExecutors: AppExecutors? = null

    private val database: AppDatabase?
        get() = AppDatabase.getInstance(this, mAppExecutors)

    val repository: DataRepository?
        get() = DataRepository.getInstance(database)

    override fun onCreate() {
        super.onCreate()

        mAppExecutors = AppExecutors()
    }
}
