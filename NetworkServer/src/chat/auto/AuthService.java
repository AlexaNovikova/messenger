package chat.auto;

import java.util.List;

public interface AuthService {

    String getUsernameByLoginAndPassword(String login, String password);
}