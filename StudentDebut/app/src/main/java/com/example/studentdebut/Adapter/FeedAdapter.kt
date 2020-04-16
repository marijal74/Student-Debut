package com.example.studentdebut.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studentdebut.Interface.ItemClickListener
import com.example.studentdebut.Model.Item
import com.example.studentdebut.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.lang.StringBuilder

class FeedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener{

    var txtTitle: TextView
    var txtPubdate: TextView
    var txtContent: TextView

    private var itemClickListener : ItemClickListener?=null

    init{

        txtTitle = itemView.findViewById(R.id.txtTitle) as TextView
        txtPubdate = itemView.findViewById(R.id.txtPubDate) as TextView
        txtContent = itemView.findViewById(R.id.txtContent) as TextView

         itemView.setOnClickListener(this)
         itemView.setOnLongClickListener(this)
    }

    fun setItemClickListener(itemClickListener: ItemClickListener){

        this.itemClickListener = itemClickListener
    }
    override fun onClick(v: View?){
        itemClickListener!!.onClick(v, absoluteAdapterPosition, false)
    }

    //
    override fun onLongClick(v: View?): Boolean {
        itemClickListener!!.onClick(v, absoluteAdapterPosition, true)
        return true
    }

}
// XXX
// promenjen items na var da bi bilo moguce menjanje njegovih polja, dodato polje url na osnovu koga
// pomocu regex-a mozemo da utvrdimo o kom sajtu se radi, da bi parsirali u zavisnosti od sajtova
class FeedAdapter(private var items: MutableList<Item>, private val mContext: Context, val url : String): RecyclerView.Adapter<FeedViewHolder>(){

    private  val inflater: LayoutInflater

    init{

        inflater = LayoutInflater.from(mContext)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val itemView = inflater.inflate(R.layout.row, parent, false)
        return FeedViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {

        //TODO: filter po zvanju
        holder.txtTitle.text = items[position].title
        //ovde je potrebno lepo transformisati tekst
        // TODO: filter po sadrzaju (istraziti sta sve moze tu da bude i da li mozemo da iskoristimo
        //        za jos neko vid filtriranja)

        //fja koji vraca izmenjeni item za prikaz
        items[position] = presentContent(items[position], url)

        holder.txtContent.text = items[position].content
        // TODO: filtrirati po tome da li je vec zavrsena prijava
        holder.txtPubdate.text = items[position].pubDate
        d("itemContent", items[position].content)
        holder.setItemClickListener(object : ItemClickListener { //anonymus object
            override fun onClick(view: View?, position: Int, isLongClick: Boolean) {
                if (!isLongClick) {

                    val browserIntent =
                        Intent(Intent.ACTION_VIEW, Uri.parse(items[position].link))
                    mContext.startActivity(browserIntent)
                }
            }
        })


    }

    private  fun presentContent( item: Item, url:String) : Item{

        val jsoup = Jsoup.parse(item.content)
        val links : List<Element> = jsoup.select("a").filter {
            it.attr("rel").equals("nofollow")
        }
        d("linkovi", links.toString())

        //TODO parsirati u zavisnosti od sajta (argument url)
        //TODO naziv firme, desc i see more

        if(links.isNotEmpty()){
            val link : Element = links.first()

            d("linkic", link.attr("href").toString())
            item.link=link.attr("href")
            val sb = StringBuilder()
            //izvlacenje linka do kompanije koja nudi posao/praksu
            sb.append("Link: " +link.attr("href")+"\n")
            //pozicija
            sb.append("Pozicija: " + link.text()+"\n")
            d("lista", sb.toString())

            item.content = sb.toString()
        }


        return item


    }


}
