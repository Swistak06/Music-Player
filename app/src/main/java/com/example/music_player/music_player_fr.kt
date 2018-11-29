package com.example.music_player

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.app.Activity
import android.media.MediaPlayer
import android.os.Handler
import android.widget.SeekBar
import kotlinx.android.synthetic.main.fragment_music_player.*
import kotlinx.android.synthetic.main.fragment_music_player.view.*


class music_player_fr : Fragment() {

    var context: Activity? = null
    var mp: MediaPlayer? = null
    var play_button_text = "play"
    private val mSeekbarUpdateHandler = Handler()
    private var trackMinutes = 0
    private var trackSeconds = 0
    private var maxTrackMinutes = 0
    private var maxTrackSeconds = 0
    private var currentTrack = R.raw.extacy
    var position = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_music_player, container, false)
        context = activity




        //Handling music player not to duplicate
        if(mp == null)
            mp = MediaPlayer.create(context, currentTrack)

        maxTrackMinutes = (mp!!.duration/1000)/60
        maxTrackSeconds = (mp!!.duration/1000)%60



        //Setting view
        view.play_button.text = play_button_text
        view.track_time.text = "$maxTrackMinutes:$maxTrackSeconds"



        //Creating listeners
        view.play_button.setOnClickListener {
            //            //Pausing player
            if(mp!!.isPlaying ()) {
                //position = mp.getCurrentPosition ()
                mp!!.pause ()
                play_button.text = "Play"
                track_name.text = mp!!.duration.toString()
                mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar)
            }
            //Resuming player
            else if (position != 0)
            {
                mp!!.seekTo (position)
                mp!!.start()
                play_button.text = "Pause"
                mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar,0)
            }
            //Running player
            else
            {
                seek_bar.max = mp!!.duration/1000
                play_button.text = "Pause"
                mp!!.start ()
                mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar,0)
            }
        }

        view.next_track_button.setOnClickListener {
            mp!!.stop()
            mp = MediaPlayer.create(context, currentTrack)
            view.seek_bar.progress = 0
            mp!!.start()


        }
        view.seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if(b){
                    mp!!.seekTo(i*1000)
                }
                trackMinutes = i / 60
                trackSeconds = i % 60
                if(trackSeconds < 10 )
                    view.current_track_time.text = "$trackMinutes:0$trackSeconds"
                else
                    view.current_track_time.text = "$trackMinutes:$trackSeconds"

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })


        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    private val mUpdateSeekbar = object : Runnable {
        override fun run() {
            //track_name.text = mp.currentPosition.toString()
            view!!.seek_bar.progress = mp!!.getCurrentPosition()/1000
            mSeekbarUpdateHandler.postDelayed(this, 1000)
        }
    }
}
