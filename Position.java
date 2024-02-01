import java.util.ArrayList;

public class Position {
    // the x of this position
    private int x;
    // the y of this position
    private int y;
    //this array will save all the pieces that stepted at this pos
    private ArrayList<ConcretePiece> piceatPos = new ArrayList<ConcretePiece>();
    // position comperator that get x & y define them and define that no one steped on
    public Position (int x, int y){
        this.x=x;
        this.y=y;
    }
    // check that this piece didn't step on this pos befor and add it to the array
    public void addpiceToPos(ConcretePiece p){
        if(piceatPos==null){
            piceatPos.add(p);
        }
        else if (!piceatPos.contains(p)){
            piceatPos.add(p);
        }
    }
    public void setpiceToPos(ConcretePiece p){
        if (piceatPos.get(piceatPos.size()-1).equals(p)){
            piceatPos.remove(piceatPos.size()-1);

        }
    }
    // return the amount of pieces steped on this position
    public int getCount (){
        return piceatPos.size();
    }
    // return the x value of this position
    public int getX(){
        return this.x;
    }
    // return the y value of this position
    public int getY(){
        return this.y;
    }
    // return a string that represent the position
    public String toString (){
        return ("("+ this.getX()+", "+this.getY()+")");
    }
}