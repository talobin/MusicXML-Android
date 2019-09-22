package com.talobin.music.parser.model

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "score-part")
data class ScorePart(
        @Attribute
        val id: String?,
        @PropertyElement(name = "part-name")
        val partname: String?
)