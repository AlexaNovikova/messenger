package chat.auto;

import java.util.ArrayList;
import java.util.List;

public class BaseAuthService implements AuthService {
    private class Entry {
        private String login;
        private String pass;
        private String nick;

        public Entry(String login, String pass, String nick) {
            this.login = login;
            this.pass = pass;
            this.nick = nick;
        }

        public String getNick() {
            return nick;
        }
    }

    private List<Entry> entries;

    @Override
    public void start() {
        System.out.println("Сервис аутентификации запущен");
    }

    @Override
    public void stop() {
        System.out.println("Сервис аутентификации остановлен");
    }


    public BaseAuthService() {
        entries = new ArrayList<>();
        entries.add(new Entry("login1", "pass1", "Алекс"));
        entries.add(new Entry("login2", "pass2", "Хлоя"));
        entries.add(new Entry("login3", "pass3", "Борис"));
    }

    @Override
    public String getNickByLoginPass(String login, String pass) {
        for (Entry o : entries) {
            if (o.login.equals(login) && o.pass.equals(pass)) return o.nick;
        }
        return null;
    }


    public List<String> contacts(){
        List<String> contacts = new ArrayList<>();
        for (Entry entry : entries) {
            contacts.add(entry.getNick());
        }
        return contacts;
    }
}

