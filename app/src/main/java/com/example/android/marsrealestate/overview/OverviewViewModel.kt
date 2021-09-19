package com.example.android.marsrealestate.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.marsrealestate.network.MarsApi
import com.example.android.marsrealestate.network.MarsApiFilter
import com.example.android.marsrealestate.network.MarsProperty
import kotlinx.coroutines.launch

enum class MarsApiStatus { LOADING, ERROR, DONE }

/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData String that stores the status of the most recent request
    //private val _status = MutableLiveData<String>()

    // The external immutable LiveData for the request status String
    /*val status: LiveData<String>
        get() = _status*/

    private val _status = MutableLiveData<MarsApiStatus>()
    val status: LiveData<MarsApiStatus>
    get() = _status

    private val _properties = MutableLiveData<List<MarsProperty>>()
    val properties: LiveData<List<MarsProperty>>
        get() = _properties

    private val _navigateToSelectedProperty = MutableLiveData<MarsProperty>()
    val navigateToSelectedProperty: LiveData<MarsProperty>
    get() = _navigateToSelectedProperty

    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getMarsRealEstateProperties(MarsApiFilter.SHOW_ALL)
    }

    fun displayPropertyDetails(marsProperty: MarsProperty){
        _navigateToSelectedProperty.value = marsProperty
    }

    fun displayPropertyDetailsComplete(){
        _navigateToSelectedProperty.value = null
    }

    fun updateFilter(filter: MarsApiFilter){
        getMarsRealEstateProperties(filter)
    }

    /**
     * Sets the value of the status LiveData to the Mars API status.
     */
    private fun getMarsRealEstateProperties(filter: MarsApiFilter) {

        viewModelScope.launch {
            _status.value = MarsApiStatus.LOADING
            try {
                val listResult = MarsApi.retrofitService.getProperties(filter.value)
                _status.value = MarsApiStatus.DONE
                //_status.value = "Success: ${listResult.size} Mars properties retrieved "

                if (listResult.isNotEmpty())
                    _properties.value = listResult
            }
            catch (ex: Exception) {
                //_status.value = "Failure" + ex.message
                _status.value = MarsApiStatus.ERROR
                _properties.value = ArrayList()
            }
        }

        // changed type of 'object' here from Callback<String> to Callback<List<MarsProperty>>
        // as of the comment mentioned in MarsApiService.kt -> in retrofitService object.
        // Also, this whole block below is commented out as we are using coroutine to fetch the data from API
        // using retrofit service, which supports coroutine out-of-box.
        /*MarsApi.retrofitService.getProperties().enqueue(object: Callback<List<MarsProperty>> {
            override fun onResponse(call: Call<List<MarsProperty>>, response: Response<List<MarsProperty>>) {
                _status.value = "Success: ${response.body()?.size} Mars properties retrieved!"
            }

            override fun onFailure(call: Call<List<MarsProperty>>, t: Throwable) {
                _status.value = "Failure" + t.message
            }

        })*/
    }
}
