package com.example.githubapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class CommitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_commit)

        //Get the views
        val dateTextView: TextView = findViewById(R.id.date_textview)
        val authorNameTextView: TextView = findViewById(R.id.author_name_textview)
        val authorEmailTextView: TextView = findViewById(R.id.author_email_textview)
        val committerNameTextView: TextView = findViewById(R.id.committer_name_textview)
        val messageTextView: TextView = findViewById(R.id.message_textview)
        val avatarImageView: ImageView = findViewById(R.id.author_avatar_imageview)
        val goToUrlBtn: Button = findViewById(R.id.go_to_website)

        //Get the intent and data
        val intent = intent
        val date = intent.getStringExtra(COMMIT_DATE)
        val authorName = intent.getStringExtra(COMMIT_AUTHOR_NAME)
        val authorEmail = intent.getStringExtra(COMMIT_AUTHOR_EMAIL)
        val committerName = intent.getStringExtra(COMMIT_COMMITTER_NAME)
        val commitMessage = intent.getStringExtra(COMMIT_MESSAGE)
        val avatarUrl = intent.getStringExtra(COMMIT_AUTHOR_AVATAR_URL)
        val commitUrl = intent.getStringExtra(COMMIT_URL)

        //Populate the views with the data
        dateTextView.text = date
        authorNameTextView.text = authorName
        authorEmailTextView.text = authorEmail
        committerNameTextView.text = committerName
        messageTextView.text = commitMessage

        //Load image from url
        if (avatarUrl != null) {
            Picasso.get().load(avatarUrl).into(avatarImageView)
        } else {
            //If there is no image, hide the imageview
            avatarImageView.visibility = View.GONE
        }

        //On button click open the website in browser
        goToUrlBtn.setOnClickListener {
            val uriUrl = Uri.parse(commitUrl)
            val urlIntent = Intent(Intent.ACTION_VIEW, uriUrl)
            startActivity(urlIntent)
        }

    }
}