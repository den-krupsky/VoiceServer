package by.sparky;

public class Main {

    public static void main(String[] args) {
	    new Main().start();
    }

    private void start() {
        ServerThread server = new ServerThread(8888, "Sparky Server");
        server.start();
    }
}
