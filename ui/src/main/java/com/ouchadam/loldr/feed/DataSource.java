package com.ouchadam.loldr.feed;

public interface DataSource<T> {

    int size();

    T get(int position);

}
