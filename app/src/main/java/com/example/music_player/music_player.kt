package com.example.music_player

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.fragment_music_player.*


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

        val fragment = music_player_fr()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.main_frame,fragment)
        transaction.addToBackStack(null)
        transaction.commit()



//        play_button.setOnClickListener {
//            //Pausing player
//            if(mp.isPlaying ()) {
//                position = mp.getCurrentPosition ()
//                mp.pause ()
//                play_button.text = "Play"
//                track_name.text = mp.duration.toString()
//                mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar)
//            }
//            //Resuming player
//            else if (position != 0)
//            {
//                mp.seekTo (position)
//                mp.start()
//                play_button.text = "Pause"
//                mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar,0)
//            }
//            //Running player
//            else
//            {
//                seek_bar.max = mp.duration/1000
//                play_button.text = "Pause"
//                mp.start ()
//                mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar,0)
//            }
//
//        }
//
//        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//
//            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
//                if(b){
//                    mp!!.seekTo(i*1000)
//                }
//                trackMinutes = i / 60
//                trackSeconds = i % 60
//                if(trackSeconds < 10 )
//                    current_track_time.text = "$trackMinutes:0$trackSeconds"
//                else
//                    current_track_time.text = "$trackMinutes:$trackSeconds"
//
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar) {
//            }
//
//            override fun onStopTrackingTouch(seekBar: SeekBar) {
//            }
//        })
    }
}
