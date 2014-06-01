package example.four;

import java.util.function.Function;

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

    static <T> List<T> unit(T t)
    {
        List<T> single = new List<T>();
        single.add(t);
        return single;
    }

    <R> List<R> flatMap(Function<T, List<R>> f)
    {
        List<R> rs = new List<R>();
        for (T t : impl)
        {
            for(R r : f.apply(t))
            {
                rs.add(r);
            }
        }
        return rs;
    }

    <R> List<R> map(Function<T, R> f)
    {
        return flatMap(f.andThen(r -> unit(r)));
        // return flatMap(t -> unit(f.apply(t)));
    }
}
