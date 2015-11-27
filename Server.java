import java.net.*;
import java.io.*;

public class Server {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(4444);      //using port
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not listen on port: " + 4444 + ", " + e);
            System.exit(1);
            //non zero is showing that the exit was not good
        }

        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
            if(clientSocket!=null) {  
                System.out.println("Accepted a connection from a client");  
            }            
        } catch (IOException e) {
            System.out.println("Accept failed: " + 4444 + ", " + e);
            System.exit(1);
        }

        try {
            BufferedReader br = new BufferedReader(
                                 new InputStreamReader(clientSocket.getInputStream()));
            //for reading the input streams
            PrintWriter pw = new PrintWriter(
                             new BufferedOutputStream(clientSocket.getOutputStream()));
            //using buffered output stream the app can write bytes to the underlying output stream without necessarily
            //causing a call to the underlying system for each byte written.
            Protocol kks = new Protocol();
            String inputLine, outputLine;

            outputLine = kks.processInput(null);
            pw.println(outputLine);
            pw.flush();

            while ((inputLine = br.readLine()) != null) {
                 outputLine = kks.processInput(inputLine);  //calling the method from the protocol class and the value 
                 //returned is stored in outputline which is then flushed to the printwriter.
                 pw.println(outputLine);
                 pw.flush();
                 //flush ensures that anything written to the writer prior to the call to flush() is written to the
                 //underlying stream, rather than sit in some internal buffer.
                 if (outputLine.equalsIgnoreCase("Bye."))
                    break;
            }
            pw.close();
            br.close();
            clientSocket.close();
            serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}