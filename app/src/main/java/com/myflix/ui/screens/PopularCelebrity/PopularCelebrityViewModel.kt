package com.myflix.ui.screens.PopularCelebrity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.myflix.data.repository.remote.celebrity.CelebrityRepository
import javax.inject.Inject

class PopularCelebrityViewModel @Inject constructor(private val repository: CelebrityRepository) :  ViewModel(){

    val popularcelebrities = repository.popularCelebrities().cachedIn(viewModelScope)

}