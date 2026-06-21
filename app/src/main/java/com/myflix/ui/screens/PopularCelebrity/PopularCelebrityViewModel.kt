package com.myflix.ui.screens.PopularCelebrity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.myflix.data.repository.remote.celebrity.CelebrityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class PopularCelebrityViewModel @Inject constructor(private val repository: CelebrityRepository) :  ViewModel(){

    val popularcelebrities = repository.popularCelebrities().cachedIn(viewModelScope)

}