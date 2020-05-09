package com.example.studentdebut.Adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.studentdebut.Database.jobItem
import com.example.studentdebut.R.*
import com.ms.square.android.expandabletextview.ExpandableTextView

//klasa koja prikazuje podatke u recyclew view
class FeedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    var txtTitle: TextView = itemView.findViewById(id.txtTitle) as TextView
    var txtPubdate: TextView = itemView.findViewById(id.txtPubDate) as TextView
    var txtContent: ExpandableTextView = itemView.findViewById(id.txtContent) as ExpandableTextView

   //vrsi inicijalizaciju
    init {

        txtTitle = itemView.findViewById(id.txtTitle) as TextView
        txtPubdate = itemView.findViewById(id.txtPubDate) as TextView
        txtContent = itemView.findViewById(id.txtContent) as ExpandableTextView


    }
}
class FeedAdapter internal constructor( private val mContext :Context): RecyclerView.Adapter<FeedViewHolder>(){

    private  val inflater: LayoutInflater = LayoutInflater.from(mContext)
    private var jobs = emptyList<jobItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val itemView = inflater.inflate(layout.row, parent, false)
        return FeedViewHolder(itemView)
    }
    //fja koja proverava da li recyclew view ima nesto da prikaze i ako nema izbacuje poruku
    override fun getItemCount(): Int {
        d("velicina", jobs.size.toString())
        return jobs.size
    }

    internal fun setJobs(jobs: List<jobItem>) {

        this.jobs = jobs
        notifyDataSetChanged()
    }

    //fja koja postavlja pronadjene podatke u polja recyclew viewa
    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {


        holder.txtTitle.text = jobs[position].title

        holder.txtContent.text = jobs[position].content

        holder.txtPubdate.text = jobs[position].pubDate

        //kada se klikne na naslov clanka vodi ka internetu(trazenoj objavi na sajtu)
        holder.txtTitle.setOnClickListener {

            holder.txtTitle.setTextColor((Color.parseColor("#663366")))
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(jobs[position].link))
                        mContext.startActivity(browserIntent)
        }
    }
}