package jp.co.integrityworks.prototype.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import jp.co.integrityworks.prototype.db.entity.Insurance

//InsuranceDao
@Dao
interface InsuranceDao {
    @Query("SELECT * FROM insurances")
    fun loadAllInsurances(): LiveData<List<Insurance>>

    // add
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(insurances: List<Insurance>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(insurance: Insurance)

    //update
//    @Query("UPDATE insurances SET nameKanji = :name WHERE id = :personId")
//    fun update(name: String, personId: Int)

//    @Query("DELETE FROM insurances WHERE id = :insuranceId")
//    fun delete(insuranceId: Int)
}
