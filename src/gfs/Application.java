package gfs;

public class Application {
    private final Client client = new Client();
    public static void main(String[] args) {
        Application app = new Application();
        app.client.write("test.txt", new Character[]{'a','b','c'});
    }
}
