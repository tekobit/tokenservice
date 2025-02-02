package com.zufarov.tokenservice.services;

import com.zufarov.tokenservice.repositories.UniqueTokenRepository;
import com.zufarov.tokenservice.util.Base10ToBase64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.List;

@Service
public class UniqueIdService {
    private final UniqueTokenRepository uniqueTokenRepository;
    @Autowired
    public UniqueIdService(UniqueTokenRepository uniqueTokenRepository) {
        this.uniqueTokenRepository = uniqueTokenRepository;
    }

    public String getNextTokens() {
        List<Long> tokens = uniqueTokenRepository.getNextSequenceValue();
        List<String> ids = new ArrayList<>(List.of());
        for (long i: tokens) {
            ids.add(Base10ToBase64.toBase64(i));
        }
        return ids.getFirst();
    }

}
