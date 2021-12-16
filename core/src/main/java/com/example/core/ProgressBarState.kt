package com.example.core

sealed class ProgressBarState{

    object Loadind: ProgressBarState()

    object Idle: ProgressBarState()
}
