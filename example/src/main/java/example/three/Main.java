package example.three;

public class Main
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

    static <T> T ignoreErrors(Error<T> maybe, T fallback)
    {
        if (maybe.ok())
        {
            return maybe.result();
        }
        else
        {
            return fallback;
        }
    }

    static <T> Future<List<T>> waitAll(List<Future<T>> futures)
    {
        final Function<Future<T>, T> get = new Function<Future<T>, T>()
        {
            public T apply(Future<T> future)
            {
                return future.get();
            }
        };
        return new Future<List<T>>(futures.map(get));
    }

    public static List<Project> fetchAllProjects()
    {
        final Function<RemoteServer, Future<Error<List<Project>>>> fetchProjects
            = new Function<RemoteServer, Future<Error<List<Project>>>>()
            {
                public Future<Error<List<Project>>> apply(RemoteServer server)
                {
                    return server.fetchProjects();
                }
            };
        final Function<Error<List<Project>>, List<Project>> ignoreErrors
            = new Function<Error<List<Project>>, List<Project>>()
            {
                public List<Project> apply(Error<List<Project>> maybeProjects)
                {
                    return ignoreErrors(maybeProjects, new List<Project>());
                }
            };
        final Function<List<Error<List<Project>>>, List<Project>> allIgnoreErrors
            = new Function<List<Error<List<Project>>>, List<Project>>()
            {
                public List<Project> apply(List<Error<List<Project>>> allMaybeProjects)
                {
                    return allMaybeProjects.flatMap(ignoreErrors);
                }
            };
        List<RemoteServer> remoteServers = getRemoteServers();
        List<Future<Error<List<Project>>>> allEventuallyMaybeProjects = remoteServers.map(fetchProjects);
        Future<List<Error<List<Project>>>> eventuallyAllMaybeProjects = waitAll(allEventuallyMaybeProjects);
        Future<List<Project>> eventuallyAllProjects = eventuallyAllMaybeProjects.map(allIgnoreErrors);
        return eventuallyAllProjects.get();
    }

    public static void main(String[] args)
    {
        List<Project> allProjects = fetchAllProjects();
        for(Project project : allProjects)
        {
            System.out.println(project.getName());
        }
    }
}
