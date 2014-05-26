package example.three;

class Future<T>
{
    T result;

    Future(T result)
    {
        this.result = result;
    }

    T get()
    {
        return result;
    }

    static <T> Future<T> unit(T t)
    {
        return new Future<T>(t);
    }

    <R> Future<R> flatMap(Function<T, Future<R>> f)
    {
        return f.apply(result);
    }

    <R> Future<R> map(Function<T, R> f)
    {
        final Function<R, Future<R>> unit = new Function<R, Future<R>>()
        {
            public Future<R> apply(R r)
            {
                return unit(r);
            }
        };
        return flatMap(f.andThen(unit));
    }
}
