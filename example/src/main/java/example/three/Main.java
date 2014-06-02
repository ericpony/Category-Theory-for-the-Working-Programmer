package example.three;

public class Main
{
    static List<RemoteServer> getRemoteServers()
    {
        return List.<RemoteServer> of(new RemoteServer("EAN", new Project("PLUG"), new Project("PLUGDEV")), new RemoteServer("JAC", new Project(
            "CONF"), new Project("CONFDEV")), new RemoteServer("JDOG", new Project("FECRU"), new Project("JDEV")));
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
        final Function<RemoteServer, Future<Error<List<Project>>>> fetchProjects = new Function<RemoteServer, Future<Error<List<Project>>>>()
        {
            public Future<Error<List<Project>>> apply(RemoteServer server)
            {
                return server.fetchProjects();
            }
        };
        final Function<Error<List<Project>>, List<Project>> ignoreErrors = new Function<Error<List<Project>>, List<Project>>()
        {
            public List<Project> apply(Error<List<Project>> maybeProjects)
            {
                return maybeProjects.getOrElse(List.<Project> of());
            }
        };
        final Function<List<Error<List<Project>>>, List<Project>> allIgnoreErrors = new Function<List<Error<List<Project>>>, List<Project>>()
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
        for (Project project : allProjects)
        {
            System.out.println(project.getName());
        }
    }
}
