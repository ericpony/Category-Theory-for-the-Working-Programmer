package example.four;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Option<T>
{
    public static <T> Option<T> unit(T t)
    {
        return new Some<T>(t);
    }

    public static <T> Option<T> none()
    {
        return new None<T>();
    }

    public abstract <R> Option<R> map(Function<T, R> f);

    public abstract <R> Option<R> flatMap(Function<T, Option<R>> f);

    public abstract void each(Consumer<T> c);

    public abstract boolean isDefined();

    public abstract T get();

    public abstract T getOrElse(Supplier<T> supplier);

    static class Some<T> extends Option<T>
    {
        final T value;

        Some(T value)
        {
            this.value = value;
        }

        @Override
        public <R> Option<R> map(Function<T, R> f)
        {
            return new Some<R>(f.apply(value));
        }

        @Override
        public <R> Option<R> flatMap(Function<T, Option<R>> f)
        {
            return f.apply(value);
        }

        @Override
        public void each(Consumer<T> c)
        {
            c.accept(value);
        }

        @Override
        public boolean isDefined()
        {
            return true;
        }

        @Override
        public T get()
        {
            return value;
        }

        @Override
        public T getOrElse(Supplier<T> supplier)
        {
            return value;
        }
    }

    static class None<T> extends Option<T>
    {
        None()
        {}

        @Override
        public <R> Option<R> map(Function<T, R> f)
        {
            return new None<R>();
        }

        @Override
        public <R> Option<R> flatMap(Function<T, Option<R>> f)
        {
            return new None<R>();
        }

        @Override
        public void each(Consumer<T> c)
        {}

        @Override
        public boolean isDefined()
        {
            return false;
        }

        @Override
        public T get()
        {
            throw new NoSuchElementException();
        }

        @Override
        public T getOrElse(Supplier<T> supplier)
        {
            return supplier.get();
        }
    }
}
