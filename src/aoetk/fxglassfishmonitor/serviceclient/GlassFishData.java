package aoetk.fxglassfishmonitor.serviceclient;

import java.util.Map;

/**
 * Data container got from GlassFish API.
 * @author aoetk
 */
public class GlassFishData {

    private String message;

    private String command;

    private String exit_code;

    private Map<String, Object> extraProperties;

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the command
     */
    public String getCommand() {
        return command;
    }

    /**
     * @param command the command to set
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * @return the exit_code
     */
    public String getExit_code() {
        return exit_code;
    }

    /**
     * @param exit_code the exit_code to set
     */
    public void setExit_code(String exit_code) {
        this.exit_code = exit_code;
    }

    /**
     * @return the extraProperties
     */
    public Map<String, Object> getExtraProperties() {
        return extraProperties;
    }

    /**
     * @param extraProperties the extraProperties to set
     */
    public void setExtraProperties(Map<String, Object> extraProperties) {
        this.extraProperties = extraProperties;
    }

}
