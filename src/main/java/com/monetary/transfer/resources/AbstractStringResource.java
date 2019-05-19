package com.monetary.transfer.resources;

import java.util.ResourceBundle;

/**
 * Basic class for building file-based resources repositories.
 */
abstract class AbstractStringResource {
    /**
     * Bundle with string resources.
     */
    private final ResourceBundle strings;

    /**
     * Constructs instance and loads specified resource file.
     *
     * @param path path to resource file
     */
    AbstractStringResource(String path) {
        strings = ResourceBundle.getBundle(path);
    }

    public final String lookup(String key) {
        return strings.getString(key);
    }
}
