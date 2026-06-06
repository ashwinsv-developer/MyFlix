package com.myflix.di

import com.myflix.data.repository.remote.movie.MovieRepository
import com.myflix.data.repository.remote.movie.MovieRepositoryImpl
import com.myflix.data.repository.remote.tvseries.TvSeriesRepository
import com.myflix.data.repository.remote.tvseries.TvSeriesRepositoryImpl
import com.myflix.data.repository.remote.artist.ArtistRepository
import com.myflix.data.repository.remote.artist.ArtistRepositoryImpl
import com.myflix.data.repository.remote.celebrity.CelebrityRepository
import com.myflix.data.repository.remote.celebrity.CelebrityRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        movieRepositoryImpl: MovieRepositoryImpl
    ): MovieRepository

    @Binds
    @Singleton
    abstract fun bindTvSeriesRepository(
        tvSeriesRepositoryImpl: TvSeriesRepositoryImpl
    ): TvSeriesRepository

    @Binds
    @Singleton
    abstract fun bindArtistRepository(
        artistRepositoryImpl: ArtistRepositoryImpl
    ): ArtistRepository

    @Binds
    @Singleton
    abstract fun bindCelebrityRepository(
        celebrityRepositoryImpl: CelebrityRepositoryImpl
    ): CelebrityRepository
}
