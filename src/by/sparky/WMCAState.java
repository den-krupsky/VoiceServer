package by.sparky;

public enum WMCAState {
    NONE(new byte[] {0, 0}),
    WAIT(new byte[] {1, 1}),
    ACCEPT(new byte[] {2, 2}),
    CONNECT(new byte[] {3, 3}),
    DISCONNECT(new byte[] {4, 4});

    private byte[] code;

    WMCAState(byte[] code) {
        this.code = code;
    }

    public byte[] code() {
        return this.code;
    }
}
