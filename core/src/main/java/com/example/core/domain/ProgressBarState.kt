package com.example.core.domain

sealed class ProgressBarState{

    object Loadind: ProgressBarState()

    object Idle: ProgressBarState()
}
