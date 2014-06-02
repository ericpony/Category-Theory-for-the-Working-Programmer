package example.four;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.Iterator;
import java.util.NoSuchElementException;

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
        return new Empty<T>();
    }

    public List<T> prepend(T t)
    {
        return new Node<T>(this, t);
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

    @Override
    public void forEach(Consumer<? super T> action)
    {
        for (T t : this)
        {
            action.accept(t);
        }
    }

    //
    // abstract
    //

    abstract List<T> next();

    abstract T value();

    abstract boolean isEmpty();

    abstract List<T> reverse();

    static class Node<T> extends List<T>
    {
        final List<T> next;
        final T value;

        Node(List<T> next, T value)
        {
            this.next = next;
            this.value = value;
        }

        @Override
        List<T> next()
        {
            return next;
        }

        @Override
        T value()
        {
            return value;
        }

        List<T> reverse()
        {
            List<T> result = empty();
            for (T t : this)
            {
                result = result.prepend(t);
            }
            return result;
        }

        @Override
        boolean isEmpty()
        {
            return false;
        }
    }

    static class Empty<T> extends List<T>
    {
        @Override
        List<T> next()
        {
            throw new NoSuchElementException();
        }

        @Override
        T value()
        {
            throw new NoSuchElementException();
        }

        @Override
        List<T> reverse()
        {
            return this;
        }

        @Override
        boolean isEmpty()
        {
            return true;
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
                    return current.value();
                }
                finally
                {
                    current = current.next();
                }
            }

            @Override
            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public String toString()
    {
        if (isEmpty())
            return "List()";
        final StringBuilder builder = new StringBuilder("List(");
        this.forEach( t -> builder.append(t).append(",") );
        builder.setCharAt(builder.length() - 1, ')');
        return builder.toString();
    }
}
