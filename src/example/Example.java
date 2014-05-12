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

    <R> List<R> map(Function<T, R> f)
    {
        Function<R, List<R>> unit = new Function<R, List<R>>()
        {
            public List<R> apply(R r)
            {
                return unit(r);
            }
        };
        return flatMap(f.andThen(unit));
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
}

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
}

class Future<T>
{
    T t;

    Future(T t)
    {
        this.t = t;
    }

    T get()
    {
        return t;
    }
}

class Project
{
    String name;

    Project(String name)
    {
        this.name = name;
    }

    String getName()
    {
        return name;
    }
}

class RemoteServer
{
    String name;
    List<Project> projects;

    RemoteServer(String name)
    {
        this.name = name;
        this.projects = new List<Project>();
    }

    void addProject(Project project)
    {
        projects.add(project);
    }

    Future<Error<List<Project>>> fetchProjects()
    {
        return new Future<Error<List<Project>>>(new Error<List<Project>>(projects));
    }
}

public class Example
{
    static List<RemoteServer> getRemoteServers()
    {
        List<RemoteServer> servers = new List<RemoteServer>();
        RemoteServer ean = new RemoteServer("EAN");
        ean.addProject(new Project("PLUG"));
        ean.addProject(new Project("PLUGDEV"));
        servers.add(ean);
        RemoteServer jac = new RemoteServer("JAC");
        jac.addProject(new Project("CONF"));
        jac.addProject(new Project("CONFDEV"));
        servers.add(jac);
        RemoteServer jdog = new RemoteServer("JDOG");
        jdog.addProject(new Project("FECRU"));
        jdog.addProject(new Project("JDEV"));
        servers.add(jdog);
        return servers;
    }

    public static List<Project> fetchAllProjectsVersion1()
    {
        List<RemoteServer> remoteServers = getRemoteServers();
        List<Project> allProjects = new List<Project>();
        for(RemoteServer server : remoteServers)
        {
            Future<Error<List<Project>>> eventuallyMaybeProjects = server.fetchProjects();
            Error<List<Project>> maybeProjects  = eventuallyMaybeProjects.get();
            if (maybeProjects.ok())
            {
                List<Project> projects = maybeProjects.result();
                for(Project project : projects)
                {
                    allProjects.add(project);
                }
            }
        }
        return allProjects;
    }

    public static List<Project> fetchAllProjectsVersion2()
    {
        List<RemoteServer> remoteServers = getRemoteServers();
        Function<RemoteServer, Future<Error<List<Project>>>> fetchProjects
            = new Function<RemoteServer, Future<Error<List<Project>>>>()
            {
                public Future<Error<List<Project>>> apply(RemoteServer server)
                {
                    return server.fetchProjects();
                }
            };
        Function<Future<Error<List<Project>>>, List<Project>> getOk
            = new Function<Future<Error<List<Project>>>, List<Project>>()
            {
                public List<Project> apply(Future<Error<List<Project>>> eventuallyMaybeProjects)
                {
                    Error<List<Project>> maybeProjects = eventuallyMaybeProjects.get();
                    if (maybeProjects.ok())
                    {
                        List<Project> projects = maybeProjects.result();
                        return projects;
                    }
                    else
                    {
                        return new List<Project>();
                    }
                }
            };
        List<Future<Error<List<Project>>>> allEventuallyMaybeProjects = remoteServers.map(fetchProjects);
        List<Project> allProjects = allEventuallyMaybeProjects.flatMap(getOk);
        return allProjects;
    }

    public static void main(String[] args)
    {
        List<Project> allProjects = fetchAllProjectsVersion2();
        for(Project project : allProjects)
        {
            System.out.println(project.getName());
        }
    }
}
