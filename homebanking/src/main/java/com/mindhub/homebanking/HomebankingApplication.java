package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository){
		return (args -> {

			Client client1 = new Client();
			client1.setFirstName("Duff");
			client1.setLastName("McKagan");
			client1.setEmail("duffmckagan@mindhub.com");

			Client client2 = new Client();
			client2.setFirstName("Axl");
			client2.setLastName("Rose");
			client2.setEmail("axlrose@mindhub.com");

			clientRepository.save(client1);
			clientRepository.save(client2);

		});

	}
}
