package com.example.myapplication

import android.content.Context
import android.media.Image
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class PostListAdapter internal constructor(context: Context) :
    RecyclerView.Adapter<PostListAdapter.UserViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var posts = emptyList<Post>() // Cached copy of user


    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userNameTextView: TextView = itemView.findViewById(R.id.userName)
        val postDescription : TextView = itemView.findViewById(R.id.postContent)
        val likeCountView : TextView = itemView.findViewById(R.id.likeCount)
        val createdAtView : TextView = itemView.findViewById(R.id.postedAt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = inflater.inflate(R.layout.user_record, parent, false)
        val like:ImageButton = itemView.findViewById((R.id.likeBtn))
        val unlike:ImageButton = itemView.findViewById((R.id.unlikeBtn))
        val fav:ImageButton = itemView.findViewById((R.id.favBtn))
        val unfav:ImageButton = itemView.findViewById((R.id.unfavBtn))
        setButtonFunc(like,unlike,fav,unfav)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val current = posts[position]
        holder.userNameTextView.text = current.name
        holder.postDescription.text = current.postDesc
        holder.likeCountView.text = current.like.toString()
        holder.createdAtView.text = "Created At : "+ current.created_at
    }

    internal fun setPost(posts: List<Post>) {
        this.posts = posts
        notifyDataSetChanged()
    }

    override fun getItemCount() = posts.size

    private fun setButtonFunc(like:ImageButton,unlike:ImageButton,fav:ImageButton,unfav:ImageButton){
        like.visibility=View.GONE
        fav.visibility=View.GONE
        unfav.setOnClickListener{
            unfav.visibility=View.GONE
            fav.visibility=View.VISIBLE
        }
        fav.setOnClickListener{
            unfav.visibility=View.VISIBLE
            fav.visibility=View.GONE
        }
        like.setOnClickListener{
            unlike.visibility=View.VISIBLE
            like.visibility=View.GONE
        }
        unlike.setOnClickListener{
            unlike.visibility=View.GONE
            like.visibility=View.VISIBLE
        }
    }

    private fun addFav()
    {

    }
    private fun removeFac()
    {

    }

}