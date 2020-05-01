### MessengerApplication
A messenger application operating with the help of a server that can support multiple clients and can handle intercommunication.
The software is written on Java on the integrated development environment
IntelliJ. The software is run on bash.


Use localhost as your host and port 5000 as your port. The client is created
with the help of sockets and any user of the client can successfully write messages on the command
line. Messages notifying that the server is working, a user has logged in, a greeting to a logged user
are all examples of server responses of our application. The software can also send a message to all
logged users or chat with users inside a group or topic of common interests.

We have defined restricted access and functionality in the form of exceptions and error messages if a
user would try to join or delete a non-existent group, create a name with a space (i.e. John Doe),
subscribe or unsubscribe from a non-existent topic, etc.


### Instructions for Linux

• Open the terminal using Ctrl + Alt + T

• Locate the directory you have installed our software using cd (in our case it will be
cd/mnt/d/cs524_assesment1_AleksandarStefanov51768282/CS3524Messenger/src)

• Compile MessageServer.java and MessageClient.java using javac MessageServer.java and
javac MessageClient.java

• This will compile the files and turn them into .class files

• Run the Server by writing java MessageServer into the console

• Run as many clients as you need by writing java MessageClient into the console


