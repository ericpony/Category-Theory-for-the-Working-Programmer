package example.six;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

abstract class ConsList<T> implements Iterable<T> {
  private static class Empty<T> extends ConsList<T> {
    public String toString() {
      return "[ ]";
    }

    int size() {
      return 0;
    }

    boolean isEmpty() {
      return true;
    }

    String subToString() {
      return " ]";
    }

    T getHead() {
      throw new NoSuchElementException();
    }

    ConsList<T> getTail() {
      throw new NoSuchElementException();
    }
  }

  private static class Node<T> extends ConsList<T> {
    private T head;
    private ConsList tail;

    Node(T head, ConsList tail) {
      this.head = head;
      this.tail = tail;
    }

    int size() {
      return 1 + tail.size();
    }

    boolean isEmpty() {
      return false;
    }

    public String toString() {
      return "[ " + head + tail.subToString();
    }

    String subToString() {
      return ", " + head + tail.subToString();
    }

    T getHead() {
      return head;
    }

    ConsList<T> getTail() {
      return tail;
    }
  }

  abstract int size();
  abstract boolean isEmpty();

  abstract T getHead();
  abstract ConsList<T> getTail();

  public Iterator<T> iterator() {
    return new Iterator<T>() {
      ConsList<T> current = ConsList.this;
      public boolean hasNext() {
        return !current.isEmpty();
      }
      public T next() {
          T head = current.getHead();
          current = current.getTail();
          return head;
      }

      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }

  abstract String subToString();

  public static <T> ConsList<T> empty() {
    return new Empty<T>();
  }

  public static <T> ConsList<T> cons(T head, ConsList<T> tail) {
    return new Node<T>(head, tail);
  }
}

public class Main {

  static <T> List<T>
  consListToUtilList(ConsList<T> cl) {
    ArrayList<T> ul =
      new ArrayList<T>(cl.size());
    for (T t : cl) {
      ul.add(t);
    }
    return ul;
  }

  static <T> ConsList<T>
  utilListToConsList(List<T> ul) {
    return (ul.isEmpty())
      ? ConsList.<T>empty()
      : ConsList.<T>cons(ul.get(0), utilListToConsList(ul.subList(1, ul.size())));
  }

  public static void main(String[] args) {
    ArrayList<Integer> al = new ArrayList(Arrays.asList(1,2,3));
    System.out.println(al);
    System.out.println(utilListToConsList(al));
    System.out.println(consListToUtilList(utilListToConsList(al)));
  }
}
