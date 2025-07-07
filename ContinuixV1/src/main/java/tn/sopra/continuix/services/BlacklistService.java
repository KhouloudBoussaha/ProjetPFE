package tn.sopra.continuix.services;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BlacklistService {
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    public void revokeToken(String token) {
        blacklistedTokens.add(token);
    }

    public boolean isRevoked(String token) {
        return blacklistedTokens.contains(token);
    }
}
