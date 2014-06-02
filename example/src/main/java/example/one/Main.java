package example.one;

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
        List<RemoteServer> remoteServers = getRemoteServers();
        List<Project> allProjects = new List<>();
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

    public static void main(String[] args)
    {
        List<Project> allProjects = fetchAllProjects();
        for(Project project : allProjects)
        {
            System.out.println(project.getName());
        }
    }
}
