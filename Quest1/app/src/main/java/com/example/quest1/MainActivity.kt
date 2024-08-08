package com.example.quest1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.content.Context

class MainActivity : AppCompatActivity() {

    private lateinit var inputName: EditText
    private lateinit var inputPalindrome: EditText
    private lateinit var checkButton: Button
    private lateinit var nextButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputName = findViewById(R.id.input1)
        inputPalindrome = findViewById(R.id.input2)
        checkButton = findViewById(R.id.checkbtn)
        nextButton = findViewById(R.id.nextbtn)

        checkButton.setOnClickListener {
            val palindromeText = inputPalindrome.text.toString().replace("\\s+".toRegex(), "").lowercase()
            if (isPalindrome(palindromeText)) {
                showAlertDialog("isPalindrome")
            } else {
                showAlertDialog("not palindrome")
            }
        }

        nextButton.setOnClickListener {
            val name = inputName.text.toString()

            // Save the name in SharedPreferences
            val sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("USER_NAME", name)
            editor.apply()

            val intent = Intent(this, Page_2::class.java)
            startActivity(intent)
        }
    }

    private fun isPalindrome(text: String): Boolean {
        val n = text.length
        for (i in 0 until n / 2) {
            if (text[i] != text[n - i - 1]) {
                return false
            }
        }
        return true
    }

    private fun showAlertDialog(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }
}
