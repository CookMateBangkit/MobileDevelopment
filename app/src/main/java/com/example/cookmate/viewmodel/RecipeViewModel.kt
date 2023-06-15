package com.example.cookmate.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cookmate.api.ApiConfig
import com.example.cookmate.response.DataItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RecipeViewModel: ViewModel() {
    val resultSucces = MutableLiveData<MutableList<DataItem>>()
    val resultError = MutableLiveData<String>()
    val resultLoading = MutableLiveData<Boolean>()

    fun getRecipe(){
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

    fun searchRecipe(name: String){
        GlobalScope.launch (Dispatchers.IO){
            launch(Dispatchers.Main){
                flow {
                    val response = ApiConfig
                        .getApiService()
                        .searchResep(mapOf("name" to name))
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