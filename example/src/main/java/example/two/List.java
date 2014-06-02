package example.two;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;

class List<T> implements Iterable<T>
{
    @SafeVarargs
    public static <T> List<T> of(T... ts)
    {
        return new List<T>(new ArrayList<>(asList(ts)));
    }

    static <T> List<T> unit(T t)
    {
        return of(t);
    }
    
    final java.util.List<T> impl;

    List(java.util.List<T> impl)
    {
        this.impl = unmodifiableList(impl);
    }

    public java.util.Iterator<T> iterator()
    {
        return impl.iterator();
    }

    <R> List<R> flatMap(Function<T, List<R>> f)
    {
        java.util.List<R> rs = new java.util.ArrayList<R>();
        for (T t : impl)
        {
            for(R r : f.apply(t))
            {
                rs.add(r);
            }
        }
        return new List<>(rs);
    }

    <R> List<R> map(Function<T, R> f)
    {
        final Function<R, List<R>> unit = new Function<R, List<R>>()
        {
            public List<R> apply(R r)
            {
                return unit(r);
            }
        };
        return flatMap(f.andThen(unit));
    }
}
