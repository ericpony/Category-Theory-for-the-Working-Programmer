package example.three;

abstract class Function<T, R>
{
    abstract R apply(T t);

    static <T, R, S> Function<T, S> compose(final Function<T, R> f, final Function<R, S> g)
    {
        return new Function<T, S>()
        {
            public S apply(T t)
            {
                return g.apply(f.apply(t));
            }
        };
    }

    <S> Function<T, S> andThen(Function<R, S> f)
    {
        return compose(this, f);
    }
}
