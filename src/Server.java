import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executors;


public class Server {
    public static void main(String[] args) {
        //port 59001: tcp
        //SG security scan :port 59001
        //Well Known Ports: 0 through 1023.
        //Registered Ports: 1024 through 49151.
        //Dynamic/Private : 49152 through 65535.
        try (ServerSocket listener = new ServerSocket(59001)) {
            //number of maximum players (threads) 
            var pool = Executors.newFixedThreadPool(200);
            Game game = new Game();
            while (true) {
                pool.execute(game.new Player(listener.accept()));
            }
        } 
        catch (IOException e) {
            //printStackTrace is a tool to handle errors and exceptions
            e.printStackTrace();
        }
    }
}

