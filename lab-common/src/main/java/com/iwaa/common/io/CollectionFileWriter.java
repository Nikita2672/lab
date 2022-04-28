package com.iwaa.common.io;

import java.io.File;

public interface CollectionFileWriter<T> {
    boolean write(File file, T object);
}
