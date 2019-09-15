package com.talobin.musicxml.parser.model

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml


@Xml(name = "part-list")
data class PartList(
        @Element(name = "part-group")
        val partGroupList: List<PartGroup>?,
        @Element(name = "score-part")
        val scorePartList: List<ScorePart>?
)