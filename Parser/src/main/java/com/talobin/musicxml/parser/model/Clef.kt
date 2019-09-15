package com.talobin.musicxml.parser.model

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml
data class Clef(
        @Attribute
        val number: String?,

        @PropertyElement
        val sign: String?,

        @PropertyElement
        val line: String?
)