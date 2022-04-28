package com.iwaa.common.io;

import java.io.File;

public interface CollectionFileReader<T> {
    T read(File file);
}
