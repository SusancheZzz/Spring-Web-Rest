package com.rntgroup.api.service;

import com.rntgroup.api.entity.AccountEntity;
import com.rntgroup.api.entity.Role;
import com.rntgroup.api.exception.AccountWithAlreadyExistsException;
import com.rntgroup.api.exception.AccountWithUsernameNotFoundException;
import com.rntgroup.api.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

  private final AccountRepository accountRepository;

  public AccountEntity createAccount(AccountEntity account) {
    if (accountRepository.existsByUsername(account.getUsername())) {
      throw new AccountWithAlreadyExistsException("username", account.getUsername());
    }
    if (accountRepository.existsByEmail(account.getEmail())) {
      throw new AccountWithAlreadyExistsException("email", account.getEmail());
    }
    if (accountRepository.existsByEmployeeId(account.getEmployee().getId())) {
      throw new AccountWithAlreadyExistsException("employeeId",
        account.getEmployee().getId().toString());
    }
    return saveAccount(account);
  }

  // Для Spring Security
  public UserDetailsService userDetailsService() {
    return this::getAccountByUsername;
  }

  public AccountEntity getCurrentAccount() {
    var username = SecurityContextHolder.getContext().getAuthentication().getName();
    return getAccountByUsername(username);
  }

  @Secured("ADMIN")
  public Boolean setAdminRoleOfUserByUsername(String username) {
    var account = getAccountByUsername(username);
    account.setRole(Role.ADMIN);
    accountRepository.save(account);
    return true;
  }

  private AccountEntity getAccountByUsername(String username) {
    return accountRepository.findByUsername(username)
      .orElseThrow(() -> new AccountWithUsernameNotFoundException(username));
  }

  private AccountEntity saveAccount(AccountEntity account) {
    return accountRepository.save(account);
  }
}