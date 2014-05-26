package example.one;

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

    public static void main(String[] args)
    {
        List<Project> allProjects = fetchAllProjects();
        for(Project project : allProjects)
        {
            System.out.println(project.getName());
        }
    }
}
