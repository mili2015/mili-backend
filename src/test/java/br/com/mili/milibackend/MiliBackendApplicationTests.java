package br.com.mili.milibackend;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MiliBackendApplicationTests {

    @Value("${spring.profiles.active}")
    private String profile;

    @Test
    void test_profile() {
        Assertions.assertEquals("prod", profile, "ATENÇÃO! Profile definido como dev mas deveria ser prod");
    }
}
