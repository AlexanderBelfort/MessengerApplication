import java.util.*;
import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


//creating MessageClient to implement a
//client application that will connect to
//our Message server
//by using port 5000 and host localhost

public class MessageClient implements Runnable {

    private static Socket clientSocket = null;              //creating our client socket

    private static DataInputStream in_stream = null;       //creating our input stream

    private static PrintStream o_stream = null;           //creating our output stream

    private static BufferedReader inputLine = null;       //to read from character input stream

    private static boolean closed = false;

    public static void main(String[] args){

        //we will implement our port being set to default 5000 and
        //our default host
        //will be set to localhost

        int portNumber = 5000;
        String host = "localhost";

        if(args.length<2)
        {
            System.out.println("Usage: java MessageClient <host> <portNumber>\n" +
                    "The host you are using is = " + host + " and portNumber= " + portNumber);
        }

        else{
            host = args[0];
            portNumber = Integer.valueOf(args[1]).intValue();
        }

        //open a socket on host and port
        //open input and outpust streams

        try{
            clientSocket = new Socket(host,portNumber);

            inputLine = new BufferedReader(new InputStreamReader(System.in));

            o_stream = new PrintStream(clientSocket.getOutputStream());

            in_stream = new DataInputStream(clientSocket.getInputStream());
        }
        catch (UnknownHostException e){
            System.err.println("Unknown host: " + host);
        }
        catch (IOException e){
            System.err.println("Exception: Did not get I/O. Could not connect to host: " + host);
        }

        if(clientSocket != null && o_stream != null && in_stream != null){
            try {
                new Thread(new MessageClient()).start();                             //we create a thread to read from server
                System.out.println("Please enter a username to join the chat." +     //implementing a username with restrictions
                        "\nSpacing not allowed!");                                    //being no spacing allowed
                while (!closed) {                                                    //write to socket using output stream
                    o_stream.println(inputLine.readLine());                          //until we terminate our application
                }
                o_stream.close();
                in_stream.close();
                clientSocket.close();                                               //close all output and input streams
            } catch (IOException e){
                System.err.println("IOException: " + e);
            }
        }
    }

    public void run() {

        //implementing reading from socket
        //print received messages
        //is = data input stream

        String responseLine;
        try{
            while((responseLine = in_stream.readLine())!= null)
            {
                System.out.println(responseLine);
            }
            closed = true;
        }
        catch(IOException e) {
            System.err.println("IOException: "+ e);
        }
    }
}