package com.example.music_player

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.music_player.adapter.MySongRecyclerViewAdapter

import com.example.music_player.model.Song

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [SongListFragment.OnSongSelectedListener] interface.
 */
class SongListFragment : Fragment() {
    private var songList:ArrayList<Song> = ArrayList()
    private var songListAdapter:MySongRecyclerViewAdapter? = null

    // TODO: Customize parameters
    private var columnCount = 1

    private var listener: OnSongSelectedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val list:ArrayList<Song>? = it.getParcelableArrayList<Song>("songList")
            if(list != null) songList = list
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_song_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                songListAdapter = MySongRecyclerViewAdapter(songList, listener)
                adapter = songListAdapter
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSongSelectedListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnSongSelectedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
    fun updateData(songs: ArrayList<Song>){
        songListAdapter?.updateData(songs)
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnSongSelectedListener {
        // TODO: Update argument type and name
        fun onSongSelection(item: Song?, position:Int)
    }

    companion object {

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(songList:ArrayList<Song>) =
            SongListFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList("songList", songList as ArrayList<out Parcelable>?)
                }
            }
        fun newInstance() =
            SongListFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
