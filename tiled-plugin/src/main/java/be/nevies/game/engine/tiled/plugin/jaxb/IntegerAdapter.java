/*
 * Copyright (c) Smals
 */
package be.nevies.game.engine.tiled.plugin.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;


/**
 * Adapter for xml type 'xs:integer'.
 * 
 * @author drs
 * 
 * @since 1.0.0
 * 
 */
public class IntegerAdapter extends XmlAdapter<String, Long> {

    

    /**
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
     */
    @Override
    public Long unmarshal(String v) {
        return new Long(v);
    }

    /**
     * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
     */
    @Override
    public String marshal(Long v) {
        return v.toString();
    }

}
