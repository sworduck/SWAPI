package com.example.swapi.repository

import com.example.swapi.api.ApiHelper

class MainRepository(private val apiHelper: ApiHelper) {
    suspend fun getUsers(page:Int) = apiHelper.getUsers(page)
}