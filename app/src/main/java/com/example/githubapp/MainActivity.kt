package com.example.githubapp

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class MainActivity : AppCompatActivity() {

    private val baseUrl = "https://api.github.com/repos/aosp-mirror/platform_build/commits?per_page=25&sort=author-date&page="
    private var pageNumber = 1

    private var commits = arrayListOf<CommitResponse?>()

    private var adapter: CommitListAdapter? = null
    private var recyclerView: RecyclerView? = null

    private var isLoading = false

    private var progressBar: ProgressBar? = null
    private var emptyView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Set up NetworkCallback for checking the internet connection
        setNetworkCallback()

        progressBar = findViewById(R.id.progress_bar)
        emptyView = findViewById(R.id.empty_view)

        //Set up the recyclerview
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = CommitListAdapter(this)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(this)
        recyclerView?.addItemDecoration(
            DividerItemDecoration(
                recyclerView?.context,
                DividerItemDecoration.VERTICAL
            )
        )

        //Check internet connection
        //If connected to internet, request data
        if(NetworkObject.isConnected) {
            //Request the commit data
            requestData()

            //Set up the on item click for the list
            handleItemListClick()

            //Set up the scroll listener, to load more data at the end of the list
            addOnScrollListener()
        } else {
            //Otherwise hide progress bar and show empty view
            progressBar?.visibility = View.GONE
            emptyView?.visibility = View.VISIBLE
        }

    }

    //Setting up the network callback
    private fun setNetworkCallback() {
        val connectivityManager: ConnectivityManager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val builder: NetworkRequest.Builder = NetworkRequest.Builder()

        connectivityManager.registerNetworkCallback(
            builder.build(),
            object: ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    NetworkObject.isConnected = true
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    NetworkObject.isConnected = false
                }
            })
    }

    //Request the JSON data from the url
    private fun requestData() {

        //Append the page number to the base url
        val url = "$baseUrl$pageNumber"

        //If the data is loading, show the progress bar
        if(isLoading) {
            progressBar?.visibility = View.VISIBLE
        }

        //Create the request queue and the request
        val queue = Volley.newRequestQueue(this)
        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->

                val gsonBuilder = GsonBuilder()
                val gson: Gson = gsonBuilder.create()

                for (i in 0 until response.length()) {
                    val commitsResponse =
                        gson.fromJson(response[i].toString(), CommitResponse::class.java)
                    commits.add(commitsResponse)
                }

                adapter?.setCommits(commits)
                isLoading = false
                progressBar?.visibility = View.GONE
            },
            { error ->
                //When error - show an error message in the empty view
                emptyView?.text = resources.getString(R.string.error)
                emptyView?.visibility = View.VISIBLE
                recyclerView?.visibility = View.GONE
                progressBar?.visibility = View.GONE
            }
        )

        //Add the request to the queue
        queue.add(jsonArrayRequest)
    }

    //On list item click - start the commit activity and sent the commit details
    private fun handleItemListClick() {
        adapter?.onItemClick = { commit ->
            val intent = Intent(this, CommitActivity::class.java)

            //Add the commit details to the intent
            intent.putExtra(COMMIT_AUTHOR_NAME, commit.commit.author.name)
            intent.putExtra(COMMIT_AUTHOR_EMAIL, commit.commit.author.email)
            intent.putExtra(COMMIT_COMMITTER_NAME, commit.commit.committer.name)
            intent.putExtra(COMMIT_MESSAGE, commit.commit.message)
            intent.putExtra(COMMIT_AUTHOR_AVATAR_URL, commit.author?.avatar_url)
            intent.putExtra(COMMIT_URL, commit.html_url)

            //Get date in string format and add to the intent
            val dateText = getDateAsString(commit.commit.committer.date)
            intent.putExtra(COMMIT_DATE, dateText)

            startActivity(intent)
        }
    }

    //Set up the scroll listener, that checks if it is the end of the list and then requests more data
    private fun addOnScrollListener() {
        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val manager = recyclerView.layoutManager as LinearLayoutManager

                if (!isLoading) {
                    if (manager != null && manager.findLastCompletelyVisibleItemPosition() == commits.size - 1) {
                        //If at the bottom of the list - get more data
                        //Load new set of data - increase page number and request the data
                        isLoading = true
                        pageNumber++
                        requestData()
                    }
                }
            }
        })

    }
}