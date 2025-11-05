package com.project.csgoinfos.ui.highlights

import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.project.csgoinfos.R
import com.project.csgoinfos.model.Map

class HighlightDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_highlight)

        val title = findViewById<TextView>(R.id.title)
        val meta = findViewById<TextView>(R.id.meta)
        val videoView = findViewById<VideoView>(R.id.video)

        val hTitle = intent.getStringExtra("title") ?: "Highlight"
        val hPlayer = intent.getStringExtra("player")
        val hTeam = intent.getStringExtra("team")
        val hEvent = intent.getStringExtra("event")
        val hMap = intent.getStringExtra("map")?.let { Map.getMapName(it) }
        val hVideo = intent.getStringExtra("video")

        title.text = hTitle
        meta.text = listOfNotNull(hPlayer, hTeam, hEvent, hMap).joinToString(" · ")

        if (!hVideo.isNullOrBlank()) {
            val controller = android.widget.MediaController(this)
            controller.setAnchorView(videoView)
            videoView.setMediaController(controller)
            videoView.setVideoURI(Uri.parse(hVideo))
            videoView.requestFocus()
            videoView.start()
        }
    }
}
