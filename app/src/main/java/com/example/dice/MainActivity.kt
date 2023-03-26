package com.example.dice
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnAbout: Button = findViewById(R.id.btnabout)
        val btnNewGame: Button = findViewById(R.id.btnnewgame)

        btnNewGame.setOnClickListener{
            ActivityGameWindow()
        }
        btnAbout.setOnClickListener {
            showAboutPopup()
        }
    }

    private fun showAboutPopup (){
        val dialog = Dialog (this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.about_popup)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun ActivityGameWindow() {
        val intent = Intent(this, ActivityGameWindow::class.java)
        startActivity(intent)
    }
}
