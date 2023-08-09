package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository){
		return (args -> {

			Client client1 = new Client("Melba", "Morel", "melbamorel@mindhub.com");
			Client client2 = new Client("Axl", "Rose", "axlrose@mindhub.com");

			Account account1 = new Account("VIN001", LocalDate.now(), 5000.00);

			Account account2 = new Account("VIN002", LocalDate.now().plusDays(1),7500.00);

			Account account3 = new Account("VIN003", LocalDate.now(), 7777.00);

			Transaction transaction1 = new Transaction(TransactionType.DEBITO, -1000.00, "Gucci Store", LocalDateTime.now());
			Transaction transaction2 = new Transaction(TransactionType.CREDITO, +3500.50, "Happy Birthday!", LocalDateTime.now());
			Transaction transaction3 = new Transaction(TransactionType.DEBITO, -800.20, "Too faced store", LocalDateTime.now());
			Transaction transaction4 = new Transaction(TransactionType.CREDITO, +5000.00, "Payday", LocalDateTime.now());
			Transaction transaction5 = new Transaction(TransactionType.CREDITO, 10000.00, "South America Tour", LocalDateTime.now());
			Transaction transaction6 = new Transaction(TransactionType.DEBITO, -260.00, "Johnnie Walker Blue Label", LocalDateTime.now());

			clientRepository.save(client1);
			clientRepository.save(client2);

			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);

			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);

			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			account1.addTransaction(transaction3);
			account2.addTransaction(transaction4);
			account3.addTransaction(transaction5);
			account3.addTransaction(transaction6);


			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);

		});


	}


}
