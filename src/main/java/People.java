import javax.websocket.Session;
import java.util.HashMap;

public class People {
    private static People people = new People();

    public static People getInstance() {
        return people;
    }

    private HashMap<String, Session> hashMap = new HashMap<>();

    public HashMap<String, Session> getSite() {
        return hashMap;
    }


}
