package com.example.cookmate.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cookmate.api.ApiConfig
import com.example.cookmate.response.DataItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ExploreViewModel: ViewModel() {
    val resultSucces = MutableLiveData<MutableList<DataItem>>()
    val resultError = MutableLiveData<String>()
    val resultLoading = MutableLiveData<Boolean>()

//    Masih kurang API buat search


    fun getRecipe(namaResep: String){
        GlobalScope.launch (Dispatchers.IO){
            launch(Dispatchers.Main){
                flow {
                    val response = ApiConfig
                        .getApiService()
                        .recipe()
                    emit(response)
                }.onStart {
                    resultLoading.value = true
                }.onCompletion {
                    resultLoading.value = false
                }.catch {

                }.collect{
                    resultSucces.value = it.data
                }
            }
        }
    }
}