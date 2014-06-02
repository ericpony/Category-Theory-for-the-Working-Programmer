package example.two;

public class Main
{
    static List<RemoteServer> getRemoteServers()
    {
        return List.<RemoteServer> of(
            new RemoteServer("EAN", new Project("PLUG"), new Project("PLUGDEV")),
            new RemoteServer("JAC", new Project("CONF"), new Project("CONFDEV")),
            new RemoteServer("JDOG", new Project("FECRU"), new Project("JDEV")));
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
        final Function<Future<Error<List<Project>>>, List<Project>> getOk
            = new Function<Future<Error<List<Project>>>, List<Project>>()
            {
                public List<Project> apply(Future<Error<List<Project>>> eventuallyMaybeProjects)
                {
                    Error<List<Project>> maybeProjects = eventuallyMaybeProjects.get();
                    if (maybeProjects.ok())
                    {
                        return maybeProjects.result();
                    }
                    else
                    {
                        return List.of();
                    }
                }
            };
        List<RemoteServer> remoteServers = getRemoteServers();
        List<Future<Error<List<Project>>>> allEventuallyMaybeProjects = remoteServers.map(fetchProjects);
        List<Project> allProjects = allEventuallyMaybeProjects.flatMap(getOk);
        return allProjects;
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
