package example.two;

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
                        List<Project> projects = maybeProjects.result();
                        return projects;
                    }
                    else
                    {
                        return new List<Project>();
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
