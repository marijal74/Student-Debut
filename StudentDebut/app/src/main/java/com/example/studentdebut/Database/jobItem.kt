package com.example.studentdebut.Database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

// predstavlja nasu tabelu, tacnije red nase tabele ali iz nekog razloga u dokementaciji govore da je tabela

@Entity (tableName="jobs_table")
data class jobItem(
    //primarni kljuc identifikator, automatski se generise, bar bi trebalo
    @PrimaryKey(autoGenerate = true)
    var id:Int,
    // item polja begin
    val title: String,
    var pubDate:String,
    var link:String,
    var description: String,
    var content: String,
    //item polja end
    // kategorije
    // posao ili praksa
    var job: String,
    // inostrantsvo/Srbija
    var stationed: String,
    // pozicija
    var position:String,
    //language
    var language:String): Parcelable {

    //procitala negde da ako se id postavi na null ili 0 a primarni je kljuc, da to ne smeta autoGenerate opciji
    // ovaj konstruktor bi vrvt mogao da se izbrise, ili samo da se ostavi id we shall see
    init{
        id=0
        /*title = title
        pubDate = pubDate
        link = link
        description = description
        content = content
        //posao/praksa
        job = job
        // inostranstvo/Srbija
        stationed = stationed
        // pozicija
        position = position
        //programski jezik
        language = language*/
    }

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun toString(): String {
        return "ID: $id\nTitle $title\nPubDate: $pubDate\nLink $link\nDescription: $description\nContent $content\nJob: $job\nStationed:$stationed\nPosition$position\nlanguage$language\n"
    }


    override fun describeContents(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(pubDate)
        parcel.writeString(link)
        parcel.writeString(description)
        parcel.writeString(content)
        parcel.writeString(job)
        parcel.writeString(stationed)
        parcel.writeString(position)
        parcel.writeString(language)
    }

    companion object CREATOR : Parcelable.Creator<jobItem> {
        override fun createFromParcel(parcel: Parcel): jobItem {
            return jobItem(parcel)
        }

        override fun newArray(size: Int): Array<jobItem?> {
            return arrayOfNulls(size)
        }
    }
}