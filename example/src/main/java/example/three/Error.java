package example.three;

class Error<T>
{
    boolean ok;
    String message;
    T result;

    Error(String message)
    {
        this.ok = false;
        this.message = message;
    }

    Error(T result)
    {
        this.ok = true;
        this.result = result;
    }

    boolean ok()
    {
        return ok;
    }

    String message()
    {
        return message;
    }

    T result()
    {
        return result;
    }

    <R> Error<R> flatMap(Function<T, Error<R>> f)
    {
        if (this.ok())
        {
            return f.apply(result);
        }
        else
        {
            return new Error<R>(message);
        }
    }
}
