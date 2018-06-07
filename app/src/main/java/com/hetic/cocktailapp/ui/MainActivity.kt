package com.hetic.cheers.ui

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.hetic.cheers.R
import com.hetic.cheers.adapter.CocktailCardAdapter
import com.hetic.cheers.api.CocktailService
import com.hetic.cheers.model.Cocktail
import com.hetic.cheers.model.Tag
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable


class MainActivity : AppCompatActivity() {

    private var mCocktails : List<Cocktail> = arrayListOf()
    private var mTags : List<Tag> = arrayListOf()
    private var mRecommandations : ArrayList<Cocktail> = arrayListOf()

    companion object {

        @JvmStatic
        fun getIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }

        val COCKTAILS = "cocktails"
        val RECOMMANDATIONS = "recommandations"
        val TAGS = "tags"

        @JvmStatic
        fun getFirstIntent(
                context: Context,
                tags : List<Tag>,
                cocktails: List<Cocktail>,
                recommandations: List<Cocktail>
        ): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(COCKTAILS, cocktails as Serializable)
            intent.putExtra(TAGS, tags as Serializable)
            intent.putExtra(RECOMMANDATIONS, recommandations as Serializable)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mCocktails = intent.getSerializableExtra(COCKTAILS) as List<Cocktail>
        mTags =  intent.getSerializableExtra(TAGS) as List<Tag>
        mRecommandations =intent.getSerializableExtra(RECOMMANDATIONS) as ArrayList<Cocktail>
        initAutomplete()
        initCocktailList()
        initAdvancedSearchButton()
    }

    //INIT RECYCLER VIEW WITH CHOSEN ITEM LAYOUT AND ORIENTATION FOR TAGS
    private val mOrientation : Int = LinearLayoutManager.HORIZONTAL
    private val mTemplate : Int = R.layout.cocktail_card_item_horizontal

    private fun initCocktailList(){
        val adapter = CocktailCardAdapter(mTemplate) {goDetail(it.id) }
        cocktail_list.adapter = adapter
        cocktail_list.layoutManager = LinearLayoutManager(this, mOrientation,false)
        adapter.swapItems(mRecommandations)
        adapter.notifyDataSetChanged()
    }

    private fun goDetail(id : Int){
        val detailIntent = DetailActivity.getIntent(this,id)
        startActivity(detailIntent)
    }

    fun initAutomplete(){
        val cocktailsName = mCocktails.map { i -> i.name }.toTypedArray()
        val cocktailsId = mCocktails.map { i -> i.id }.toTypedArray()

        val adapter = ArrayAdapter<String>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                cocktailsName
        )
        input_search.setAdapter(adapter)

        input_search.onItemClickListener = AdapterView.OnItemClickListener{
            parent,view,position,id->
            val selectedItem = parent.getItemAtPosition(position).toString()
            goDetail(cocktailsId[position])
        }
    }

    fun initAdvancedSearchButton(){
        advanced_search.setOnClickListener{
            val advancedIntent = AdvancedSearchActivity.getIntent(this,mTags)
            startActivity(advancedIntent)
        }
    }
}