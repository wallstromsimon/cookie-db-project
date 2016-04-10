package cookie;

public class CookieFactory {
    public static void main(String[] args) {
        Database db = new Database();
        new CookieGUI(db);
    }
}
