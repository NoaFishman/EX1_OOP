public class Position {
    private int x;
    private int y;
    private int count;
    private Piece pieceAtPos;

    public Position (int x, int y){
        this.x=x;
        this.y=y;
        this.count=0;
    }
    public Position (int x, int y, int c){
        this.x=x;
        this.y=y;
        this.count=c;
    }
    public int getCount (){
        return count;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public String toString (){
        return ("("+ this.getX()+", "+this.getY()+")");
    }


    public Piece getPieceAtPos(){
        return this.pieceAtPos;
    }

    public void setPieceAtPos(Piece piece){
        this.pieceAtPos=piece;
    }
    public void setPosition(int x, int y){
        this.x=x;
        this.y=y;
    }


    public void clearPosition(){
        this.pieceAtPos=null;
    }


    public boolean isEmpty(){


        return false;
    }

}