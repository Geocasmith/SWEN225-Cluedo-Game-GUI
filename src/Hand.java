import java.util.ArrayList;
import java.util.HashSet;
import java.awt.Graphics;
import java.lang.Object;

public class Hand {
    private String player;
     ArrayList<Card> cards;

    public Hand(){
        cards = new ArrayList<Card>();

    }
    public void addToHand(Card c){
       this.cards.add(c);
    }

    /**
     * Passes in suggested cards, if they match any in hand then add it to a list of matching
     * Check if the list is empty outside and use that to print they cannot deny, if has cards give player option
     * which card they want to deny with.
     * @param suggestionCards
     * @return
     */
    public ArrayList<Card> Suggestion(HashSet<Card> suggestionCards){
        ArrayList<Card> Deny = new ArrayList<Card>();
        for(Card s : suggestionCards){
            for(Card c : this.cards){
                if(s.compare(c)){

                    Deny.add(c);
                }
            }
        }

        return Deny;
    }
    public String printHand(){
        String handString = "| ";
        for(Card c : cards){
            handString+=c.getName()+" | ";
        }

        return handString;
    }
}
