package com.example.vktask2.model

data class MasterPassword(
    var pass: ByteArray? = null,
    var iv: ByteArray? = null
)
