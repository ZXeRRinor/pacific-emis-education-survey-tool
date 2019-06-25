package fm.doe.national.offline_sync.data.model;

import java.io.Serializable;

public class BtMessage implements Serializable {

    private Type type;
    private String content;

    public BtMessage(Type type, String content) {
        this.type = type;
        this.content = content;
    }

    public Type getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public enum Type implements Serializable {
        REQUEST_SURVEYS, REQUEST_FILLED_SURVEY, RESPONSE_SURVEYS, RESPONSE_FILLED_SURVEY
    }
}
