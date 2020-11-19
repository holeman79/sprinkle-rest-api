package com.kakaopay.sprinklerestapi.sprinkling.infra;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SecureRandomTokenProviderTest {
    SecureRandomTokenProvider secureRandomTokenProvider = new SecureRandomTokenProvider();

    @Test
    public void generateToken(){
        String token = secureRandomTokenProvider.generateToken();
        assertThat(token).hasSize(3);
    }
}