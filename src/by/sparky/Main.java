package by.sparky;

public class Main {

    public static void main(String[] args) throws Exception {
	    new Main().start();
    }

    private void start() throws Exception {
        WMSAService service = new WMSAService();
        service.start();
    }
}
