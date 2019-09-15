package com.talobin.musicxml.parser.model

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml
data class Note(
        @Element
        val pitch: Pitch?,

        @PropertyElement
        val duration: String?,

        @PropertyElement
        val voice: String?,

        @PropertyElement
        val type: String?,
        @PropertyElement
        val stem: String?,

        @PropertyElement
        val accidental: String?,

        @PropertyElement
        val unpitched: String?,

        @PropertyElement
        val staff: String?,

        @PropertyElement
        val chord: String?,

        @PropertyElement
        val rest: String?,

        @Element
        val notation: Notation?,

        @Element
        val beam: Beam?
)
