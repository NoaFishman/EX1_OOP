public class Pawn extends ConcretePiece{
private int kills=0;

//    public Pawn(Player p, Position po) {
//        this.owner=p;
//        this.pos=po;
//        this.steps.add(po);
//
//    }
    public Pawn(ConcretePiece p) {
        this.owner=p.getOwner();

    }
    public Pawn(Player p) {
        this.owner = p;

    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public void addSN(int x) {
        this.sn=x;
    }


    public String getType (){
        if(owner.isPlayerOne())
            return "♙";
        else
            return "♟︎";

    }

}