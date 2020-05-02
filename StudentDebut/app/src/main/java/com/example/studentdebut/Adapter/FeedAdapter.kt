package com.example.studentdebut.Adapter

import android.animation.ObjectAnimator
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.studentdebut.Database.jobItem
import com.example.studentdebut.Interface.ItemClickListener
import com.example.studentdebut.ListOfJobs
import com.example.studentdebut.R
import com.example.studentdebut.R.*
import com.ms.square.android.expandabletextview.ExpandableTextView


class FeedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    var txtTitle: TextView
    var txtPubdate: TextView
    var txtContent: ExpandableTextView
    private var itemClickListener: ItemClickListener? = null

    init {

        txtTitle = itemView.findViewById(id.txtTitle) as TextView
        txtPubdate = itemView.findViewById(id.txtPubDate) as TextView
        txtContent = itemView.findViewById(id.txtContent) as ExpandableTextView


    }
}
// XXX
// promenjen items na var da bi bilo moguce menjanje njegovih polja, dodato polje url na osnovu koga
// pomocu regex-a mozemo da utvrdimo o kom sajtu se radi, da bi parsirali u zavisnosti od sajtova
class FeedAdapter internal constructor( private val mContext :Context): RecyclerView.Adapter<FeedViewHolder>(){

    private  val inflater: LayoutInflater
    //private val jobs = mutableListOf<jobItem>()
    private var jobs = emptyList<jobItem>()

     init{

        inflater = LayoutInflater.from(mContext)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val itemView = inflater.inflate(layout.row, parent, false)
        return FeedViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        d("velicina", jobs.size.toString())
        return jobs.size
    }

    internal fun setJobs(jobs: List<jobItem>) {
        //this.jobs.addAll(jobs)
        this.jobs = jobs
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {

        //TODO: filter po zvanju
        holder.txtTitle.text = jobs[position].title
        //ovde je potrebno lepo transformisati tekst



        holder.txtContent.text = jobs[position].content
        // TODO: filtrirati po tome da li je vec zavrsena prijava
        holder.txtPubdate.text = jobs[position].pubDate


        holder.txtTitle.setOnClickListener(){
            holder.txtTitle.setTextColor((Color.parseColor("#663366")))
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(jobs[position].link))
                        mContext.startActivity(browserIntent)



                    }

    }

}