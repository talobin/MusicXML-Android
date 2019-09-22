package com.talobin.music.parser.model

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml
data class Notation(
    @Element(name = "tied")
        val tiedList: List<Tied>?,

    @Element(name = "slur")
        val slurList: List<Slur>?,

    @Element
        val articulations: Articulations?
)