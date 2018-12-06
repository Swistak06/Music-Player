package com.example.music_player

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.music_player.model.Song


class music_player : AppCompatActivity(), SongListFragment.OnSongSelectedListener {

    override fun onSongSelection(item: Song?, position:Int) {
//        if(item != null) Log.d("INFO", item.name + " | " + position)
//        else Log.d("INFO", "null")
        musicPlayerFrag.setNewSongIndex(position)
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.main_frame, musicPlayerFrag)
        transaction.addToBackStack(null)
        transaction.commit()
        Log.d("INFO", item!!.name)
    }

    val musicPlayerFrag = MusicPlayerFragment()
    val songListFragment = SongListFragment()
    val manager:FragmentManager = supportFragmentManager

    private var songList = ArrayList<Song>()
    companion object {
        const val PERMISSION_REQUEST_CODE = 12
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)
//        val bundle = Bundle()
//        bundle.putParcelableArrayList("songList",songL)ist
//        musicPlayerFrag.arguments = bundle

        if(ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE)
            !=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this@music_player,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
        }
        else
            loadSongs()

        musicPlayerFrag.setSongList(songList)
        val transaction = manager.beginTransaction()
        transaction.add(R.id.main_frame,musicPlayerFrag)
        transaction.add(R.id.main_frame, songListFragment)
        transaction.replace(R.id.main_frame, songListFragment)
        transaction.addToBackStack(null)
        transaction.commit()
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

    override fun onBackPressed() {
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.main_frame, songListFragment)
        transaction.commit()
    }
}
