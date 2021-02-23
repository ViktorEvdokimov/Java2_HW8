package Server;

import java.util.ArrayList;
import java.util.List;


public class AuthService {
    private final List<Entry> entryList;

    public AuthService() {
        entryList = new ArrayList<>();
        entryList.add(new Entry("L1","P1", "N1"));
        entryList.add(new Entry("L2","P2", "N2"));
        entryList.add(new Entry("L3","P3", "N3"));
        entryList.add(new Entry("L4","P4", "N4"));
        entryList.add(new Entry("L5","P5", "N5"));
        entryList.add(new Entry("L6","P6", "N6"));
    }

    public synchronized String getNickByLoginAndPass(String login, String pass){
        for (Entry entry : entryList){
            if(entry.login.equals(login)&&entry.password.equals(pass)) return entry.nickname;
        }
        return null;
    }

    private class Entry{
        private final String login;
        private final String password;
        private final String nickname;

        public Entry(String login, String password, String nickname) {
            this.login = login;
            this.password = password;
            this.nickname = nickname;
        }
    }

}
