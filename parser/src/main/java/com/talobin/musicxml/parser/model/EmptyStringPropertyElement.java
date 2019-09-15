package com.talobin.musicxml.parser.model;

import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

/**
 * @author Hannes Dorfmann
 */
@Xml(name = "emptyPropertyTag")
public class EmptyStringPropertyElement {
    @PropertyElement
    String empty;

    @Element
    Identification identification;

}