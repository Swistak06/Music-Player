package com.example.music_player

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_music_player.*
import android.R.attr.orientation
import android.content.res.Configuration


class music_player : AppCompatActivity() {

    private lateinit var mp: MediaPlayer
    private val mSeekbarUpdateHandler = Handler()
    private var trackMinutes = 0
    private var trackSeconds = 0
    private var maxTrackMinutes = 0
    private var maxTrackSeconds = 0
    private var currentTrack = R.raw.extacy

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)
        if(mp == null)
            mp = MediaPlayer.create (this, currentTrack)
        maxTrackMinutes = (mp.duration/1000)/60
        maxTrackSeconds = (mp.duration/1000)%60
        track_time.text = "$maxTrackMinutes:$maxTrackSeconds"
        var position = 0
        play_button.setOnClickListener {
            //Pausing player
            if(mp.isPlaying ()) {
                position = mp.getCurrentPosition ()
                mp.pause ()
                play_button.text = "Play"
                track_name.text = mp.duration.toString()
                mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar)
            }
            //Resuming player
            else if (position != 0)
            {
                mp.seekTo (position)
                mp.start()
                play_button.text = "Pause"
                mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar,0)
            }
            //Running player
            else
            {
                seek_bar.max = mp.duration/1000
                play_button.text = "Pause"
                mp.start ()
                mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar,0)
            }

        }

        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if(b){
                    mp!!.seekTo(i*1000)
                }
                trackMinutes = i / 60
                trackSeconds = i % 60
                if(trackSeconds < 10 )
                    current_track_time.text = "$trackMinutes:0$trackSeconds"
                else
                    current_track_time.text = "$trackMinutes:$trackSeconds"

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
    }

    private val mUpdateSeekbar = object : Runnable {
        override fun run() {
            //track_name.text = mp.currentPosition.toString()
            seek_bar.progress = mp.getCurrentPosition()/1000
            mSeekbarUpdateHandler.postDelayed(this, 1000)
        }
    }




    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
    }
}
