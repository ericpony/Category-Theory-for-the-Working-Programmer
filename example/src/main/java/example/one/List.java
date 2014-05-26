package example.one;

class List<T> implements Iterable<T>
{
    java.util.List<T> impl;

    List()
    {
        impl = new java.util.ArrayList<T>();
    }

    void add(T t)
    {
        impl.add(t);
    }

    public java.util.Iterator<T> iterator()
    {
        return impl.iterator();
    }
}
