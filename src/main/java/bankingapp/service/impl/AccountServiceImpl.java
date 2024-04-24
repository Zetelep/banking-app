package bankingapp.service.impl;

import bankingapp.dto.AccountDto;
import bankingapp.entity.Account;
import bankingapp.mapper.AccountMapper;
import bankingapp.repository.AccountRepository;
import bankingapp.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    private AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account account = AccountMapper.mapToAccount(accountDto);
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto getAccountById(Long id) {
       Account account =  accountRepository.findById(id).orElseThrow(()->new RuntimeException("Account doesn't exist"));
        return AccountMapper.mapToAccountDto(account);
    }

    @Override
    public AccountDto deposit(Long id, double amount) {
        Account account =  accountRepository.findById(id).orElseThrow(()->new RuntimeException("Account doesn't exist"));
        double total = account.getBalance()+ amount;
        account.setBalance(total);
        Account savedAccount = accountRepository.save(account);

        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto withdraw(Long id, double amount) {
        Account account =  accountRepository.findById(id).orElseThrow(()->new RuntimeException("Account doesn't exist"));
        if(amount > account.getBalance()){
            throw new IllegalStateException("Not enough balance");
        }
        double total = account.getBalance() - amount;
        account.setBalance(total);
        Account savedAccount = accountRepository.save(account);

        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public List<AccountDto> getAllAccount() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map(AccountMapper::mapToAccountDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAccount(Long id) {
        Account account =  accountRepository.findById(id).orElseThrow(()->new RuntimeException("Account doesn't exist"));
        accountRepository.deleteById(id);
    }
}
