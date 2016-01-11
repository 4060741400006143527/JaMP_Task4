package com.epam.jamp.demo.impl;

import com.epam.jamp.demo.api.Example;
import org.apache.log4j.Logger;

public class JarExample implements Example {

    public final static Logger LOGGER = Logger.getLogger(JarExample.class);

    @Override
    public void demo(String string) {
        LOGGER.info("v2 " + string);
    }
}
