package com.example.music_player.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.music_player.R


import com.example.music_player.SongListFragment.OnSongSelectedListener
import com.example.music_player.model.Song

/**
 * [RecyclerView.Adapter] that can display a [Song] and makes a call to the
 * specified [OnSongSelectedListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MySongRecyclerViewAdapter(
    private val songList: ArrayList<Song>,
    private val mListener: OnSongSelectedListener?
) : RecyclerView.Adapter<MySongRecyclerViewAdapter.SongListViewHolder>() {

    class SongListViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var songTV:TextView = itemView.findViewById(R.id.song_name_tv)
        var bandTV:TextView = itemView.findViewById(R.id.band_name_tv)
        var durationTV:TextView = itemView.findViewById(R.id.duration_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongListViewHolder {
        var view = LayoutInflater.from(parent!!.context).inflate(R.layout.fragment_song, parent, false)
        return SongListViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongListViewHolder, position: Int) {
        var song = songList[position]
        holder.songTV.text = song.name
        holder.bandTV.text = song.band
        holder.durationTV.text = song.duration

        holder.itemView.setOnClickListener{
            mListener?.onSongSelection(song, position)
        }
    }

    override fun getItemCount(): Int = songList.size

    fun updateData(songs: ArrayList<Song>){
        songList.clear()
        songList.addAll(songs)
        notifyDataSetChanged()
    }

}
