package example.one;

import java.util.Arrays;
import java.util.ArrayList;

class List<T> implements Iterable<T>
{
    @SafeVarargs
    public static <T> List<T> of(T... ts)
    {
        return new List<T>(new ArrayList<>(Arrays.asList(ts)));
    }

    final java.util.List<T> impl;

    List(java.util.List<T> impl)
    {
        this.impl = impl;
    }

    List()
    {
        this.impl = new ArrayList<>();
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
