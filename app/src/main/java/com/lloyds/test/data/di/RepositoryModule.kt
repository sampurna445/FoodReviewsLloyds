package com.lloyds.test.data.di

import com.lloyds.test.data.repository.FoodReviewRepositoryImpl
import com.lloyds.test.domain.repository.FoodReviewRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindFoodReviewRepository(impl: FoodReviewRepositoryImpl): FoodReviewRepository
}
