package com.fish.readymanager

class ReadyManager() {
    private var observableReady = Observable(false)

    private var jobsTagList = HashSet<String>()

    constructor(tagsJobs: Array<String>) : this() {
        jobsTagList.addAll(tagsJobs)
        refreshReady()
    }

    fun isReady() = observableReady.value

    fun addTagJob(tag: String) {
        jobsTagList.add(tag)
        refreshReady()
    }

    fun apply(tag: String) {
        jobsTagList.remove(tag)
        refreshReady()
    }

    fun addListener(observer: Observer<Boolean>) {
        observableReady.addObserver(observer)
    }

    private fun refreshReady() {
        val currentReady = jobsTagList.isEmpty()
        if (currentReady != observableReady.value)
            observableReady.value = currentReady
    }


}
