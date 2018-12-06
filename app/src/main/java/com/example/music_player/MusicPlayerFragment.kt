package com.example.music_player

import android.app.Activity
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import com.example.music_player.model.Song
import kotlinx.android.synthetic.main.fragment_music_player.*
import kotlinx.android.synthetic.main.fragment_music_player.view.*


class MusicPlayerFragment : Fragment() {

    var context: Activity? = null
    var mp: MediaPlayer? = null
    var play_button_text = "play"
    private val mSeekbarUpdateHandler = Handler()
    private var trackMinutes = 0
    private var trackSeconds = 0
    private var maxTrackMinutes = 0
    private var maxTrackSeconds = 0
    private lateinit var currentTrack : Uri
    private var currentTrackIndex: Int = 0
    private var newTrackIndex:Int = 0
    var position = 0
    private var trackName = ""
    private var songList:ArrayList<Song> = ArrayList()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_music_player, container, false)
        context = activity


        if(mp == null){
            currentTrackIndex = newTrackIndex
            currentTrack = Uri.parse(songList[currentTrackIndex].path)
            mp = MediaPlayer.create(context,currentTrack)
            updateViewOnChange(view,0)
        }
        else if(mp!!.isPlaying && currentTrackIndex != newTrackIndex){
            currentTrackIndex = newTrackIndex
            currentTrack = Uri.parse(songList[currentTrackIndex].path)
            mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar)
            mp!!.stop()
            mp = MediaPlayer.create(context,currentTrack)
            updateViewOnChange(view,0)
            mp!!.start()
            mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar,0)
        }
        else if(!mp!!.isPlaying && currentTrackIndex != newTrackIndex){
            currentTrackIndex = newTrackIndex
            currentTrack = Uri.parse(songList[currentTrackIndex].path)
            mp = MediaPlayer.create(context,currentTrack)
            updateViewOnChange(view,0)
            mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar,0)
        }
        else if(mp!!.isPlaying && currentTrackIndex == newTrackIndex){
            //view.seek_bar.progress = position
            mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar,0)
        }

        //Setting view
        view.play_button.text = play_button_text
        updateViewOnChange(view, position)

        //Creating listeners
        view.play_button.setOnClickListener {
            //Pausing player
            if(mp!!.isPlaying) {
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
            playNextSong(view)
        }
        view.previous_track_button.setOnClickListener {


            if(mp!!.currentPosition > 5000)
                mp!!.seekTo(0)
            else if(mp!!.isPlaying)
            {
                mp!!.stop()
                if(currentTrackIndex == 0)
                    currentTrackIndex = songList.size-1
                else
                    currentTrackIndex--
                currentTrack = Uri.parse(songList[currentTrackIndex].path)
                mp = MediaPlayer.create(context, currentTrack)
                runMusicPlayer()
            }
            else{
                if(currentTrackIndex == 0)
                    currentTrackIndex = songList.size-1
                else
                    currentTrackIndex--
                currentTrack = Uri.parse(songList[currentTrackIndex].path)
                mp = MediaPlayer.create(context, currentTrack)
            }
            updateViewOnChange(view,0)

            //Updating music duration
            position = 0
            updateViewOnChange(view,0)
        }
        mp!!.setOnCompletionListener {
            playNextSong(view)
            runMusicPlayer()
        }

        //Method handling seekbar progress
        view.seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if(b){
                    mp!!.seekTo(i*1000)
                    seekBar.progress = i
                }
                seekBar.progress = i
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

    private val mUpdateSeekbar = object : Runnable {
        override fun run() {
            view!!.seek_bar.progress = mp!!.currentPosition /1000
            mSeekbarUpdateHandler.postDelayed(this, 1000)
        }
    }
    private fun runMusicPlayer(){
        play_button_text = "Pause"
        play_button.text = play_button_text
        mp!!.start ()
        mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar,0)
    }
    private fun resumeMusicPlayer(){
        mp!!.seekTo (position)
        mp!!.start()
        play_button_text = "Pause"
        play_button.text = play_button_text
        mSeekbarUpdateHandler.postDelayed(mUpdateSeekbar,0)
    }
    private fun pauseMuicPlayer(){
        mp!!.pause ()
        play_button_text = "Play"
        play_button.text = play_button_text
        mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar)
    }

    fun updateViewOnChange(view : View, position : Int){
        //Calculating max track time
        maxTrackMinutes = (mp!!.duration/1000)/60
        maxTrackSeconds = (mp!!.duration/1000)%60
        view.seek_bar.max = mp!!.duration/1000
        view.track_time.text = "$maxTrackMinutes:$maxTrackSeconds"
        view.seek_bar.progress = position
        view.play_button.text = play_button_text
        trackName = songList[currentTrackIndex].name
        view.track_name.text = trackName
    }
    fun playNextSong(view : View){
        if(mp!!.isPlaying)
        {
            mp!!.stop()
            if(currentTrackIndex == songList.size-1)
                currentTrackIndex = 0
            else
                currentTrackIndex++
            currentTrack = Uri.parse(songList[currentTrackIndex].path)
            mp = MediaPlayer.create(context, currentTrack)
            runMusicPlayer()
        }
        else{
            if(currentTrackIndex == songList.size-1)
                currentTrackIndex = 0
            else
                currentTrackIndex++
            currentTrack = Uri.parse(songList[currentTrackIndex].path)
            mp = MediaPlayer.create(context, currentTrack)
        }
        updateViewOnChange(view,0)
    }
    override fun onStop() {
        super.onStop()
        mSeekbarUpdateHandler.removeCallbacks(mUpdateSeekbar)
    }
    fun setSongList(songs: ArrayList<Song>){
        this.songList = songs
    }
    fun setNewSongIndex(index : Int){
        newTrackIndex = index
    }
}
