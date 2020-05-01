import java.io.*;
import java.net.*;
import java.util.*;

public class MessageServer implements Runnable{
    Socket csocket;

    String username;

    int userpreced;                                                                         //to initialize username precedence

    static HashMap<Integer, Socket> hm = new HashMap<Integer,Socket>();                     //to store each client socket

    static HashMap<Socket, String> usernames = new HashMap<Socket, String>();               //and every username

    static int cn = 1;                                                                      //to initialize client number

    static List<Groups> groups = new ArrayList<Groups>();                                   //to implement groups CGS B3-B1

    static List<Topics> topics = new ArrayList<Topics>();                                   //to implement topics CGS A5-A1

    MessageServer(Socket csocket, int userpreced){
        this.csocket = csocket;
        this.userpreced = userpreced;
    }

    public static void main(String args[]) throws Exception{

        ServerSocket ssock = new ServerSocket(5000);         //using port 5000
        System.out.println("Listening");                          //displaying text when we have a successful connection
        while (true){


            Socket sock = ssock.accept();                           //when we accept the client socket
            MessageServer ser = new MessageServer(sock, cn);        //we pass it to the constuctor
            new Thread(ser).start();                                //start the tread
            hm.put(cn,sock);                                  //and add socket to hasmpap
            cn++;                                                   //initialize next client



        }
    }
    public void run() {
        try {


            PrintWriter out;
            BufferedReader in = new BufferedReader(new InputStreamReader(csocket.getInputStream()));    //read input from socket

            username = in.readLine().split(" ")[0];                         //ask for username
            usernames.put(hm.get(userpreced),username);                     //populate second hashmap

            //our code will send a message saying who has joined the group chat
            //and will introduce a welcome message

            System.out.println(username + " has joined the group chat");
            out = new PrintWriter(csocket.getOutputStream(), true);
            out.println("Welcome " + username + " to the group chat!\n" +
                    "If you require assisstance for the commands type 'help'!");


            //user input
            String inputLine;


            //we will follow a pattern for sending messages
            //implement send message to all clients
            String all = "sendtoall";

            //a list describing all commands recognized by our application
            //help commands
            String help = "help";
            String groupAll = "groupAll";
            String topicAll = "topicAll";



            //group related commands
            String create = "create";
            String join = "join";
            String leave = "leave";
            String remove = "remove";


            //extra commands as per assessment intructions



            //command to send a message to a group
            String group = "group";
            //command to send a private message
            String privateM = "private";
            //topic related commands
            String topic = "topic";
            String subscribe = "subscribe";
            String unsubscribe = "unsubscribe";

            while((inputLine=in.readLine()) != null) {
                String a[] = inputLine.split(" ");
                if (a[0].equals(help)){
                    out = new PrintWriter(csocket.getOutputStream(), true);

                    //print the list describing all commands recognized by our application to the user
                    //we will refer to this as the manual

                    out.println("1) To send message to all members of the chat, type 'sendtoall' and your message." +
                            "\n2) To create a new group, type 'create' and the group name." +
                            "\n3) To remove an existing group, type 'remove' and the group name." +
                            "\n4) To join an existing group, type 'join' and the group name." +
                            "\n5) To leave a group type, 'leave ' and the group name." +
                            "\n6) To list all the groups and their members, type 'groupAll'." +
                            "\n7) To send a message to a group, type 'group' and the group name followed by the message." +
                            "\n8) To create a topic, type 'topic' and the topic name you wish to create." +
                            "\n9) To follow a topic, type 'subscribe' and the topic name you wish to subsribe to." +
                            "\n10) To unfollow a topic, type 'unsubscribe' and the topic name." +
                            "\n11) To get the full list of the topics, type 'topicAll'." +
                            "\nRestrictions apply!" +
                            "\nOnly one word allowed for GROUPNAME and TOPICNAME!");



                //implement all selections following the pattern
                } else if(a[0].equals(groupAll)){
                    out = new PrintWriter(csocket.getOutputStream(), true);
                    out.println("The current groups in our server are the following: ");
                    for(int counter = 0; counter< groups.size(); counter++){
                        Groups current_Group = groups.get(counter);
                        out.print(current_Group.name + " has members: ");
                        for(int counter1=0;counter1<current_Group.members.size(); counter1++){
                            out.print(usernames.get(current_Group.members.get(counter1)) + ", ");
                        }
                        out.println("");
                    }



                } else if(a[0].equals(topicAll)){
                    out = new PrintWriter(csocket.getOutputStream(), true);
                    out.println("The current topics of interest are: ");
                    for(int counter = 0; counter< topics.size(); counter++){
                        Topics current_Topic = topics.get(counter);
                        out.print(current_Topic.name + " has subscribers: ");
                        for(int counter1=0;counter1<current_Topic.members.size(); counter1++){
                            out.print(usernames.get(current_Topic.members.get(counter1)) + ", ");
                        }
                        out.println("");
                    }



                } else if (a[0].equals(all)) {

                    //to implement the send to all message in groups and topics
                    //we will use a cache for all sockets receiving our message

                    List<Socket> alreadySent = new ArrayList<Socket>();
                    for(int i =1; i <a.length; i++){

                        //we will check every word with index[i] for every topic
                        //by making an index counter

                        for (int counter = 0; counter < topics.size(); counter++){

                            //now we will implement a print
                            //to all subscribers of a topic

                            if(a[i].equals(topics.get(counter).name)){
                                for (int userID =0;userID<topics.get(counter).members.size(); userID++){
                                    Socket subscriber = topics.get(counter).members.get(userID);
                                    if( !alreadySent.contains(subscriber)) {
                                        out = new PrintWriter(subscriber.getOutputStream(), true);
                                        out.printf("[Topic:%s] %s:" , topics.get(counter).name, usernames.get(csocket));
                                        for (int index = 1; index < a.length; index++) {
                                            out.printf("%s ", a[index]);
                                        }
                                        alreadySent.add(subscriber);
                                        out.println("");
                                        out.flush();
                                    }
                                }
                            }
                        }
                    }
                    for (int counter = 1; counter <= hm.size(); counter++) {

                        Socket ser1 = hm.get(counter);
                        if( !alreadySent.contains(ser1)) {
                            out = new PrintWriter(ser1.getOutputStream(), true);



                            //since in most server based games
                            //the sendtoall is implemented as a channel
                            //I have named our channel World Chat



                            out.printf("[World Chat] %s:" , usernames.get(csocket));
                            for (int i = 1; i < a.length; i++) {
                                out.printf("%s ", a[i]);
                            }
                            out.println("");
                            out.flush();
                        }
                    }



                } else if (a[0].equals(create)) {                                           //implementing the create group functonality
                    out = new PrintWriter(csocket.getOutputStream(), true);
                    int counter = 0;

                    boolean createdAlready = false;                                         //if group already exists throw exception
                    while (counter < Groups.groupCount) {
                        if (groups.get(counter).name.equals(a[1])) {

                            out.println("Group with this name already exists!\n" +
                                    "You can enter the group by typing " +
                                    "'join' and the name of the group");

                            System.out.println(usernames.get(hm.get(userpreced)) +
                                    " attempted to create a group with name " +
                                    a[1] + " but it already existed!");

                            out.flush();
                            createdAlready = true;
                            break;
                        }
                        counter++;
                    }

                    //else create the group
                    //mention the creator of the group joining
                    //mention the group name created

                    if (!(createdAlready)) {
                        groups.add(new Groups(a[1]));
                        groups.get(Groups.groupCount).members.add(csocket);
                        out.println("You are part of the group " +
                                a[1]);
                        System.out.println(usernames.get(hm.get(userpreced)) +
                                " created the group " +
                                a[1] + "!");
                        Groups.groupCount++;
                        out.flush();
                    }

                } else if (a[0].equals(join)) {                                         //implement group join
                                                                                        // we need to check if the group exists
                    boolean found = false;
                    int groupID = 0;                                                    //if group found then group id is saved

                    out = new PrintWriter(csocket.getOutputStream(), true);
                    for (int counter = 0; counter < Groups.groupCount; counter++) {
                        if (groups.get(counter).name.equals(a[1])) {
                            groupID = counter;
                            found = true;
                        }
                    }

                    //create exception if one is trying to join
                    //a group that is not created
                    //or if one is already a part of the group

                    if (!found || groups.get(groupID).members.contains(csocket)) {

                        System.out.println(usernames.get(hm.get(userpreced)) +
                                " has attempted to join a nonexistent group " +
                                a[1] +
                                " or the user is already a part of the group.");

                        out.println("You are already a part of that group\n or " +
                                "no group with such name exists." +
                                a[1] +
                                " exists.\n Try creating the group with command 'create' and the name of the group.");

                    }else if (found) {
                        groups.get(groupID).members.add(csocket);
                        System.out.println(usernames.get(hm.get(userpreced)) +
                                " has joined the group " +
                                a[1] + ".");
                        out.println("You have successfully joined the group " +
                                a[1] + ".");
                    }
                    out.flush();
                    found = false;



                } else if (a[0].equals(leave)) {

                    //before leaving the group chat we need to
                    //check if the group is found i.e. if it already exists
                    boolean found = false;
                    int groupID = 0;

                    out = new PrintWriter(csocket.getOutputStream(), true);
                    for (int counter = 0; counter < Groups.groupCount; counter++) {
                        if (groups.get(counter).name.equals(a[1])) {
                            if (groups.get(counter).members.contains(csocket)) {
                                groupID = counter;
                                found = true;
                            }
                        }
                    }
                    if (found) {
                        groups.get(groupID).members.remove(csocket);
                        System.out.println(usernames.get(hm.get(userpreced)) +
                                " has left the group " +
                                a[1] + "!");
                        out.println("You have successfully left the group " +
                                a[1]);

                    } else if (!found) {
                        System.out.println(usernames.get(hm.get(userpreced)) +
                                " has attempted to leave the group " +
                                a[1] + " but he was not a part of it.");
                        out.println("You were not a part of the group with name " +
                                a[1]);
                    }
                    out.flush();
                    found = false;



                } else if (a[0].equals(remove)) {
                    //we will implement the remove group funciton

                    out = new PrintWriter(csocket.getOutputStream(), true);
                    int counter = 0;
                    boolean createdAlready = false;

                    while (counter < Groups.groupCount) {
                        if (groups.get(counter).name.equals(a[1])) {
                            groups.remove(groups.get(counter));
                            Groups.groupCount--;

                            out.println("Group " + a[1] +
                                    " deleted!");
                            System.out.println(usernames.get(hm.get(userpreced)) +
                                    " deleted a group with the name " +
                                    a[1] + ".");

                            out.flush();
                            createdAlready = true;
                            break;
                        }
                        counter++;
                    }
                    if (!(createdAlready)) {

                        //if created already is false
                        //then return group does not exist exception

                        out.println("Group with name " +
                                a[1] + " does not exist!");
                        System.out.println(usernames.get(hm.get(userpreced)) +
                                " tried to delete the group " +
                                a[1] + " but it does not exist!");
                        out.flush();
                    }



                } else if (a[0].equals(group)){
                    List<Socket> alreadySent = new ArrayList<Socket>();
                    boolean found = false;                                      //check if user is already part of the group
                    int groupID=0;
                    for(int counter = 0; counter<groups.size(); counter ++){
                        if(groups.get(counter).name.equals(a[1])){
                            found = true;
                            groupID = counter;
                        }
                    }
                    if(found){

                        for(int i =2; i <a.length; i++){
                            for (int counter = 0; counter < topics.size(); counter++){
                                if(a[i].equals(topics.get(counter).name)){
                                    for (int userID =0;userID<topics.get(counter).members.size(); userID++){
                                        Socket subscriber = topics.get(counter).members.get(userID);
                                        if( !alreadySent.contains(subscriber)) {
                                            out = new PrintWriter(subscriber.getOutputStream(), true);
                                            out.printf("[Topic:%s|Group:%s] %s:" , topics.get(counter).name, groups.get(groupID).name, usernames.get(csocket));
                                            for (int index = 2; index < a.length; index++) {
                                                out.printf("%s ", a[index]);
                                            }
                                            alreadySent.add(subscriber);
                                            out.println("");
                                            out.flush();
                                        }
                                    }
                                }
                            }
                        }



                        for (int counter = 0; counter < groups.get(groupID).members.size(); counter++){
                            Socket member = groups.get(groupID).members.get(counter);
                            if( !alreadySent.contains(member)) {
                                out = new PrintWriter(member.getOutputStream(), true);
                                out.printf("[%s] %s:" , groups.get(groupID).name, usernames.get(csocket));
                                for (int i = 2; i < a.length; i++) {
                                    out.printf("%s ", a[i]);
                                }
                                out.println("");
                                out.flush();
                            }
                        }
                    }else if(!(found)){

                        //exception message if no such group is found
                        out = new PrintWriter(csocket.getOutputStream(), true);
                        out.printf("You are either not a part of the group %s " +
                                "or such group does not exist!",a[1]);
                        out.println("");
                    }


                } else if (a[0].equals(topic)) {
                    out = new PrintWriter(csocket.getOutputStream(), true);
                    int counter = 0;
                    boolean createdAlready = false;
                    while (counter < Topics.topicCount) {
                        if (topics.get(counter).name.equals(a[1])) {

                            out.println("Topic with this keyword already exists!\n" +
                                    "You can subscribe to the topic by typing " +
                                    "subscribe and the topic name.");
                            System.out.println(usernames.get(hm.get(userpreced)) +
                                    " attempted to create a topic with the keyword " +
                                    a[1] + " but it already existed!");

                            out.flush();
                            createdAlready = true;
                            break;
                        }
                        counter++;
                    }
                    if (!(createdAlready)) {
                        topics.add(new Topics(a[1]));
                        topics.get(Topics.topicCount).members.add(csocket);
                        out.println("You are subscribed to the topic " +
                                a[1]);
                        System.out.println(usernames.get(hm.get(userpreced)) +
                                " created the topic " +
                                a[1] + "!");
                        Topics.topicCount++;
                        out.flush();
                    }



                } else if (a[0].equals(subscribe)) {

                    //code to join group chat, subscribe
                    //first need to check if it exists
                    boolean found = false;
                    int topicID = 0;                                                    //save group id if found

                    out = new PrintWriter(csocket.getOutputStream(), true);
                    for (int counter = 0; counter < Topics.topicCount; counter++) {
                        if (topics.get(counter).name.equals(a[1])) {
                            topicID = counter;
                            found = true;
                        }
                    }
                    if (!found || topics.get(topicID).members.contains(csocket)) {

                        System.out.println(usernames.get(hm.get(userpreced)) +
                                " has attempted to subscribe to a non-existent topic " +
                                a[1] +
                                " or the user is already subscribed to that topic!");
                        out.println("You are already subscribed to that topic" +
                                "\nor no topic with such keyword " +
                                a[1] +
                                " exists.\nTry creating it with command 'topic' and the topic name.");

                    }else if (found) {
                        topics.get(topicID).members.add(csocket);
                        System.out.println(usernames.get(hm.get(userpreced)) +
                                " has subscribed to the topic with keyword " +
                                a[1] + "!");
                        out.println("You have successfully subscribed to the topic with keyword " +
                                a[1]);
                    }
                    out.flush();
                    found = false;



                } else if (a[0].equals(unsubscribe)) {
                    //when unsubscribing / leaving a chat
                    //we need to check if the group/topic exists exists
                    boolean found = false;
                    int topicID = 0;

                    out = new PrintWriter(csocket.getOutputStream(), true);
                    for (int counter = 0; counter < Topics.topicCount; counter++) {
                        if (topics.get(counter).name.equals(a[1])) {
                            if (topics.get(counter).members.contains(csocket)) {
                                topicID = counter;
                                found = true;
                            }
                        }
                    }
                    if (found) {
                        topics.get(topicID).members.remove(csocket);
                        System.out.println(usernames.get(hm.get(userpreced)) +
                                " has unsubscribed to the topic with keyword " +
                                a[1] + "!");
                        out.println("You have successfully unsubscribed to the topic with keyword " +
                                a[1]);
                    } else if (!found) {
                        System.out.println(usernames.get(hm.get(userpreced)) +
                                " has attempted to to unsubscribe to the topic with keyword " +
                                a[1] + " but he is was not subscribed to it!");
                        out.println("You were not subscribed to topic woth keyword " +
                                a[1]);
                    }
                    out.flush();
                    found = false;
                }

                //we need to implement an else statement
                //for all unrecognized command
                //referring you back to the list of
                //commands recognized by our application


                else{
                    out = new PrintWriter(csocket.getOutputStream(), true);
                    out.println("Unknown command." +
                            "\nType 'help' for the help manual.");
                }
            }
        }catch (IOException e){
            System.out.println(e);
        }

    }
}