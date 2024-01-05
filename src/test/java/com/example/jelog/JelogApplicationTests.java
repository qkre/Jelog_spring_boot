package com.example.jelog;

import com.example.jelog.domain.account.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class JelogApplicationTests {

    @Autowired
    private AccountRepository accountRepository;

    Account account = new Account("test@test.com", "1234", "test", "1234");


    @Test
    void crud() {

        accountRepository.save(account);
        Optional<Account> result = accountRepository.findByEmail("test");
        System.out.println("result = " + result);
    }

}
