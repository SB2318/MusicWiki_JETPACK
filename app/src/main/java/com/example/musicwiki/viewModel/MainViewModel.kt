package com.example.musicwiki.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicwiki.model.Genre
import com.example.musicwiki.model.GenreDetail
import com.example.musicwiki.model.GenreInfo
import com.example.musicwiki.model.Tag
import com.example.musicwiki.services.DataService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel: ViewModel() {

    private val apiService = DataService.lastFmService

    private val _genres = MutableStateFlow<List<Tag>>(emptyList())
    val genres: StateFlow<List<Tag>> = _genres

    val _genreDetail: MutableStateFlow<GenreInfo?> =  MutableStateFlow(null)
    val genreDetail:StateFlow<GenreInfo?> = _genreDetail
    private val apiKey = "ca06b1aafc690c9e16394aca19681fae"

    fun getAllGenres() {
        viewModelScope.launch {
            try {
                val response = apiService.getGenres("tag.getTopTags",apiKey,"json")

               response.enqueue(object: Callback<Genre> {

                    override fun onResponse(call: Call<Genre>, response: Response<Genre>) {

                        val body = response.body();

                        Log.d("Response",body.toString())
                        if(response.isSuccessful){
                           _genres.value= body?.toptags?.tag!!

                        }
                    }
                   /** Handle Network Error, Server Error */
                    override fun onFailure(call: Call<Genre>, t: Throwable) {
                        Log.d("GenreError",t.localizedMessage)
                    }
                })
            } catch (e: Exception) {

                /** Handle Error */
                Log.d("GenreError", e.localizedMessage)
            }
        }
    }


    fun getGenreInfo(tagName:String){

        viewModelScope.launch {
            try{
                val genreInfoResponse= DataService.lastFmService.getTagInfo("tag.getinfo",tagName,apiKey,"json")
                genreInfoResponse.enqueue(object: Callback<GenreDetail> {

                    override fun onResponse(call: Call<GenreDetail>, response: Response<GenreDetail>) {

                        val body = response.body();

                        Log.d("Response",body.toString())
                        if(response.isSuccessful){
                            _genreDetail.value= body?.tag

                        }
                    }
                    /** Handle Network Error, Server Error */
                    override fun onFailure(call: Call<GenreDetail>, t: Throwable) {
                        Log.d("GenreDetailError",t.localizedMessage)
                    }
                })
            }catch(e:Exception){
                /** Handle Error */
                Log.d("GenreDetailError", e.localizedMessage)
            }
        }
    }

}