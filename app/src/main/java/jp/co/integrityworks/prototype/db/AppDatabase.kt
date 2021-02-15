package jp.co.integrityworks.prototype.db

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import jp.co.integrityworks.prototype.AppExecutors
import jp.co.integrityworks.prototype.db.dao.InsuranceDao
import jp.co.integrityworks.prototype.db.dao.PersonDao
import jp.co.integrityworks.prototype.db.entity.Insurance
import jp.co.integrityworks.prototype.db.entity.Person

@Database(entities = [Person::class, Insurance::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    private val mIsDatabaseCreated = MutableLiveData<Boolean>()

    val databaseCreated: LiveData<Boolean>
        get() = mIsDatabaseCreated

    abstract fun personDao(): PersonDao
    abstract fun insuranceDao(): InsuranceDao

    private fun updateDatabaseCreated(context: Context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated()
        }
    }

    private fun setDatabaseCreated() {
        mIsDatabaseCreated.postValue(true)
    }

    companion object {

        private var sInstance: AppDatabase? = null

        @VisibleForTesting
        val DATABASE_NAME = "basic-sample-db"

        fun getInstance(context: Context, executors: AppExecutors?): AppDatabase? {
            if (sInstance == null) {
                synchronized(AppDatabase::class.java) {
                    if (sInstance == null) {
                        sInstance = buildDatabase(context.applicationContext, executors)
                        sInstance!!.updateDatabaseCreated(context.applicationContext)
                    }
                }
            }
            return sInstance
        }

        private fun buildDatabase(appContext: Context,
                                  executors: AppExecutors?): AppDatabase {
            return Room.databaseBuilder(appContext, AppDatabase::class.java, DATABASE_NAME)
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            executors?.diskIO()?.execute {
                                val database: AppDatabase? = getInstance(appContext, executors)
                                val persons = DataGenerator.generatePersons()
                                val insurances = DataGenerator.generateInsurance()
                                insertData(database, persons, insurances)
                                database?.setDatabaseCreated()
                            }
                        }
                    })
                    .build()
        }

        private fun insertData(database: AppDatabase?,
                               persons: List<Person>,
                               insurances: List<Insurance>) {
            database?.runInTransaction {
                database.personDao().insertAll(persons)
                database.insuranceDao().insertAll(insurances)
            }
        }
    }
}
