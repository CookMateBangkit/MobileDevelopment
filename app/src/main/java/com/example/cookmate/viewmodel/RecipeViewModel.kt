package com.example.cookmate.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cookmate.api.ApiConfig
import com.example.cookmate.response.DataItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

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

private fun filterRecipes(recipes: List<DataItem>, query: String): MutableList<DataItem> {
    val filteredRecipes = mutableListOf<DataItem>()

    val lowercaseQuery = query.lowercase(Locale.getDefault())

    for (recipe in recipes) {
        val recipeName = recipe.name.lowercase(Locale.getDefault())
        val recipeIngredients = recipe.ingredients.lowercase(Locale.getDefault())

        val nameMatches = recipeName.containsWholeWords(lowercaseQuery)
        val ingredientsMatches = recipeIngredients.containsWholeWords(lowercaseQuery)

        if (nameMatches || ingredientsMatches) {
            filteredRecipes.add(recipe)
        }
    }

    return filteredRecipes
}

private fun String.containsWholeWords(query: String): Boolean {
    val words = this.split("\\s".toRegex())
    for (word in words) {
        if (word == query) {
            return true
        }
    }
    return false
}