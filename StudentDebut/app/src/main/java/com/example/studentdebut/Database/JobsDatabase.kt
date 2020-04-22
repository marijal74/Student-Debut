package com.example.studentdebut.Database


import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.studentdebut.MyApp.Companion.ListOfJobItems
import com.example.studentdebut.MyApp.Companion.done
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


//pravi se baza, koja je singlton
@Database (entities = arrayOf(jobItem::class), version = 1)
public abstract class JobsDatabase: RoomDatabase() {

    val dbCreated=MutableLiveData<Boolean>()
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
                ).addCallback(JobsDatabaseCallback(scope,context)).build()
                instance = roomInstance
                return roomInstance
            }
        }

    }

    // implementira callback fju
    // pp da mogu razlicite stvari da se implementiraju ovde, ja sam samo ostavila kod koji puni bazu
    // po njenom otvaranju
    private class JobsDatabaseCallback(private val scope: CoroutineScope,val appContext: Context):RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                scope.launch {
                    //TODO maybe neka obrada ovde
                    // Generate the data for pre-population
                    withContext(IO) {
                        val database: JobsDatabase = JobsDatabase.getDatabase(appContext, scope)
                        if (done) {
                            var products = ListOfJobItems
                            database.jobDao().insert(products)

                        }
                        // notify that the database was created and it's ready to be used

                        database.dbCreated.postValue(true)
                    }
                }
            }

        }
}












// fja koja puni bazu, pp da je preporuceno da se obrisu podaci
// koji su  ostali u bazi nakon poslednjeg koriscenja applikacije
// a msm da bi moralo i zbg slucaja kada korisnik opet udje u MainActivity i izmeni filtere
// u tom slucaju: TODO napisati upit za brisanje svih podataka iz baze
/* suspend fun populateDatabase(jobDao: DataAccessObject) {
        // Delete all content here.
        //jobDao.deleteAll()

        // pravi jedan red i ubacuje ga u tabelu pomocu Dao interfejsa
        val aJob = jobItem(0, "title", "pubdate", "link", "desc", "content", "", "", "", "")
        jobDao.insert(aJob)


    }*/






