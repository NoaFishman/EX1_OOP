public class King extends ConcretePiece{
    // constractor that get the player and creat a king
    public King(Player c){
        this.owner=c;
    }
    @Override
    // add to the king the sn that we got
    public void addSN(int x) {
        this.sn=x;
    }
    // return the typ of the king
    public String getType() {return "♔";}
}