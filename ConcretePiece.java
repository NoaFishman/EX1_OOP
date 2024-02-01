import java.util.ArrayList;
import java.util.Comparator;

public abstract class ConcretePiece implements Piece {
    // the piece owner
    protected Player owner;
    // an array that contain all the positions that this piece steped on
    protected ArrayList<Position> steps = new ArrayList<>();
    // the siriel number of this piece
    protected int sn;
    // counter that tell us how many pieces this pawn killed
    protected int kills = 0;
    // constractor empty because this is abstract class
    protected ConcretePiece() {}
    // add to the steps array list the new position
    public void addStep(Position p) {
        steps.add(p);
    }
    // add to the piece his sn
    public abstract void addSN (int x);
    // creat a string of all the position this piece steped on
    public String toStringSteps() {
        String s = "[";
        s = s + steps.get(0).toString();
        for (int i = 1; i < steps.size(); i++) {
            s = s + ", " + steps.get(i).toString();
        }
        s = s + "]";
        return s;
    }
    // add to the kill counter one kill
    public void addKill(){
        kills++;
    }
    // remove from the kill counter the amount pieces that came back to life
    public void setKill(int alive){
        this.kills=kills-alive;
    }
    @Override
    // return this piece owner
    public Player getOwner() {
        return this.owner;
    }
    @Override
    // return the type of this piece with pawn and king get typ functions
    public String getType() {
        return this.getType();
    }
    // clculate the distance between two position
    public int disP2P (Position a, Position b){
        int x= Math.abs(a.getX()-b.getX());
        int y= Math.abs(a.getY()-b.getY());
        return x+y;
    }
    // calculate the distance piece did all game by sum all the positions p2p by order
    public int disAllGame(){
        int ans=1;
        for (int i=0; i<steps.size()-1; i++){
            ans= ans+disP2P(steps.get(i),steps.get(i+1));
        }
        return ans-1;
    }
}


