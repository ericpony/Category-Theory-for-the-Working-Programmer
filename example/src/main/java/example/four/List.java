package example.four;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Singly-linked (cons) list. Prepends to start rather than adds to end (ie.
 * it's an immutable Stack).
 */
public abstract class List<T> implements Iterable<T>
{
    @SafeVarargs
    public static <T> List<T> of(T... ts)
    {
        List<T> result = empty();
        for (int i = ts.length - 1; i >= 0; i--)
        {
            result = result.prepend(ts[i]);
        }
        return result;
    }

    public static <T> List<T> unit(T t)
    {
        return of(t);
    }

    public static <T> List<T> empty()
    {
        return new Empty<>();
    }

    public List<T> prepend(T t)
    {
        return new Node<>(this, t);
    }

    public <R> List<R> flatMap(Function<T, List<R>> f)
    {
        List<R> result = empty();
        for (T t : this.reverse())
        {
            for (R r : f.apply(t).reverse())
            {
                result = result.prepend(r);
            }
        }
        return result;
    }

    public <R> List<R> map(Function<T, R> f)
    {
        return flatMap(f.andThen(r -> unit(r)));
    }

    //
    // abstract
    //

    public abstract boolean isEmpty();

    public abstract T head();

    public abstract List<T> tail();

    public abstract List<T> reverse();

    <X> X foldLeft(X init, BiFunction<X, T, X> f)
    {
        X result = init;
        for (T t : this)
        {
            result = f.apply(result, t);
        }
        return result;
    }

    static class Node<T> extends List<T>
    {
        final List<T> next;
        final T value;

        Node(List<T> next, T value)
        {
            this.next = next;
            this.value = value;
        }

        public T head()
        {
            return value;
        }

        public List<T> tail()
        {
            return next;
        }

        public List<T> reverse()
        {
            return foldLeft(List.<T> empty(), (ts, t) -> ts.prepend(t));
        }

        @Override
        public boolean isEmpty()
        {
            return false;
        }
    }

    static class Empty<T> extends List<T>
    {
        @Override
        public List<T> reverse()
        {
            return this;
        }

        @Override
        public boolean isEmpty()
        {
            return true;
        }

        @Override
        public T head()
        {
            throw new NoSuchElementException();
        }

        @Override
        public List<T> tail()
        {
            throw new NoSuchElementException();
        }
    }

    public Iterator<T> iterator()
    {
        return new Iterator<T>()
        {
            List<T> current = List.this;

            @Override
            public boolean hasNext()
            {
                return !current.isEmpty();
            }

            @Override
            public T next()
            {
                try
                {
                    return current.head();
                }
                finally
                {
                    current = current.tail();
                }
            }
        };
    }

    @Override
    public String toString()
    {
        if (isEmpty())
            return "List()";
        final StringBuilder builder = new StringBuilder("List(");
        this.forEach(t -> builder.append(t).append(","));
        builder.setCharAt(builder.length() - 1, ')');
        return builder.toString();
    }
}
