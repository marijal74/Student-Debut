package com.example.studentdebut.Database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


//pravi se baza, koja je singlton
@Database (entities = arrayOf(jobItem::class), version = 1)
public abstract class JobsDatabase: RoomDatabase() {
    abstract fun jobDao(): DataAccessObject

    companion object {

        //sve sto se pise u ovaj thread je vidljivo ostalim
        //pravimo singlton baze
        @Volatile
        private var instance: JobsDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): JobsDatabase {
            val tempInstance = instance
            //ako je vec napravljena samo je prosledi
            if (tempInstance != null) {
                return tempInstance
            }

            //  izvrsava ovo konkurentno, zakljucava ovaj singlton monitorom
            // pravi room bazu
            synchronized(this) {
                val roomInstance = Room.databaseBuilder(
                    context.applicationContext,
                    JobsDatabase::class.java,
                    "jobs_database"
                ).addCallback(JobsDatabaseCallback(scope)).build()
                instance = roomInstance
                return roomInstance
            }
        }

    }

    // implementira callback fju
    // pp da mogu razlicite stvari da se implementiraju ovde, ja sam samo ostavila kod koji puni bazu
    // po njenom otvaranju
    private class JobsDatabaseCallback( private val scope: CoroutineScope) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            instance?.let { database ->
                scope.launch {
                    populateDatabase(database.jobDao())
                }
            }
        }

        // fja koja puni bazu, pp da je preporuceno da se obrisu podaci
        // koji su  ostali u bazi nakon poslednjeg koriscenja applikacije
        // a msm da bi moralo i zbg slucaja kada korisnik opet udje u MainActivity i izmeni filtere
        // u tom slucaju: TODO napisati upit za brisanje svih podataka iz baze
        suspend fun populateDatabase(jobDao: DataAccessObject) {
            // Delete all content here.
            //jobDao.deleteAll()

            // pravi jedan red i ubacuje ga u tabelu pomocu Dao interfejsa
            val aJob = jobItem (0, "title", "pubdate", "link", "desc", "content", "", "", "", "")
            jobDao.insert(aJob)



        }
    }



}


