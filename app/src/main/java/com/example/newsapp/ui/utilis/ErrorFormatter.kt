package com.example.newsapp.ui.utilis

import com.example.newsapp.R
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.formatError() =
    if (this.isNoConnexionNetwork()) R.string.no_network_error else R.string.unexpected_error

fun Throwable.isNoConnexionNetwork() =
    this is SocketTimeoutException || this is ConnectException || this is UnknownHostException