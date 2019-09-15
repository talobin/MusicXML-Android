package com.talobin.musicxml.parser.model

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml
data class Encoding(

        @Element(name = "software")
        val softwareList: List<Software>,


        @PropertyElement(name = "encoding-date")
        val encodingDate: String?,


        @PropertyElement
        val supports: String?
)