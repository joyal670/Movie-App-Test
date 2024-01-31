package com.movieapp.api


object ApiRepositoryProvider {
    fun providerApiRepository(): MainRepository {
        return MainRepository(ApiService.create())
    }
}
