package com.example.vktask2.model

data class Site(
    var url: String? = null,
    var pass: ByteArray? = null,
    var iv: ByteArray? = null,
    var icon_url: String? = null
)
