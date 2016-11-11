import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client implements Runnable {

    private static final int CLIENT_THREADS = 10;
    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(CLIENT_THREADS);

    private Socket socket;

    Client(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))){
            System.out.println("CLIENT> RECEIVED:" + br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        while (true) {
            Socket socket = new Socket("localhost", Server.LISTEN_PORT);
            THREAD_POOL.execute(new Client(socket));
        }
    }

}
