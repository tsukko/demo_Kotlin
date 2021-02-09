package jp.co.integrityworks.prototype.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import jp.co.integrityworks.prototype.db.entity.Person

//契約者
@Dao
interface PersonDao {
    @Query("SELECT * FROM persons")
    fun loadAllPersons(): LiveData<List<Person>>

    @Query("select * from persons where id = :personId")
    fun loadPerson(personId: Int): LiveData<Person>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(persons: List<Person>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPerson(person: Person)

    @Update
    fun updatePerson(person: Person): Int

    @Delete
    fun deletePerson(person: Person)

    @Query("DELETE FROM persons WHERE id = :personId")
    fun delete(personId: Int)
}
