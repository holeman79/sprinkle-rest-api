package com.kakaopay.sprinklerestapi.sprinkling.infra;

import com.kakaopay.sprinklerestapi.sprinkling.domain.TokenProvider;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SecureRandomTokenProviderTest {
    TokenProvider secureRandomTokenProvider = new SecureRandomTokenProvider();

    @Test
    public void generateToken(){
        String token = secureRandomTokenProvider.generateToken();
        assertThat(token).hasSize(3);
    }
}