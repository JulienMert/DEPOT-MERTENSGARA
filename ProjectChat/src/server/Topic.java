package server;

import java.io.Serializable;

public class Topic implements Serializable {
    private String topicName;
    private int topicNumber;

    public Topic(String topicName, int topicNumber)
    {
        this.topicName = topicName;
        this.topicNumber = topicNumber;

    }

    public String getTopicName() {
        return topicName;
    }

    public int getTopicNumber() {
        return topicNumber;
    }
}
