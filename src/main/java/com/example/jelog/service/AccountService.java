package com.example.jelog.service;

import com.example.jelog.domain.account.Account;
import com.example.jelog.repository.AccountRepository;
import com.example.jelog.web.dto.AddAccountRequestDto;
import com.example.jelog.web.dto.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccountService {

    private  final AccountRepository accountRepository;

    public Long save(AddAccountRequestDto requestDto){
        return accountRepository.save(
                Account.builder()
                        .email(requestDto.getEmail())
                        .password(requestDto.getPassword())
                        .userID(requestDto.getUserID())
                        .icon(requestDto.getIcon())
                        .build()
        ).getId();
    }

    public Boolean login(LoginRequestDto requestDto){
        Optional<Account> result = accountRepository.findByEmail(requestDto.getEmail());

        return result.get().getPassword().equals(requestDto.getPassword());
    }

    public Account getAccount(String email){
        return accountRepository.findByEmail(email).get();
    }


}
