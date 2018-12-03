package com.example.music_player

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.music_player.model.Song
import kotlinx.android.synthetic.main.fragment_music_player.*


class music_player : AppCompatActivity(), SongListFragment.OnSongSelectedListener {

    override fun onSongSelection(item: Song?, position:Int) {
//        if(item != null) Log.d("INFO", item.name + " | " + position)
//        else Log.d("INFO", "null")
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.main_frame, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
        Log.d("INFO", item!!.name)
    }

    val fragment = music_player_fr()
    val songListFragment = SongListFragment()
    val manager:FragmentManager = supportFragmentManager

    private var songList = ArrayList<Song>()
    companion object {
        const val PERMISSION_REQUEST_CODE = 12
    }

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

        if(ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE)
            !=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this@music_player,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        }
        else
            loadSongs()

        val transaction = manager.beginTransaction()
        transaction.add(R.id.main_frame,fragment)
        transaction.add(R.id.main_frame, songListFragment)
        transaction.replace(R.id.main_frame, songListFragment)
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

    override fun onResume() {
        super.onResume()
        songListFragment.updateData(songList)
    }
    fun loadSongs(){
        val songCursor: Cursor? = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            null, null, null, null)
        while (songCursor!= null && songCursor.moveToNext()){
            val songName = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
            val songBand = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
            var songDuration = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
            val songPath = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DATA))
            songDuration = convertDurationInMilisToMinutesAndSeconds(songDuration)
            songList.add(Song(songName,songBand,songDuration,songPath))
        }
    }

    fun convertDurationInMilisToMinutesAndSeconds(duration:String):String{
        val durationInteger: Int = Integer.parseInt(duration)
        var seconds: Int = durationInteger / 1000
        val minutes:Int = seconds / 60
        seconds %= 60
        val secondsString:String
        secondsString = if(seconds < 10) "0" + seconds.toString()
        else seconds.toString()
        return minutes.toString() + ":" + secondsString
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == PERMISSION_REQUEST_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext, "PERMISSION GRANTED", Toast.LENGTH_SHORT).show()
                loadSongs()
            }
        }
    }
}
