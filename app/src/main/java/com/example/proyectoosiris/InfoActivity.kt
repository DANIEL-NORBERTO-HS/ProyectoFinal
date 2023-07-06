package com.example.proyectoosiris

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectoosiris.databinding.ActivityInfoBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class InfoActivity : AppCompatActivity() {

    private lateinit var bindingActivityInfo: ActivityInfoBinding
    private lateinit var messagesListener: ValueEventListener

    private val database = Firebase.database
    private val listVideoGames:MutableList<VideoGame> = ArrayList()
    private val myRef = database.getReference("game")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingActivityInfo = ActivityInfoBinding.inflate(layoutInflater)
        val view = bindingActivityInfo.root

        setContentView(view)
        fullScreen()

        bindingActivityInfo.addImageView.setOnClickListener { v ->
            val intent = Intent(this, AddActivity::class.java)
            v.context.startActivity(intent)
        }

        listVideoGames.clear()
        setupRecyclerView(bindingActivityInfo.recyclerView)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {

        messagesListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listVideoGames.clear()
                dataSnapshot.children.forEach { resp ->
                    val mVideoGame =
                        VideoGame(resp.child("name").value as String?,
                            resp.child("date").value as String?,
                            resp.child("price").value as String?,
                            resp.child("description").value as String?,
                            resp.child("url").value as String?,
                            resp.key)
                    mVideoGame.let { listVideoGames.add(it) }
                }
                recyclerView.adapter = VideoGameViewAdapter(listVideoGames)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "messages:onCancelled: ${error.message}")
            }
        }
        myRef.addValueEventListener(messagesListener)

        deleteSwipe(recyclerView)
    }

    class VideoGameViewAdapter(private val values: List<VideoGame>) :
        RecyclerView.Adapter<VideoGameViewAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.video_game_content, parent, false)
            return ViewHolder(view)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val mVideoGame = values[position]
            holder.mNameTextView.text = mVideoGame.name
            holder.mDateTextView.text = mVideoGame.date
            holder.mPriceTextView.text = mVideoGame.price + " $"
            holder.mPosterImageView.let {
                Glide.with(holder.itemView.context)
                    .load(mVideoGame.url)
                    .into(it)
            }

            holder.itemView.setOnClickListener { v ->
                val intent = Intent(v.context, DetailActivity::class.java).apply {
                    putExtra("key", mVideoGame.key)
                }
                v.context.startActivity(intent)
            }

            holder.itemView.setOnLongClickListener{ v ->
                val intent = Intent(v.context, EditActivity::class.java).apply {
                    putExtra("key", mVideoGame.key)
                }
                v.context.startActivity(intent)
                true
            }

        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val mNameTextView: TextView = view.findViewById(R.id.nameTextView) as TextView
            val mDateTextView: TextView = view.findViewById(R.id.dateTextView) as TextView
            val mPriceTextView: TextView = view.findViewById(R.id.priceTextView) as TextView
            val mPosterImageView: ImageView = view.findViewById(R.id.posterImageView) as ImageView
        }
    }

    private fun deleteSwipe(recyclerView: RecyclerView){
        val touchHelperCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val imageFirebaseStorage = FirebaseStorage.getInstance().reference.child("game/img"+listVideoGames[viewHolder.adapterPosition].key)
                imageFirebaseStorage.delete()

                listVideoGames[viewHolder.adapterPosition].key?.let { myRef.child(it).setValue(null) }
                listVideoGames.removeAt(viewHolder.adapterPosition)

                recyclerView.adapter?.notifyItemRemoved(viewHolder.adapterPosition)
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }
        val itemTouchHelper = ItemTouchHelper(touchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun fullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.insetsController
            if (controller != null) {
                controller.hide(WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

            }
        }
    }
}