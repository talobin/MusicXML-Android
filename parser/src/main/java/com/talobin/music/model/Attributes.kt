package com.talobin.music.parser.model

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml
data class Attributes(
    @PropertyElement
        val divisions: String?,

    @PropertyElement
        val staves: String?,

    @Element
        val time: Time?,

    @Element
        val clef: Clef?
)