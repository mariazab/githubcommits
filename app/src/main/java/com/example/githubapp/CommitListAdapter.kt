package com.example.githubapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CommitListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<CommitListAdapter.CommitViewHolder>() {

    var onItemClick: ((CommitResponse) -> Unit)? = null

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var commits = emptyList<CommitResponse?>()

    inner class CommitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val commitNumberTextView: TextView = itemView.findViewById(R.id.commit_number)
        val authorNameTextView: TextView = itemView.findViewById(R.id.author_textview)
        val dateTextView: TextView = itemView.findViewById(R.id.date_textview)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(commits[adapterPosition]!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommitViewHolder {
        val itemView = inflater.inflate(R.layout.commit_recyclerview_item, parent, false)
        return CommitViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CommitViewHolder, position: Int) {
        val currentCommit = commits[position]
        val commitNumber = position + 1

        //Check if number is bigger than 200 or 100 - then change the size of the number text
        when {
            commitNumber >= 200 -> {
                holder.commitNumberTextView.textSize = 34F
            }
            commitNumber >= 100 -> {
                holder.commitNumberTextView.textSize = 48F
            }
            else -> {
                holder.commitNumberTextView.textSize = 61F
            }
        }

        holder.commitNumberTextView.text = (position + 1).toString()
        if (currentCommit != null) {
            holder.authorNameTextView.text = currentCommit.commit.author.name
            val dateText = getDateAsString(currentCommit.commit.committer.date)
            holder.dateTextView.text = dateText
        }
    }

    internal fun setCommits(commitResponses: ArrayList<CommitResponse?>) {
        this.commits = commitResponses
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = commits.size

}