package aoetk.fxglassfishmonitor.serviceclient;

/**
 * An exception thrown by connection failure.
 * @author aoetk
 */
public class ConnectFailedException extends Exception {

    private int status;

    public ConnectFailedException(Throwable cause) {
        super("Connection failed.", cause);
    }

    public ConnectFailedException(int status, Throwable cause) {
        super("Connection failed. status: " + status, cause);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

}
