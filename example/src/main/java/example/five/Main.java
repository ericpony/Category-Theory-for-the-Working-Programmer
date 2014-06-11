package example.five;

public class Main {
  static List<RemoteServer> getRemoteServers() {
    return List.of(new RemoteServer("EAN", List.of(new Project("PLUG"), new Project("PLUGDEV"))),
      new RemoteServer("JAC", List.of(new Project("CONF"), new Project("CONFDEV"))),
      new RemoteServer("JDOG", List.of(new Project("FECRU"), new Project("JDEV"))));
  }

  // aka `sequence`
  static <T> Future<List<T>> waitAll(List<Future<T>> futures) {
    return new Future<List<T>>(futures.map(f -> f.get()));
  }

  public static List<Project> fetchAllProjects() {
    List<RemoteServer> remoteServers = getRemoteServers();
    List<Future<Error<List<Project>>>> allEventuallyMaybeProjects = remoteServers.map(server -> server.fetchProjects());
    Future<List<Error<List<Project>>>> eventuallyAllMaybeProjects = waitAll(allEventuallyMaybeProjects);
    Future<List<Project>> eventuallyAllProjects = eventuallyAllMaybeProjects.map(allMaybeProjects -> allMaybeProjects
      .flatMap(maybeProjects -> maybeProjects.getOrElse(List.<Project> empty())));
    return eventuallyAllProjects.get();
  }

  public static void main(String[] args) {
    List<Project> allProjects = fetchAllProjects();
    for (Project project : allProjects) {
      System.out.println(project.getName());
    }
  }
}
