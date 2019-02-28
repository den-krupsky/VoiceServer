package by.sparky;

import java.net.InetAddress;

public interface ClientRequestHandler {
    void handle(InetAddress inetAddress);
}
