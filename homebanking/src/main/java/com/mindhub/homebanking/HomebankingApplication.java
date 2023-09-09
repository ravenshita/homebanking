package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	/*@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository){
		return (args -> {

			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("melba"));


			Client client2 = new Client("Axl", "Rose", "axlrose@mindhub.com", passwordEncoder.encode("Thejungle66"));


			Client client3 = new Client("admin", "admin", "admin@mindhub.com", passwordEncoder.encode("admin"));


			Account account1 = new Account("VIN-001", LocalDate.now(), 5000.00);

			Account account2 = new Account("VIN-002", LocalDate.now().plusDays(1),7500.00);

			Account account3 = new Account("VIN-003", LocalDate.now(), 7777.00);

			Transaction transaction1 = new Transaction(TransactionType.DEBIT, -1000.00, "Gucci Store", LocalDateTime.now());
			Transaction transaction2 = new Transaction(TransactionType.CREDIT, +3500.50, "Happy Birthday!", LocalDateTime.now());
			Transaction transaction3 = new Transaction(TransactionType.DEBIT, -800.20, "Too faced store", LocalDateTime.now());
			Transaction transaction4 = new Transaction(TransactionType.CREDIT, +5000.00, "Payday", LocalDateTime.now());
			Transaction transaction5 = new Transaction(TransactionType.CREDIT, 10000.00, "South America Tour", LocalDateTime.now());
			Transaction transaction6 = new Transaction(TransactionType.DEBIT, -260.00, "Johnnie Walker Blue Label", LocalDateTime.now());

            Loan loan1 = new Loan("Mortgage", 500000.00, List.of(12,24,36,48,60));
			Loan loan2 = new Loan("Personal", 100000.00, List.of( 6,12,24));
			Loan loan3 = new Loan("Auto", 300000.00, List.of(6,12,24,36));

			ClientLoan clientLoan1 = new ClientLoan( 400000.00, 60);
			ClientLoan clientLoan2 = new ClientLoan(50000.00, 12);
			ClientLoan clientLoan3 = new ClientLoan(100.000, 24);
			ClientLoan clientLoan4 = new ClientLoan(200.000, 36);

			Card card1 = new Card("4000 0012 3456 7899", 292, LocalDate.now(), LocalDate.now().plusYears(5), "Melba Morel", CardType.DEBIT, CardColor.GOLD);
			Card card2 = new Card("5302 6101 2345 6769", 412, LocalDate.now(), LocalDate.now().plusYears(5), "Melba Morel", CardType.CREDIT, CardColor.TITANIUM);
			Card card3 = new Card("4575 6568 5785 6787", 806, LocalDate.now(), LocalDate.now().plusYears(5), "Axl Rose", CardType.CREDIT, CardColor.SILVER);

			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(client3);

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

			client1.addCard(card1);
			client1.addCard(card2);
			client2.addCard(card3);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);

			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);

		});*/

	/*}*/
}
