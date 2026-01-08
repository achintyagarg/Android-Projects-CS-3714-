package edu.vt.cs3714.spring2023.challenge_03

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

/**
 * ViewModel that contains the live data variables.
 */
class Model : ViewModel() {

    private val valuesLiveData = MutableLiveData<List<Int>>()
    private val valuesLocal = mutableListOf(0, 0, 0)

    private val delay: Long = 1_000
    private val repetitions = 10

    private lateinit var viewModelJob: Job

    //the scope is a must in order to ensure friendly behavior
    private lateinit var ioScope: CoroutineScope

    /**
     * The init method provides a place at the creation of the ViewModel to do some setup, in our
     * case, the creation of Job(), and adding that job to another thread.
     */
    init {
        viewModelScope.launch {
            viewModelJob = Job()
        }
        ioScope = CoroutineScope(Dispatchers.IO + viewModelJob)
        valuesLiveData.value = valuesLocal
    }

    /**
     * Return a random number between 0 to 10
     *
     * @param repetitions Number of times to repeat the random selection
     * @param delay       Time to wait between random selection, in milliseconds
     * @param index       Index of the mutable list to store the random selection
     * @return last integer selected
     */
    suspend fun randomInt(repetitions: Int, delay: Long, index: Int): Int {
        var rand = -1
        for (i: Int in 0..repetitions) {
            delay(delay)
            rand = (0..10).random()

            Log.d("coroutine", "Hello from background thread" + Thread.currentThread().name)

            valuesLocal[index] = rand
            when (index) {
                0 -> Log.i("model_left_model", rand.toString())
                1 -> Log.i("model_middle_model", rand.toString())
                2 -> Log.i("model_right_model", rand.toString())
            }

            valuesLiveData.postValue(valuesLocal)

        }
        return rand
    }

    /**
     * Cancels the current viewModelJob, and then recreates it, adding it to the CoroutineScope
     *
     */
    fun stop() {

        viewModelJob.cancel()
        viewModelJob = Job()
        ioScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    }

    /**
     * Getter for the mutable livedata list
     *
     * @return list of ints
     */
    fun getValues(): MutableLiveData<List<Int>> {
        return valuesLiveData
    }

    /**
     * Controls the selection of the three random numbers concurrently
     *
     */
    fun slotMachineDraw() {

        //Start the process on the ioScope thread (not the UI thread)
        ioScope.launch {

            //Left
            val leftDeferred = async {
                randomInt(repetitions, delay, 0)
            }

            //middle
            val middleDeferred = async {
                randomInt(repetitions, delay, 1)
            }

            //right
            val rightDeferred = async {
                randomInt(repetitions, delay, 2)
            }

            // the .await() method allows the process to have an opportunity to do a final return
            // and for the system to wait until that process ends, since they may not end
            // at the same time.
            valuesLocal[0] = leftDeferred.await()

            valuesLocal[1] = middleDeferred.await()

            valuesLocal[2] = rightDeferred.await()

            //update the live data, so that UI is notified of the change.
            valuesLiveData.postValue(valuesLocal)
        }
    }
}