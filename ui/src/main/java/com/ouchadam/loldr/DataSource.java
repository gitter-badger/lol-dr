package com.ouchadam.loldr;

public interface DataSource<T> {

    DataSource EMPTY = new DataSource() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public Object get(int position) {
            throw new IllegalAccessError("Tried to get some an empty datasource, tut tut");
        }

        @Override
        public void close() {
            // do nothing
        }
    };

    int size();

    T get(int position);

    void close();
}
