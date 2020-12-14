package com.melq.howmanydays

import androidx.room.*
import java.lang.reflect.Constructor

@Database(entities = [DateData::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun dateDao(): DateDao
}

@Entity(tableName = "dates")
data class DateData (
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name:   String,
    val year:   Int,
    val month:  Int,
    val date:   Int,
) {
    constructor(name: String, year: Int, month: Int, date: Int): this(0, name, year, month, date)
}

@Dao
interface DateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dateData : DateData)

    @Update
    fun update(dateData : DateData)

    @Delete
    fun delete(dateData : DateData)

    @Query("delete from dates")
    fun deleteAll()

    @Query("select * from dates")
    fun getAll(): List<DateData>

    @Query("select * from dates where id = :id")
    fun getDate(id: Int): DateData

    fun updateList(dateList: ArrayList<DateData>) {
        val gotList = getAll()
        dateList.clear()
        for (dateData in gotList) {
            dateList.add(dateData)
        }
    }
}