package com.talobin.music.parser.model

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml
data class Identification(

    @Element
        val encoding: Encoding?,

    @PropertyElement
        val creator: String?,

    @Attribute(name = "type")
        val creatorType: String?
)