package com.thomas.gestPro.service;


import com.thomas.gestPro.Exception.ResourceNotFoundException;
import com.thomas.gestPro.model.Card;
import com.thomas.gestPro.model.ListCard;
import com.thomas.gestPro.repository.CardRepository;
import com.thomas.gestPro.repository.ListCardRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ListCardService {

    private final ListCardRepository listCardRepository;
    private final CardRepository cardRepository;

    @Autowired
    public ListCardService(ListCardRepository listCardRepository, CardRepository cardRepository) {
        this.listCardRepository = listCardRepository;
        this.cardRepository = cardRepository;
    }

    public ListCard findListCardById(Long id) {
        return listCardRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ListCard not found with id : " + id));
    }

    public Set<Card> getCardsByListCardId(Long id) {
       return findListCardById(id).getCardList();
    }

    @Transactional
    public void createCard(Long listCardId, Card card) {
        ListCard listCard = findListCardById(listCardId);
         card.setListCard(listCard);
        cardRepository.save(card);
        listCard.getCardList().add(card);
        listCardRepository.save(listCard);
    }

    public ListCard updateListCard(Long listCardId, ListCard updateListCard){
       ListCard existingCard = findListCardById(listCardId);
       existingCard.setListCardName(updateListCard.getListCardName());

        return listCardRepository.save(updateListCard);
    }

    public void deleteListCard(Long listCardId) {
        listCardRepository.deleteById(listCardId);
    }


}
