package com.inz.inzpro

import android.databinding.ViewDataBinding


interface ViewModelBinder {
    fun bind(bind:ViewDataBinding, vm:BaseViewModel?)
}