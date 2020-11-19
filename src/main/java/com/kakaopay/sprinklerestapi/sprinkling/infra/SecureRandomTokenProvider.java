package com.kakaopay.sprinklerestapi.sprinkling.infra;

import com.kakaopay.sprinklerestapi.sprinkling.domain.TokenProvider;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SecureRandomTokenProvider implements TokenProvider {

    private SecureRandom secureRandom = new SecureRandom();

    private final int TOKEN_LENGTH = 3;
    private final int SINGLE_NUMBER = 0;
    private final int SINGLE_LOWER_ALPHABET = 1;
    private final int SINGLE_UPPER_ALPHABET = 2;

    @Override
    public String generateToken() {
        List<Integer> randomCharacters = getRandomCharacters();
        StringBuilder stringBuilder = new StringBuilder();
        for(Integer randomCharacter : randomCharacters){
            if(randomCharacter == SINGLE_NUMBER){
                stringBuilder.appendCodePoint(getSingleNumber());
            }else if(randomCharacter == SINGLE_LOWER_ALPHABET){
                stringBuilder.appendCodePoint(getSingleLowerAlphabet());
            }else if(randomCharacter == SINGLE_UPPER_ALPHABET){
                stringBuilder.appendCodePoint(getSingleUpperAlphabet());
            }
        }
        return stringBuilder.toString();
    }

    private List<Integer> getRandomCharacters() {
        return secureRandom
                .ints(TOKEN_LENGTH, SINGLE_NUMBER, SINGLE_UPPER_ALPHABET + 1)
                .boxed()
                .collect(Collectors.toList());
    }

    private int getSingleNumber(){
        return getSingleRandomChar('0', '9');
    }
    private int getSingleLowerAlphabet(){
        return getSingleRandomChar('a', 'z');
    }
    private int getSingleUpperAlphabet(){
        return getSingleRandomChar('A', 'Z');
    }

    private int getSingleRandomChar(char min, char max) {
        return secureRandom
                .ints(1, min, max + 1)
                .findFirst()
                .getAsInt();
    }
}
