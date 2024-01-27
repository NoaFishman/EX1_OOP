public class King extends ConcretePiece{


    public King(Player c,Position po){
        this.owner=c;
        this.pos=po;
        this.steps.add(po);
    }
    public King(Player c){
        this.owner=c;
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public void addSN(int x) {
        this.sn=x;
    }

    public String getType() {

        return "♔";

}
}