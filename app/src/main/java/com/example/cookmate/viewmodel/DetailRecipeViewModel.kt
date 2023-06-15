package com.example.cookmate.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cookmate.api.ApiConfig
import com.example.cookmate.response.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class DetailRecipeViewModel: ViewModel() {
    val resultSucces = MutableLiveData<Data>()
    val resultError = MutableLiveData<String>()
    val resultLoading = MutableLiveData<Boolean>()

    fun getDetailStory(id: String) {
        GlobalScope.launch (Dispatchers.IO){
            launch (Dispatchers.Main){
                flow {
                    val response = ApiConfig
                        .getApiService()
                        .recipeDetail(id)
                    emit(response)
                }.onStart {

                }.onCompletion {

                }.catch {

                }.collect{
                    resultSucces.value = it.data
                }
            }
        }
    }
}