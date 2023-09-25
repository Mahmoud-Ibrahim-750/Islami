package com.mis.route.islami.ui.home.fragments.tasbeeh

class TasbeehCounter private constructor() {
    companion object {
        private var tasbeehCounter: TasbeehCounter? = null

        fun getInstance(): TasbeehCounter {
            return if (tasbeehCounter == null) TasbeehCounter()
            else tasbeehCounter as TasbeehCounter
        }
    }

    var counter = 0
}