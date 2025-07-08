package pl.kurs.peopleapp.security;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 3;
    private static final Duration BLOCK_DURATION = Duration.ofMinutes(10);
    private static final Duration WINDOW = Duration.ofMinutes(5);

    private final Map<String, List<LocalDateTime>> attempts = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> blocked = new ConcurrentHashMap<>();

    public void loginFailed(String username) {
        LocalDateTime now = LocalDateTime.now();
        attempts.computeIfAbsent(username, k -> new ArrayList<>()).add(now);
        attempts.get(username).removeIf(t -> t.isBefore(now.minus(WINDOW)));

        if (attempts.get(username).size() >= MAX_ATTEMPTS) {
            blocked.put(username, now.plus(BLOCK_DURATION));
        }
    }

    public void loginSucceeded(String username) {
        attempts.remove(username);
        blocked.remove(username);
    }

    public boolean isBlocked(String username) {
        LocalDateTime until = blocked.get(username);
        if (until == null) {
            return false;
        }
        if (until.isBefore(LocalDateTime.now())) {
            blocked.remove(username);
            return false;
        }
        return true;
    }
}
