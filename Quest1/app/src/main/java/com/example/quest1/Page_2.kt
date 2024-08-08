package com.example.quest1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Page_2 : AppCompatActivity() {

    private lateinit var userbtn: Button
    private lateinit var nametxt: TextView
    private lateinit var usernametxt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page2)

        userbtn = findViewById(R.id.userbtn)
        nametxt = findViewById(R.id.nametxt)
        usernametxt = findViewById(R.id.Usernametxt)

        // Retrieve the name from SharedPreferences and set it to nametxt
        val sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("USER_NAME", "No Name")
        nametxt.text = userName

        // Retrieve the selected user name from the intent (if any) and set it to usernametxt
        val selectedUserName = intent.getStringExtra("SELECTED_USER_NAME")
        if (selectedUserName != null) {
            usernametxt.text = selectedUserName
        }

        val arrowImageView: View = findViewById(R.id.page2arrow)
        arrowImageView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        userbtn.setOnClickListener {
            val intent = Intent(this, page3::class.java)
            startActivity(intent)
        }
    }
}
