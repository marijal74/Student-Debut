package com.example.studentdebut.Adapter

import android.content.Context
import android.content.Entity
import android.content.Intent
import android.net.Uri
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studentdebut.Database.jobItem
import com.example.studentdebut.Interface.ItemClickListener
import com.example.studentdebut.Model.Item
import com.example.studentdebut.R


class FeedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{

    var txtTitle: TextView
    var txtPubdate: TextView
    var txtContent: TextView

    private var itemClickListener : ItemClickListener?=null

    init{

        txtTitle = itemView.findViewById(R.id.txtTitle) as TextView
        txtPubdate = itemView.findViewById(R.id.txtPubDate) as TextView
        txtContent = itemView.findViewById(R.id.txtContent) as TextView

         itemView.setOnClickListener(this)
    }

    fun setItemClickListener(itemClickListener: ItemClickListener){

        this.itemClickListener = itemClickListener
    }
    override fun onClick(v: View?){
        itemClickListener!!.onClick(v, absoluteAdapterPosition, false)
    }


}
// XXX
// promenjen items na var da bi bilo moguce menjanje njegovih polja, dodato polje url na osnovu koga
// pomocu regex-a mozemo da utvrdimo o kom sajtu se radi, da bi parsirali u zavisnosti od sajtova
class FeedAdapter internal constructor( private val mContext :Context): RecyclerView.Adapter<FeedViewHolder>(){

    private  val inflater: LayoutInflater
    private var jobs = emptyList<jobItem>()

     init{

        inflater = LayoutInflater.from(mContext)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val itemView = inflater.inflate(R.layout.row, parent, false)
        return FeedViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        d("velicina", jobs.size.toString())
        return jobs.size
    }

    internal fun setJobs(jobs: List<jobItem>) {
        this.jobs = jobs
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {

        //TODO: filter po zvanju
        holder.txtTitle.text = jobs[position].title
        //ovde je potrebno lepo transformisati tekst
        // TODO: filter po sadrzaju (istraziti sta sve moze tu da bude i da li mozemo da iskoristimo
        //        za jos neko vid filtriranja)


        holder.txtContent.text = jobs[position].content
        // TODO: filtrirati po tome da li je vec zavrsena prijava
        holder.txtPubdate.text = jobs[position].pubDate
        d("itemContent", jobs[position].content)


        holder.setItemClickListener(object : ItemClickListener { //anonymus object
            override fun onClick(view: View?, position: Int, isLongClick: Boolean) {
                if (!isLongClick) {

                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(jobs[position].link))
                    mContext.startActivity(browserIntent)
                }
            }
        })
    }

}
