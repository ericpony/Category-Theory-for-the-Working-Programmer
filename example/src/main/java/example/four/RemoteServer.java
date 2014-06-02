package example.four;

class RemoteServer
{
    final String name;
    final List<Project> projects;

    RemoteServer(String name, List<Project> projects)
    {
        this.name = name;
        this.projects = projects;
    }

    Future<Error<List<Project>>> fetchProjects()
    {
        return new Future<Error<List<Project>>>(new Error<List<Project>>(projects));
    }
}
