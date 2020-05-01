import java.io.*;
import java.net.*;
import java.util.*;

//CGS A5-A1 Topics Implementation
public class Topics {
    String name;
    List<Socket> members = new ArrayList<Socket>();
    public static int topicCount = 0;

    public Topics(String name) {
        this.name = name;
    }
}