package com.ouchadam.loldr;

public interface SourceProvider<T, Impl extends DataSource<T>> {

    void swap(Impl source);

    T get(int position);

    int size();

}
