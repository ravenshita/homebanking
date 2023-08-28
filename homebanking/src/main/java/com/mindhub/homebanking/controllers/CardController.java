package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CardRepository cardRepository;

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(@RequestParam CardColor color,
                                             @RequestParam CardType type,
                                             Authentication authentication) {

        Client client = clientRepository.findByEmail(authentication.getName());

        if (client.getCards().stream().filter(card -> card.getType() == type).count() >= 3) {
            return new ResponseEntity<>("Client already has 3 cards of this type", HttpStatus.FORBIDDEN);
        }

        if (client.getCards().stream().anyMatch(card -> card.getColor() == color && card.getType() == type)) {
            return new ResponseEntity<>("Client already has a card of the same color and type", HttpStatus.FORBIDDEN);
        }

        int cvv = (int) (Math.random() * 900) + 100;

        StringBuilder numberBuilder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            numberBuilder.append(String.format("%04d", (int) (Math.random() * 10000)));
            if (i < 3) {
                numberBuilder.append("-");
            }
        }
        String number = numberBuilder.toString();

        String cardholder = client.getFirstName() + " " + client.getLastName();

        LocalDate fromDate = LocalDate.now();
        LocalDate thruDate = fromDate.plusYears(5);

        Card newCard = new Card(number, cvv, fromDate, thruDate, cardholder, type, color);
        newCard.setClient(client);
        cardRepository.save(newCard);

        CardDTO newCardDTO = new CardDTO(newCard);
        return new ResponseEntity<>(newCardDTO, HttpStatus.CREATED);

    }

    @GetMapping("/clients/current/cards")
    public ResponseEntity<List<CardDTO>> getClientCards(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());

        List<Card> cards = client.getCards();
        List<CardDTO> cardDTOs = cards.stream().map(CardDTO::new).collect(Collectors.toList());
        return new ResponseEntity<>(cardDTOs, HttpStatus.OK);
    }
}
