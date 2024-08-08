package com.example.quest1

import EndlessScrollListener
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONException

class page3 : AppCompatActivity() {

    private lateinit var userListView: ListView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var adapter: SimpleAdapter
    private var userList = ArrayList<HashMap<String, String>>()
    private var currentPage = 1
    private val perPage = 6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page3)

        val arrowImageView: android.view.View? = findViewById(R.id.page3arrow)
        arrowImageView?.setOnClickListener {
            val intent = Intent(this, Page_2::class.java)
            startActivity(intent)
            finish()
        }

        userListView = findViewById(R.id.listv)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)

        // Initialize the adapter with an empty list
        adapter = SimpleAdapter(
            this,
            userList,
            R.layout.list_item_user,
            arrayOf("name", "email", "avatar"),
            intArrayOf(R.id.userName, R.id.userEmail, R.id.userAvatar)
        )

        adapter.setViewBinder { view, data, _ ->
            if (view.id == R.id.userAvatar) {
                val imageView = view as ImageView
                Picasso.get().load(data as String).into(imageView)
                true
            } else {
                false
            }
        }

        userListView.adapter = adapter

        swipeRefreshLayout.setOnRefreshListener {
            currentPage = 1
            userList.clear()
            fetchDataFromApi()
        }

        userListView.setOnScrollListener(object : EndlessScrollListener() {
            override fun onLoadMore() {
                currentPage++
                fetchDataFromApi()
            }
        })

        // Set the OnItemClickListener to handle item clicks
        userListView.setOnItemClickListener { _, _, position, _ ->
            val selectedUser = userList[position]
            val selectedUserName = selectedUser["name"]

            val userName = intent.getStringExtra("USER_NAME") // Get the original name

            val intent = Intent(this, Page_2::class.java)
            intent.putExtra("SELECTED_USER_NAME", selectedUserName)
            intent.putExtra("USER_NAME", userName) // Pass the original name back to Page_2
            startActivity(intent)
            finish()
        }

        fetchDataFromApi()
    }

    private fun fetchDataFromApi() {
        val url = "https://reqres.in/api/users?page=$currentPage&per_page=$perPage"
        val requestQueue = Volley.newRequestQueue(this)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val dataArray = response.getJSONArray("data")

                    if (dataArray.length() == 0 && currentPage == 1) {
                        Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show()
                        swipeRefreshLayout.isRefreshing = false
                        return@JsonObjectRequest
                    }

                    for (i in 0 until dataArray.length()) {
                        val userObject = dataArray.getJSONObject(i)
                        val userMap = HashMap<String, String>()

                        userMap["email"] = userObject.getString("email")
                        userMap["name"] = "${userObject.getString("first_name")} ${userObject.getString("last_name")}"
                        userMap["avatar"] = userObject.getString("avatar")

                        userList.add(userMap)
                    }

                    adapter.notifyDataSetChanged()
                    swipeRefreshLayout.isRefreshing = false
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                error.printStackTrace()
                swipeRefreshLayout.isRefreshing = false
            }
        )

        requestQueue.add(jsonObjectRequest)
    }
}
