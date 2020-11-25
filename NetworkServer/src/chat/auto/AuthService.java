package chat.auto;

import java.util.List;

public interface AuthService {
    void start();
    String getNickByLoginPass(String login, String pass);
    List<String> contacts();
    void stop();
}
