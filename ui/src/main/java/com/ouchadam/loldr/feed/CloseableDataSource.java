package com.ouchadam.loldr.feed;

public interface CloseableDataSource<T> extends DataSource<T> {

    void close();

}
