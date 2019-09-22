package com.talobin.music.parser.model

import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml
data class Pitch(
        @PropertyElement
        val step: String?,

        @PropertyElement
        val alter: String?,

        @PropertyElement
        val octave: String?
)