package com.talobin.musicxml.parser.model

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml
data class ScorePartWise(
        @Attribute
        var version: String?,

        @Element
        var identification: Identification?,

        @Element
        var partList: PartList?,

        @Element
        var parts: List<Part>?
)
