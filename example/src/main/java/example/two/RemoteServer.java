package example.two;

class RemoteServer
{
    final String name;
    final List<Project> projects;

    RemoteServer(String name, Project... projects)
    {
        this.name = name;
        this.projects = List.of(projects);
    }

    Future<Error<List<Project>>> fetchProjects()
    {
        return new Future<Error<List<Project>>>(new Error<List<Project>>(projects));
    }
}
