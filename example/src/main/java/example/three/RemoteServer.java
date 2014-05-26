package example.three;

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
