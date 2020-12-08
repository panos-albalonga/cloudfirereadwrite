package com.albalonga.cloudfirereadwrite;//package com.albalonga.cloudfirereadwrite;

public class Card {
    //private Suit suit;    // S=spades D=Diamonds C=Club H=Hearts
    private String suit;    // S=spades D=Diamonds C=Club H=Hearts
    private String rank;    // 2, 3, 4, 5, 6, 7, 8, 9, 0, J, Q, K, A
    private String lctn;    // DK=Deck, PH=Player Hand, PL, PC, PR= Player Left, Center, Right, TA, TC, TB= Table Above, Center, Below
    private String status;  // C=Closed V=Visible P=Private

    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
        this.lctn = "DK";
        this.status = "C";
    }

    public String getSuit() {
        return suit;
    }

    public String getRank() {
        return rank;
    }

    public String getLctn() {
        return lctn;
    }

    public String getStatus() {
        return status;
    }
}
