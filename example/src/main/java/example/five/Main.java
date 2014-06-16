package example.five;

import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

public class Main {
  static List<RemoteServer> getRemoteServers() {
    return List.of(new RemoteServer("EAN", List.of(new Project("PLUG"), new Project("PLUGDEV"))), new RemoteServer(
      "JAC", List.of(new Project("CONF"), new Project("CONFDEV"))),
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

  public enum Product {
    BAMBOO, CONFLUENCE, CROWD, FECRU, JIRA, STASH
  };

  public static class Datum {
    long time;
    boolean completed;
    Product product;
    String label;

    Datum(long time, boolean completed, Product product, String label) {
      this.time = time;
      this.completed = completed;
      this.product = product;
      this.label = label;
    }

    long getTime() {
      return time;
    }

    boolean isCompleted() {
      return completed;
    }

    Product getProduct() {
      return product;
    }

    String getLabel() {
      return label;
    }
  };

  public static class CountTotal {
    long count;
    long total;

    CountTotal(long total) {
      this.count = 1;
      this.total = total;
    }

    CountTotal(long count, long total) {
      this.count = count;
      this.total = total;
    }

    public static CountTotal make(long count, long total) {
      return new CountTotal(count, total);
    }

    public long getCount() {
      return count;
    }

    public long getTotal() {
      return total;
    }

    public double mean() {
      return (1. * total) / count;
    }
  };

  public static void main(String[] args) {
    List<Datum> data = List.of(new Datum(1, false, Product.CONFLUENCE, "cloud"), new Datum(2, true, Product.JIRA,
      "server"), new Datum(3, false, Product.JIRA, "dataCentre"), new Datum(4, true, Product.STASH, "dataCentre"));
    Monoid<Long> sum = new Monoid<>(0L, (x, y) -> (x + y));
    // Long totalTime = listTimes.fold(sum);
    // Long count = listTimes.map(x -> 1L).fold(sum);
    // Long totalTime = listTimes.foldMap(d -> d.getTime(), sum);
    // CountTotal countTotal = data.foldMap(d -> new CountTotal(d.getTime()),
    // Monoid.product(sum, sum, CountTotal::make, CountTotal::getCount,
    // CountTotal::getTotal));
    CountTotal countTotal = data.foldMapFilter(Datum::isCompleted, d -> new CountTotal(d.getTime()),
      Monoids.Product.product(sum, sum, CountTotal::make, CountTotal::getCount, CountTotal::getTotal));
    System.out.println("total = " + countTotal.getTotal() + ", count = " + countTotal.getCount() + ", mean = "
      + countTotal.mean());

    Function<Product, Long> timePerProduct = data.foldMap(d -> (p -> (p == d.getProduct()) ? d.getTime() : sum.zero()),
      Monoids.Functions.<Product, Long> pointwise(sum));
    for (Product p : Product.values()) {
      System.out.println(p + ": " + timePerProduct.apply(p));
    }

    Set<String> labels = data.foldMap(d -> Collections.singleton(d.getLabel()), Monoids.Sets.<String> union());
    for (String label : labels) {
      System.out.println(label);
    }
  }
}
