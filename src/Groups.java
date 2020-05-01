import java.io.*;
import java.net.*;
import java.util.*;

//CGS B3-B1 Groups implementation
public class Groups {
    String name;
    List<Socket> members = new ArrayList<Socket>();
    public static int groupCount = 0;

    public Groups(String name) {
        this.name = name;
    }
}