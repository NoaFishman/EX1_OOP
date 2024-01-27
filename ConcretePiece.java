import java.util.ArrayList;
import java.util.Comparator;

public abstract class ConcretePiece implements Piece {

    protected Player owner;
    protected ArrayList<Position> steps = new ArrayList<>();
    protected Position pos;
    protected int sn;
    protected int kills = 0;
    // will count the number of piece that this piece killed
    protected ConcretePiece() {

    }
    public void addStep(Position p) {
        steps.add(p);
    }
    public abstract String toString ();
    public abstract void addSN (int x);
    public String toStringSteps() {
        String s = "[";
        s = s + steps.get(0).toString();
        for (int i = 1; i < steps.size(); i++) {
            s = s + ", " + steps.get(i).toString();
        }
        s = s + "]";
        return s;
    }
    public void addKill(){
        kills++;
    }
    public void setKill(int alive){
        this.kills=kills-alive;
    }
//    public void setPos(Position p) {
//        this.pos = p;
//    }
//
//    public void dead() {
//        this.pos = null;
//    }


    @Override
    public Player getOwner() {
        return this.owner;
    }

    @Override
    public String getType() {
        return this.getType();
    }
    public int disP2P (Position a, Position b){
        int x= Math.abs(a.getX()-b.getX());
        int y= Math.abs(a.getY()-b.getY());
        return x+y;
    }
    public int disAllGame(){
        int ans=1;
        for (int i=0; i<steps.size()-1; i++){
            ans= ans+disP2P(steps.get(i),steps.get(i+1));
        }
        return ans-1;
    }
}


