package com.ouchadam.loldr;

public interface SourceProvider<T, Impl extends DataSource<T>> extends DataSource<T> {

    void swap(Impl source);

}
