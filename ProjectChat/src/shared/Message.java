package shared;
//FONCTION NON UTILISEE
public class Message {

    private String message;
    private String author;

    public Message(String message, String author)
    {
        this.message = message;
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }
}
