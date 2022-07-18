public class Card {
    private String name;

    public Card(String name){
        this.name=name;
    }

    public boolean compare(Card c){
        if(this.name.equals(c.name)){
            return true;
        }
        return false;
    }
    public String getName(){
        return name;
    }
}
