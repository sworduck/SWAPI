package com.example.swapi

import android.content.Context

interface RealmProvider{
    fun provide(context: Context)
}