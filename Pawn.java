public class Pawn extends ConcretePiece{
    // pawn constractor that get a player an creat a new pawn
    public Pawn(Player p) {this.owner = p;}
    // add to the pawn the sn we got
    @Override
    public void addSN(int x) {
        this.sn=x;
    }
    // return the typ of the pawn by his player
    public String getType (){
        if(owner.isPlayerOne())
            return "♙";
        else
            return "♟︎";
    }

}