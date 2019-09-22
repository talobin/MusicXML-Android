package com.talobin.music.parser.model

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Xml

@Xml
data class Tied(
        @Attribute
        val number: String?,

        @Attribute
        val orientation: String?,

        //or placement
        @Attribute
        val type: String?
)
