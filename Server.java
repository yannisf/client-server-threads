import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class Server implements Runnable {

    public static final int LISTEN_PORT = 3000;

    private static final int SERVER_THREADS = 2;
    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(SERVER_THREADS);
    private static final AtomicLong COUNTER = new AtomicLong(0);

    private Socket socket;

    Server(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        long count = COUNTER.incrementAndGet();
        System.out.println("SERVER> RECEIVED REQUEST: " + count);
        try {
            Thread.currentThread().sleep(5000L);
            PrintStream out = new PrintStream(socket.getOutputStream());
            out.println("RESPONSE: " + count);
            System.out.printf("SERVER> SERVED REQUEST: %s\n", count);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(LISTEN_PORT);
        while (true) {
            Socket socket = serverSocket.accept();
            THREAD_POOL.execute(new Server(socket));
        }
    }

}
