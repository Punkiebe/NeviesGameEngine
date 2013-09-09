/*
 * Copyright (c) Smals
 */
package be.nevies.game.engine.tiled.plugin.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;


/**
 * Adapter for xml type 'xs:decimal'.
 * 
 * @author drs
 * 
 * @since 1.0.0
 * 
 */
public class DecimalAdapter extends XmlAdapter<String, Double> {


    @Override
    public Double unmarshal(String v) {
        return new Double(v);
    }

    @Override
    public String marshal(Double v) {
        return v.toString();
    }

}
