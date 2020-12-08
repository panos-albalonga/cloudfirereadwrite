package com.albalonga.cloudfirereadwrite;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Deck {
    ArrayList<Card> deck = new ArrayList<Card>();
    private TextView tmp;

    public Deck(){
        Card s7 = new Card("Spade", "7");
        Card h7 = new Card("Heart", "7");
        Card d7 = new Card("Diamond", "7");
        Card c7 = new Card("Club", "7");
        Card s8 = new Card("Spade", "8");
        Card h8 = new Card("Heart", "8");
        Card d8 = new Card("Diamond", "8");
        Card c8 = new Card("Club", "8");
        Card s9 = new Card("Spade", "9");
        Card h9 = new Card("Heart", "9");
        Card d9 = new Card("Diamond", "9");
        Card c9 = new Card("Club", "9");
        Card s1 = new Card("Spade", "1");
        Card h1 = new Card("Heart", "1");
        Card d1 = new Card("Diamond", "1");
        Card c1 = new Card("Club", "1");
        Card sJ = new Card("Spade", "J");
        Card hJ = new Card("Heart", "J");
        Card dJ = new Card("Diamond", "J");
        Card cJ = new Card("Club", "J");
        Card sQ = new Card("Spade", "Q");
        Card hQ = new Card("Heart", "Q");
        Card dQ = new Card("Diamond", "Q");
        Card cQ = new Card("Club", "Q");
        Card sK = new Card("Spade", "K");
        Card hK = new Card("Heart", "K");
        Card dK = new Card("Diamond", "K");
        Card cK = new Card("Club", "K");
        Card sA = new Card("Spade", "A");
        Card hA = new Card("Heart", "A");
        Card dA = new Card("Diamond", "A");
        Card cA = new Card("Club", "A");
        deck.add(s7);
        deck.add(h7);
/*
        deck.add(d7);
        deck.add(c7);
        deck.add(s8);
        deck.add(h8);
        deck.add(d8);
        deck.add(c8);
        deck.add(s9);
        deck.add(h9);
        deck.add(d9);
        deck.add(c9);
        deck.add(s1);
        deck.add(h1);
        deck.add(d1);
        deck.add(c1);
        deck.add(sJ);
        deck.add(hJ);
        deck.add(dJ);
        deck.add(cJ);
        deck.add(sQ);
        deck.add(hQ);
        deck.add(dQ);
        deck.add(cQ);
        deck.add(sK);
        deck.add(hK);
        deck.add(dK);
        deck.add(cK);
        deck.add(sA);
        deck.add(hA);
        deck.add(dA);
        deck.add(cA);
*/

/*
        fullDeck.add(s7);
        fullDeck.add(h7);
        fullDeck.add(d7);
        fullDeck.add(c7);
        fullDeck.add(s8);
        fullDeck.add(h8);
        fullDeck.add(d8);
        fullDeck.add(c8);
        fullDeck.add(s9);
        fullDeck.add(h9);
        fullDeck.add(d9);
        fullDeck.add(c9);
        fullDeck.add(s1);
        fullDeck.add(h1);
        fullDeck.add(d1);
        fullDeck.add(c1);
        fullDeck.add(sJ);
        fullDeck.add(hJ);
        fullDeck.add(dJ);
        fullDeck.add(cJ);
        fullDeck.add(sQ);
        fullDeck.add(hQ);
        fullDeck.add(dQ);
        fullDeck.add(cQ);
        fullDeck.add(sK);
        fullDeck.add(hK);
        fullDeck.add(dK);
        fullDeck.add(cK);
        fullDeck.add(sA);
        fullDeck.add(hA);
        fullDeck.add(dA);
        fullDeck.add(cA);
*/
    }
    public void upload(String deckIn){
        String deckCol = deckIn;
//        Log.d("DECKIN", "panos>>"+deckCol+"<<panos");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        for(Card c: deck){
            Map<String, Object> card = new HashMap<>();
            card.put("suit", c.getSuit());
            card.put("rank", c.getRank());
            card.put("lctn", c.getLctn());
            card.put("status", c.getStatus());
            db.collection(deckCol).add(card);
        }
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<Card> deck) {
        this.deck = deck;
    }
}
