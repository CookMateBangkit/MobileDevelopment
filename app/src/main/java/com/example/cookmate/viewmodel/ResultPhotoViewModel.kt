package com.example.cookmate.viewmodel

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cookmate.api.ApiConfig
import com.example.cookmate.response.DataItem
import com.example.cookmate.response.DataPhotoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ResultPhotoViewModel: ViewModel() {
    val resultSucces = MutableLiveData<MutableList<DataPhotoItem>>()
    val resultError = MutableLiveData<String>()
    val resultLoading = MutableLiveData<Boolean>()

    private var getFile: File? = null

    fun getPhotoRecipe(){

        if (getFile != null){
        val file = getFile as File

        val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )
        GlobalScope.launch (Dispatchers.IO){
            launch(Dispatchers.Main){
                flow {
                    val response = ApiConfig
                        .getApiService()
                        .uploadPhoto(imageMultipart)
                    emit(response)
                }.onStart {
                    resultLoading.value = true
                }.onCompletion {
                    resultLoading.value = false
                }.catch {

                }.collect{
//                    resultSucces.value = it.data
                }
            }
        }
        }

    }
}