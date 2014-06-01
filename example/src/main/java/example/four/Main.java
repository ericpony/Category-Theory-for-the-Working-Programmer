package example.four;

import java.util.function.Function;

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
        return new Future<List<T>>(futures.map(f -> f.get()));
    }

    public static List<Project> fetchAllProjects()
    {
        List<RemoteServer> remoteServers = getRemoteServers();
        List<Future<Error<List<Project>>>> allEventuallyMaybeProjects = remoteServers.map(
            server -> server.fetchProjects());
        Future<List<Error<List<Project>>>> eventuallyAllMaybeProjects = waitAll(allEventuallyMaybeProjects);
        Future<List<Project>> eventuallyAllProjects = eventuallyAllMaybeProjects.map(
            allMaybeProjects -> allMaybeProjects.flatMap(
                maybeProjects -> ignoreErrors(maybeProjects, new List<Project>())));
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
