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

        //Setting view
        view.play_button.text = play_button_text
        updateViewOnChange(view, position)

        //Creating listeners
        view.play_button.setOnClickListener {
            //            //Pausing player
            if(mp!!.isPlaying) {
                //position = mp.getCurrentPosition ()
                pauseMuicPlayer()
            }
            //Resuming player
            else if (position != 0)
            {
                resumeMusicPlayer()
            }
            //Running player
            else
            {
                runMusicPlayer()
            }
        }

        view.next_track_button.setOnClickListener {

            //changing track
            currentTrack = R.raw.partyzant
            position = 0


            if(mp!!.isPlaying)
            {
                mp!!.stop()
                mp = MediaPlayer.create(context, currentTrack)
                runMusicPlayer()
            }
            else
                mp = MediaPlayer.create(context, currentTrack)


            updateViewOnChange(view,0)
        }

        view.previous_track_button.setOnClickListener {

            currentTrack = R.raw.extacy

            if(mp!!.currentPosition > 5000)
                mp!!.seekTo(0)
            else if(mp!!.isPlaying)
            {
                mp!!.stop()
                mp = MediaPlayer.create(context, currentTrack)
                runMusicPlayer()
            }
            else{
                mp!!.stop()
                mp = MediaPlayer.create(context, currentTrack)
            }

            //Updating music duration
            position = 0
            updateViewOnChange(view,0)


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
            view!!.seek_bar.progress = mp!!.getCurrentPosition()/1000
            mSeekbarUpdateHandler.postDelayed(this, 1000)
        }
    }

    private fun runMusicPlayer(){
        play_button.text = "Pause"
        mp!!.start ()
        mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar,0)
    }

    private fun resumeMusicPlayer(){
        mp!!.seekTo (position)
        mp!!.start()
        play_button.text = "Pause"
        mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar,0)
    }

    private fun pauseMuicPlayer(){
        mp!!.pause ()
        play_button_text = "Play"
        play_button.text = play_button_text
        track_name.text = mp!!.duration.toString()
        mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar)
    }

    fun updateViewOnChange(view : View, position : Int){
        //Calculating max track time
        maxTrackMinutes = (mp!!.duration/1000)/60
        maxTrackSeconds = (mp!!.duration/1000)%60
        view.seek_bar.max = mp!!.duration/1000
        view.track_time.text = "$maxTrackMinutes:$maxTrackSeconds"
        view.seek_bar.progress = position

        //Todo: Track name
    }
}
