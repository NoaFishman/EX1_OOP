import java.util.ArrayList;
import java.util.Comparator;
import java.util.Stack;

public class GameLogic implements PlayableLogic {
    // creat 2 players they are final because no one need to change it
    private final ConcretePlayer p1 = new ConcretePlayer(true);
    private final ConcretePlayer p2 = new ConcretePlayer(false);
    // create 2D array that will presant the bord it will be ConccretePice typ so i could get the pice at each position
    // the x any of the bord will be the x and y of the position
    private ConcretePiece[][] board;
    // creat an array list of pices so at any point of the game i could reach any pice even if dead
    private ArrayList<ConcretePiece> pice ;
    // this array list will keep all the positions
    private ArrayList<Position> pCounter;
    // stack that save string with the info of each move
    private Stack<String> back;
    // boolean that if true is p2 turn and if false its p1 turn
    private boolean player2Turn;
    // boolean that tell as if some one wan and we need to end the game
    private boolean isFinished;
    // string that help as to creat the string that represent move and will insert to the stack
    private  String s;
    // constractor to new game difine that p2 is starting and restart the isfinished boolan
    public GameLogic() {
        player2Turn = true;
        isFinished = false;
        reset();

    }
    // move function in this function we will check if the pice can move we will move it and check if ate others pices
    @Override
    public boolean move(Position a, Position b) {
        // adding to the string the sorce x,y and destenaition x,y and sparading with chars that will help as to split later
        s="!"+a.getX()+","+a.getY()+"!"+"%"+b.getX()+","+b.getY()+"%^";
        //check that it is the player turn and that the game isn't over
        if (board[a.getX()][a.getY()].getOwner().isPlayerOne() != player2Turn && isFinished == false) {
            //check if the wanted destination is empty
            if (board[b.getX()][b.getY()] == null) {
                //check if want to walk strait
                if (a.getX() == b.getX() || a.getY() == b.getY()) {
                    // check if the way is clear
                    boolean free = true;
                    // if the x's are the same run on the y's and if a position is not empty change the flag free to false
                    if (a.getX() == b.getX()) {
                        for (int i = Math.min(a.getY(), b.getY()) + 1; i < Math.max(a.getY(), b.getY()); i++) {
                            if (board[a.getX()][i] != null)
                                free = false;
                        }
                        // if the y's are the same run on the x's and if a position is not empty change the flag free to false
                    } else if (a.getY() == b.getY()) {
                        for (int i = Math.min(a.getX(), b.getX()) + 1; i < Math.max(a.getX(), b.getX()); i++) {
                            if (board[i][a.getY()] != null)
                                free = false;
                        }
                    }
                    // if there was pices on the way will not g in to the if
                    if (free) {
                        // check that a pawn don't want to fo to a cornner
                        if (((b.getX() == 0 && b.getY() == 0) || (b.getX() == 0 && b.getY() == 10) || (b.getX() == 10 && b.getY() == 0) || (b.getX() == 10 && b.getY() == 10)) && board[a.getX()][a.getY()].getType() != "♔") {
                            // can't go there so the move function end
                            return false;
                        }
                        // if we got here so we made sure that this move is posible
                        else {
                            // taking the piece was in position a and move it to position b with the help of current and clear position a
                            ConcretePiece current;
                            current = board[a.getX()][a.getY()];
                            board[b.getX()][b.getY()] = current;
                            board[a.getX()][a.getY()] = null;
                            // adding to the piece array list that saving all his position the new position
                            board[b.getX()][b.getY()].addStep(b);
                            // look for the position in the positions array list and add to his pieces array lis the new piece (if this piece wasn't already ther)
                            for(int i=0; i<pCounter.size(); i++){
                                if(pCounter.get(i).getX()==b.getX()&&pCounter.get(i).getY()==b.getY())
                                {pCounter.get(i).addpiceToPos(board[b.getX()][b.getY()]);}
                            }
                            player2Turn = !player2Turn; // change the turn
                            //check if in this turn we killed a pawn add killing
                            isKilled(b);
                            // checking if the king is ded
                            // because i already change the turn so if it saying that it p1 turn so it was p2 turn and p2 is the only one that can kill the king
                            if(!player2Turn) {
                                if (kingKilled()) {
                                    // each player count how many times won
                                    p2.addWin();
                                }
                            }
                            // function that check each of the corners and return true if the king is in one of them
                            if (kingWin()) {
                                // count wins
                                p1.addWin();
                                // if king in cornner so p1 won and the game over
                                isFinished = true;
                            }
                            // adding the string that represent the move to the stack
                            back.add(s);
                            // function that print all the info in part 2 of the assainment
                            printWin();
                            // check if the game is over
                            isGameFinished();
                            // we moved the piced and update every thing we needed
                            return true;
                        }
                    }
                }
            }
        }
        // it is not the chosen piece turn or the game is over there wasn't move
        return false;
    }
    // we getting a position and return the piece on this position
    @Override
    public Piece getPieceAtPosition(Position position) {
        // retur the piece that his indexes in the board array is the position x&y
        return board[position.getX()][position.getY()];
    }
    // return player 1
    @Override
    public Player getFirstPlayer() {
        return p1;
    }
    // return player 2
    @Override
    public Player getSecondPlayer() {
        return p2;
    }
    // return the boolean isFinished that tell as if some one wan and if wan
    @Override
    public boolean isGameFinished() {
        return isFinished;
    }
    // return the boolean player2Turn true if it is player 2 turn and false if player's 1
    @Override
    public boolean isSecondPlayerTurn() {
        return player2Turn;
    }
    // the reset function difine and reset every thing we need to start a new game
    @Override
    public void reset() {
        // difine the booleans that p2 is starting and the game didn't over
        isFinished = false;
        player2Turn = true;
        // creat a new board of pieces size 11x11
        board = new ConcretePiece[11][11];
        // creat new array that will keep all the pieces in this game
        pice = new ArrayList<>();
        // creat a position array list and insert all the position and their amount of pieces stepd on
        pCounter = new ArrayList<Position>();
        // creat stack that will keep the info of each move
        back = new Stack<String>();
        // define all the pieces in the board on the position where they need to start the game by their player
        // adding the new piece to the pieces array
        // define for each piece it's serial number so now when we have it's sn and player we know which player is it
        // each row between 154-190 represent all the above definition for piece
        board[3][0] = new Pawn(p2);pice.add(board[3][0]);board[3][0].addSN(1);
        board[4][0] = new Pawn(p2);pice.add(board[4][0]);board[4][0].addSN(2);
        board[5][0] = new Pawn(p2);pice.add(board[5][0]);board[5][0].addSN(3);
        board[6][0] = new Pawn(p2);pice.add(board[6][0]);board[6][0].addSN(4);
        board[7][0] = new Pawn(p2);pice.add(board[7][0]);board[7][0].addSN(5);
        board[5][1] = new Pawn(p2);pice.add(board[5][1]);board[5][1].addSN(6);
        board[3][10] = new Pawn(p2);pice.add(board[3][10]);board[3][10].addSN(20);
        board[4][10] = new Pawn(p2);pice.add(board[4][10]);board[4][10].addSN(21);
        board[5][10] = new Pawn(p2);pice.add(board[5][10]);board[5][10].addSN(22);
        board[6][10] = new Pawn(p2);pice.add(board[6][10]);board[6][10].addSN(23);
        board[7][10] = new Pawn(p2);pice.add(board[7][10]);board[7][10].addSN(24);
        board[5][9] = new Pawn(p2);pice.add(board[5][9]);board[5][9].addSN(19);
        board[10][3] = new Pawn(p2);pice.add(board[10][3]);board[10][3].addSN(8);
        board[10][4] = new Pawn(p2);pice.add(board[10][4]);board[10][4].addSN(10);
        board[10][5] = new Pawn(p2);pice.add(board[10][5]);board[10][5].addSN(14);
        board[10][6] = new Pawn(p2);pice.add(board[10][6]);board[10][6].addSN(16);
        board[10][7] = new Pawn(p2);pice.add(board[10][7]);board[10][7].addSN(18);
        board[9][5] = new Pawn(p2);pice.add(board[9][5]);board[9][5].addSN(13);
        board[0][3] = new Pawn(p2);pice.add(board[0][3]);board[0][3].addSN(7);
        board[0][4] = new Pawn(p2);pice.add(board[0][4]);board[0][4].addSN(9);
        board[0][5] = new Pawn(p2);pice.add(board[0][5]);board[0][5].addSN(11);
        board[0][6] = new Pawn(p2);pice.add(board[0][6]);board[0][6].addSN(15);
        board[0][7] = new Pawn(p2);pice.add(board[0][7]);board[0][7].addSN(17);
        board[1][5] = new Pawn(p2);pice.add(board[1][5]);board[1][5].addSN(12);
        board[5][5] = new King(p1);pice.add(board[5][5]);board[5][5].addSN(7);
        board[3][5] = new Pawn(p1);pice.add(board[3][5]);board[3][5].addSN(5);
        board[4][5] = new Pawn(p1);pice.add(board[4][5]);board[4][5].addSN(6);
        board[6][5] = new Pawn(p1);pice.add(board[6][5]);board[6][5].addSN(8);
        board[7][5] = new Pawn(p1);pice.add(board[7][5]);board[7][5].addSN(9);
        board[4][4] = new Pawn(p1);pice.add(board[4][4]);board[4][4].addSN(2);
        board[4][6] = new Pawn(p1);pice.add(board[4][6]);board[4][6].addSN(10);
        board[6][6] = new Pawn(p1);pice.add(board[6][6]);board[6][6].addSN(12);
        board[6][4] = new Pawn(p1);pice.add(board[6][4]);board[6][4].addSN(4);
        board[5][4] = new Pawn(p1);pice.add(board[5][4]);board[5][4].addSN(3);
        board[5][3] = new Pawn(p1);pice.add(board[5][3]);board[5][3].addSN(1);
        board[5][6] = new Pawn(p1);pice.add(board[5][6]);board[5][6].addSN(11);
        board[5][7] = new Pawn(p1);pice.add(board[5][7]);board[5][7].addSN(13);
       // create all the positions ad add them to array list
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                Position curr=new Position(i,j);
                // add to each position's arry of pieces the first piece standing on
                if(board[i][j]!=null) {
                    curr.addpiceToPos(board[i][j]);
                }
                pCounter.add(curr);
            }
        }
        // run on the board and if find piece so we adding to this piece array that keep it's position the start position
        for(int i=0;i<11;i++){
            for (int j=0;j<11;j++){
                if(board[i][j]!=null) {
                    board[i][j].addStep(new Position(i, j));
                }
            }
        }
    }
    // this function take the last string in the stack that represent the last move and split it to the last move info and undo everything that happened
    @Override
    public void undoLastMove() {
        // each string in the stack build like that: !start x,start y!%current x,current y%^~1kiild x,1killd y,kiiled player, killed sn~(all the killed pices)~^@how many pices was killed this step@
        // if the stack is empty so we are at starting position
        if(!back.empty()) {
            // change back the turn
            player2Turn = !player2Turn;
            String last;
            last = back.pop();
            String current;
            boolean flag = true;
            String[] a;
            String[] b;
            // finding the x,y that the piece came from
            a = last.split("!");
            current = a[1];
            b = current.split(",");
            int x1 = Integer.parseInt(b[0]);
            int y1 = Integer.parseInt(b[1]);
            //finding the x,y that the piece moved to
            a = last.split("%");
            current = a[1];
            b = current.split(",");
            int x2 = Integer.parseInt(b[0]);
            int y2 = Integer.parseInt(b[1]);
            board[x2][y2].steps.remove(board[x2][y2].steps.size() - 1);// remove the last position this piece stand on
            for(int i=0 ; i<pCounter.size(); i++){
                if(pCounter.get(i).getX()==x2&& pCounter.get(i).getY()==y2){
                    pCounter.get(i).setpiceToPos(board[x2][y2]);
                }
            }
            //moving the piece back
            ConcretePiece curr;
            curr = board[x2][y2];
            board[x1][y1] = curr;
            board[x2][y2] = null;
            // find how many pieces was killed this turn
            a = last.split("@");
            int killed = Integer.parseInt(a[1]);
            // remove from the counter of how many piece this piece killed the amount he killed in this move
            board[x1][y1].setKill(killed);
            // if some one killed this turn so bring it back to life
            if (killed > 0) {
                // all the killed pieces is between "^"
                a = last.split("\\^");
                current = a[1];
                // each killed pice is between "~"
                String[] B = current.split("~");
                String[] c;
                String viktim;
                // run on all the killed pieces
                for (int i = 1; i < killed + 1; i++) {
                    viktim = B[i];
                    c = viktim.split(",");
                    // fined the position where was killed
                    int xc = Integer.parseInt(c[0]);
                    int yc = Integer.parseInt(c[1]);
                    // find the owner ond sn of this piece so we can find it in the pieces array
                    boolean player1 = Boolean.parseBoolean(c[2]);
                    int snc = Integer.parseInt(c[3]);
                    boolean flag1 = true;
                    // look in the array the killed piece and if find stop the loop and return to the board
                    for (int j = 0; j < pice.size() && flag1; j++) {
                        if (pice.get(j).sn == snc && pice.get(j).owner.isPlayerOne() == player1) {
                            board[xc][yc] = pice.get(j);
                            flag1 = false;
                        }
                    }
                }
            }
        }
    }
    // return the board size
    @Override
    public int getBoardSize() {
        return 11;
    }
    // this function get the position the piece moved to and check around if the piece killed another piece
    public void isKilled(Position b) {
        int ckilled=0; // counter of how many pieces killed this turn
        int x = b.getX();
        int y = b.getY();
        // checking that this is not a king because king can't kill
        if (board[x][y].getType() != "♔") {
            // checking if the left piece was killed & not king
            if (x > 0 && board[x - 1][y] != null && board[x - 1][y].getType() != "♔") {
                // check if killed with help of left wall
                if (board[x][y].getOwner() != board[x - 1][y].getOwner() && x - 1 == 0) {
                    // add the info to the string
                    s=s+"~"+(x-1)+","+y+","+board[x - 1][y].owner.isPlayerOne()+","+board[x - 1][y].sn;
                    // killing and counting it
                    board[x - 1][y] = null;
                    ckilled++;
                }
                // check if killed with help of another his player team from the left
                else if (x - 1 > 0 && board[x - 2][y] != null && board[x][y].getOwner() != board[x - 1][y].getOwner() && board[x - 2][y].getOwner() != board[x - 1][y].getOwner() && board[x - 2][y].getType() != "♔") {
                    // add the info to the string
                    s=s+"~"+(x-1)+","+y+","+board[x - 1][y].owner.isPlayerOne()+","+board[x - 1][y].sn;
                    // killing and counting it
                    board[x - 1][y] = null;
                    ckilled++;
                }
            }
            // checking if the right piece was killed & not king
            if (x < 10 && board[x + 1][y] != null && board[x + 1][y].getType() != "♔") {
                // check if killed the right piece with the help of a wall
                if (board[x][y].getOwner() != board[x + 1][y].getOwner() && x + 1 == 10) {
                    // add the info to the string
                    s=s+"~"+(x+1)+","+y+","+board[x + 1][y].owner.isPlayerOne()+","+board[x + 1][y].sn;
                    // killing and counting it
                    board[x + 1][y] = null;
                    ckilled++;
                }
                // check if killed with help of another his player team from the right
                else if (x + 1 < 10 && board[x + 2][y] != null && board[x][y].getOwner() != board[x + 1][y].getOwner() && board[x + 2][y].getOwner() != board[x + 1][y].getOwner() && board[x + 2][y].getType() != "♔") {
                    // add the info to the string
                    s=s+"~"+(x+1)+","+y+","+board[x + 1][y].owner.isPlayerOne()+","+board[x + 1][y].sn;
                    // killing and counting it
                    board[x + 1][y] = null;
                    ckilled++;
                }
            }
            // checking if the above piece was killed & not king
            if (y < 10 && board[x][y + 1] != null && board[x][y + 1].getType() != "♔") {
                // check if killed the above piece with the help of a wall
                if (board[x][y].getOwner() != board[x][y + 1].getOwner() && y + 1 == 10) {
                    // add the info to the string
                    s=s+"~"+x+","+(y+1)+","+board[x][y+1].owner.isPlayerOne()+","+board[x][y+1].sn;
                    // killing and counting it
                    board[x][y + 1] = null;
                    ckilled++;
                }
                // check if killed with help of another his player team from the above
                else if (y + 1 < 10 && board[x][y + 2] != null && board[x][y].getOwner() != board[x][y + 1].getOwner() && board[x][y + 2].getOwner() != board[x][y + 1].getOwner() && board[x][y + 2].getType() != "♔") {
                    // add the info to the string
                    s=s+"~"+x+","+(y+1)+","+board[x][y+1].owner.isPlayerOne()+","+board[x][y+1].sn;
                    // killing and counting it
                    board[x][y + 1] = null;
                    ckilled++;
                }
            }
            // checking if the bottom piece was killed & not king
            if (y > 0 && board[x][y - 1] != null && board[x][y - 1].getType() != "♔") {
                // check if killed the bottom piece with the help of a wall
                if (board[x][y].getOwner() != board[x][y - 1].getOwner() && y - 1 == 0) {
                    // add the info to the string
                    s=s+"~"+x+","+(y-1)+","+board[x][y-1].owner.isPlayerOne()+","+board[x][y-1].sn;
                    // killing and counting it
                    board[x][y - 1] = null;
                    ckilled++;
                }
                // check if killed with help of another his player team from the bottom
                else if (y - 1 > 0 && board[x][y - 2] != null && board[x][y].getOwner() != board[x][y - 1].getOwner() && board[x][y - 2].getOwner() != board[x][y - 1].getOwner() && board[x][y - 2].getType() != "♔") {
                    // add the info to the string
                    s=s+"~"+x+","+(y-1)+","+board[x][y-1].owner.isPlayerOne()+","+board[x][y-1].sn;
                    // killing and counting it
                    board[x][y - 1] = null;
                    ckilled++;
                }
            }
        }
        // add the killed  if the pawn eat with help of acornner
        ckilled=ckilled+killedCornner(b);
        // add the amount of kiiled to the move string
        s=s+"~^@"+ckilled+"@";
        // use a while loop and the amount of killed to add the kills to the piece that killed
        if (ckilled>0) {
            while(ckilled>0){
                // add to the pawn the amount killed he did
                board[x][y].addKill();
                ckilled--;
            }

        }

    }
    // check if the pawn eat another pawn with the cornner and return the numof killed
    public int killedCornner(Position p){
        int killed=0;
        // king can't kill
        if(board[p.getX()][p.getY()].getType()=="♔")
            return 0;
        // because their is only 8 options to eat with corrner I will check each one of them and if happen add to the move string
        // top left with the to position from the right
        if(p.getX()==2&&p.getY()==0 &&board[1][0]!=null &&board[1][0].getType()!="♔" &&board[1][0].owner!=board[p.getX()][p.getY()].getOwner()){
            s=s+"~1,0,"+board[1][0].owner.isPlayerOne()+","+board[1][0].sn;
            board[1][0] = null;
            killed++;
        }
        // top left with the two position under
        if(p.getX()==0 && p.getY()==2 && board[0][1]!=null &&board[0][1].getType()!="♔"&& board[0][1].owner!=board[p.getX()][p.getY()].getOwner()){
            s=s+"~0,1,"+board[0][1].owner.isPlayerOne()+","+board[0][1].sn;
            board[0][1]=null;
            killed++;
        }
        // top right with the two position to the left
        if(p.getX()==8 && p.getY()==0 && board[9][0]!=null &&board[9][0].getType()!="♔"&& board[9][0].owner!=board[p.getX()][p.getY()].getOwner()){
            s=s+"~9,0,"+board[9][0].owner.isPlayerOne()+","+board[9][0].sn;
            board[9][0]=null;
            killed++;
        }
        // top right with the two position under
        if(p.getX()==10 && p.getY()==2 &&board[10][1]!=null&&board[10][1].getType()!="♔"&& board[10][1].owner!=board[p.getX()][p.getY()].getOwner()){
            s=s+"~10,1,"+board[10][1].owner.isPlayerOne()+","+board[10][1].sn;
            board[10][1]=null;
            killed++;
        }
        // bottom left with the two position right
        if(p.getX()==0 && p.getY()==8 &&board[0][9]!=null&&board[0][9].getType()!="♔"&& board[0][9].owner!=board[p.getX()][p.getY()].getOwner()){
            s=s+"~0,9,"+board[0][9].owner.isPlayerOne()+","+board[0][9].sn;
            board[0][9]=null;
            killed++;
        }
        // bottom left with the two position above
        if(p.getX()==2 && p.getY()==10 &&board[1][10]!=null&&board[1][10].getType()!="♔"&& board[1][10].owner!=board[p.getX()][p.getY()].getOwner()){
            s=s+"~1,10,"+board[1][10].owner.isPlayerOne()+","+board[1][10].sn;
            board[1][10]=null;
            killed++;
        }
        // bottom right with the two position left
        if(p.getX()==8 && p.getY()==10 &&board[9][10]!=null&&board[9][10].getType()!="♔"&& board[9][10].owner!=board[p.getX()][p.getY()].getOwner()){
            s=s+"~9,10,"+board[9][10].owner.isPlayerOne()+","+board[9][10].sn;
            board[9][10]=null;
            killed++;
        }
        // bottom right with the two position above
        if(p.getX()==10 && p.getY()==8 &&board[10][9]!=null&&board[10][9].getType()!="♔"&& board[10][9].owner!=board[p.getX()][p.getY()].getOwner()){
            s=s+"~10,9,"+board[10][9].owner.isPlayerOne()+","+board[10][9].sn;
            board[10][9]=null;
            killed++;
        }
        return killed;
    }
    // check if the king is seround
    public boolean kingKilled() {
        // look for the king on the board
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (board[i][j] != null && board[i][j].getType() == "♔") {
                    int help = 0;
                    // check that the king is srounde and count by how many pawns/ walls (need 4 pawns)
                    if (i == 0 || (board[i - 1][j] != null && board[i - 1][j].getOwner() == p2)) {
                        help++;
                    }
                    if (i == 10 || (board[i + 1][j] != null && board[i + 1][j].getOwner() == p2)) {
                        help++;
                    }
                    if (j == 0 || (board[i][j - 1] != null && board[i][j - 1].getOwner() == p2)) {
                        help++;
                    }
                    if (j == 10 || (board[i][j + 1] != null && board[i][j + 1].getOwner() == p2)) {
                        help++;
                    }
                    if (help == 4) {
                        // the king is dead so the game over
                        isFinished = true;
                        return true;
                    } else {
                        //the king isn't dead not seround by 4
                        return false;
                    }
                }
            }
        }
        // king is not dead
        return false;
    }
    // this function check if player 1 wan
    public boolean kingWin() {
        //check each corrner if the king is there
        if (board[0][0] != null && board[0][0].getType() == "♔") {
            return true;
        }
        if (board[0][10] != null && board[0][10].getType() == "♔") {
            return true;
        }
        if (board[10][0] != null && board[10][0].getType() == "♔") {
            return true;
        }
        if (board[10][10] != null && board[10][10].getType() == "♔") {
            return true;
        }
        // check if there is any red pawns on the board if there isn't so the blue win
        for(int i=0; i<11;i++){
            for(int j=0; j<11; j++){
                // if p2 ate all the p1 pawn so he ate also the king so he already wan
                if(board[i][j]!=null && board[i][j].owner==p2){
                    // there is p2 pieces
                    return false;
                }
            }
        }
        // player 1 wan
        return true;
    }
    // comperator that comper by steps by the size of the positions array of each piece
    Comparator<ConcretePiece> CompSteps = new Comparator<ConcretePiece>() {
        @Override
        public int compare(ConcretePiece o1, ConcretePiece o2) {
            // who have more position in his array
            if (o1.steps.size() > o2.steps.size())
                return 1;
            if (o1.steps.size() < o2.steps.size())
                return -1;
            // if same size so the one with smaller sn will be first
            if (o1.steps.size() == o2.steps.size()) {
                if (o1.sn > o2.sn)
                    return 1;
                if ((o1.sn <= o2.sn))
                    return -1;
            }
            return 0;
        }
    };
    // comperator that comper by the distance between all the position piece step on
    Comparator<ConcretePiece> CompDis = new Comparator<ConcretePiece>() {
        @Override
        public int compare(ConcretePiece o1, ConcretePiece o2) {
            // which piece did bigger distance
            if (o1.disAllGame() > o2.disAllGame())
                return -1;
            if (o1.disAllGame() < o2.disAllGame())
                return 1;
            // if same distance so who have bigger sn and if also the sme so by which team wan
            if (o1.disAllGame() == o2.disAllGame()) {
                if (o1.sn > o2.sn)
                    return 1;
                if (o1.sn < o2.sn)
                    return -1;
                if (o1.sn == o2.sn) {
                    if (player2Turn) {
                        if (o1.owner == p1)
                            return -1;
                        return 1;
                    } else {
                        if (o1.owner == p1)
                            return 1;
                        return -1;
                    }
                }
            }
            return 0;
        }
    };
    // comperator that comper by how many pieces this pice killed
    Comparator<ConcretePiece> CompKill = new Comparator<ConcretePiece>() {
        @Override
        public int compare(ConcretePiece o1, ConcretePiece o2) {
            // who kill more
            if (o1.kills > o2.kills)
                return -1;
            if (o1.kills < o2.kills)
                return 1;
            // if kill the same amount so by sn and if hve the same sn so the piece that his team wan will be first
            if (o1.kills == o2.kills) {
                if (o1.sn > o2.sn)
                    return 1;
                if (o1.sn < o2.sn)
                    return -1;
                if (o1.sn == o2.sn) {
                    if (player2Turn) {
                        if (o1.owner == p1)
                            return -1;
                        return 1;
                    } else {
                        if (o1.owner == p1)
                            return 1;
                        return -1;
                    }
                }
            }
            return 0;
        }
    };
    // comperator that comper by how many pieces was stand on this position
    Comparator<Position> CompPiceAtPos= new Comparator<Position>() {
        @Override
        public int compare(Position p1, Position p2) {
            // who had more pieces steped on
            if (p1.getCount() > p2.getCount())
                return -1;
            if (p1.getCount() < p2.getCount())
                return 1;
            // if the same amount so the one with smaller x will be first
            if (p1.getCount() == p2.getCount()) {
                if (p1.getX() < p2.getX())
                    return -1;
                if (p1.getX() > p2.getX())
                    return 1;
                // if the same x so the one withe smaller y will be first
                if (p1.getX() == p2.getX()) {
                    if (p1.getY() < p2.getY()) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            }
            return 0;
        }
    };
    // this function print all the info we need from the second part of the assainment
    public void printWin() {
        // if the game didn't over so go out from the function
        if(!isFinished){
            return;
        }
        // 1
        // sort the piece array by thier amount of steps this game
        pice.sort(CompSteps);
        // if p2 turn so p1 wan
        if (player2Turn) {
            // print just p1 pieces and just if have more then one position in the array
            for (int i = 0; i < pice.size(); i++) {
                if (pice.get(i).owner == p1 && pice.get(i).steps.size() > 1) {
                    // the kung have diffrent letter so we look for him
                    if (pice.get(i).sn == 7) {
                        System.out.println("K" + pice.get(i).sn + ": " + pice.get(i).toStringSteps());
                    } else {
                        System.out.println("D" + pice.get(i).sn + ": " + pice.get(i).toStringSteps());

                    }
                }
            }
            // print just p2 pieces and just if have more then one position in the array
            for (int i = 0; i < pice.size(); i++) {
                if (pice.get(i).owner == p2 && pice.get(i).steps.size() > 1)
                    System.out.println("A" + pice.get(i).sn + ": " + pice.get(i).toStringSteps());
            }
        }
        // if p1 turn so p2 wan
        else {
            // print just p2 pieces and just if have more then one position in the array
            for (int i = 0; i < pice.size(); i++) {
                if (pice.get(i).owner == p2 && pice.get(i).steps.size() > 1)
                    System.out.println("A" + pice.get(i).sn + ": " + pice.get(i).toStringSteps());
            }
            // print just p1 pieces and just if have more then one position in the array
            for (int i = 0; i < pice.size(); i++) {
                if (pice.get(i).owner == p1 && pice.get(i).steps.size() > 1)
                    // the king have diffent letter so looking for him
                    if (pice.get(i).sn == 7) {
                        System.out.println("K" + pice.get(i).sn + ": " + pice.get(i).toStringSteps());
                    } else {
                        System.out.println("D" + pice.get(i).sn + ": " + pice.get(i).toStringSteps());
                    }
            }
        }
        // print 75 stars
        for (int i = 0; i < 74; i++)
            System.out.print("*");
        System.out.println("*");
        // 2
        // sort the pieces array by the amount of killes each piece did
        pice.sort(CompKill);
        for (int i = 0; i < pice.size(); i++) {
            // check the typ of the piece to know what to print and if killed at least one piece
            if (pice.get(i).getType() == "♟︎" && pice.get(i).kills >0)
                System.out.println("A" + pice.get(i).sn + ": " + pice.get(i).kills + " kills");
            if (pice.get(i).getType() == "♙" && pice.get(i).kills >0)
                System.out.println("D" + pice.get(i).sn + ": " + pice.get(i).kills + " kills");
            if (pice.get(i).getType() == "♔" && pice.get(i).kills >0)
                System.out.println("K" + pice.get(i).sn + ": " + pice.get(i).kills + " kills");
        }
        //print 75 stars
        for (int i = 0; i < 74; i++)
            System.out.print("*");
        System.out.println("*");
        // 3
        // sort the piece array by the distance each piece did
        pice.sort(CompDis);
        for (int i = 0; i < pice.size(); i++) {
            // check the typ of the piece to know what to print and if the distance is bigger then 0
            if (pice.get(i).getType() == "♟︎" && pice.get(i).disAllGame() > 0)
                System.out.println("A" + pice.get(i).sn + ": " + pice.get(i).disAllGame() + " squares");
            if (pice.get(i).getType() == "♙" && pice.get(i).disAllGame() > 0)
                System.out.println("D" + pice.get(i).sn + ": " + pice.get(i).disAllGame() + " squares");
            if (pice.get(i).getType() == "♔" && pice.get(i).disAllGame() > 0)
                System.out.println("K" + pice.get(i).sn + ": " + pice.get(i).disAllGame() + " squares");
        }

        // ************* how many piece was here ****************
        //print 75 stars
        for (int i = 0; i < 74; i++)
            System.out.print("*");
        System.out.println("*");
        // 4
        // sort the position array list by the amount of pieces steped on
        pCounter.sort(CompPiceAtPos);
        for(int i=0; i<pCounter.size(); i++){
            // run on all the position and print only if mor then 2 pieces steped on
            if(pCounter.get(i).getCount()>=2){
                int x=pCounter.get(i).getX();
                int y=pCounter.get(i).getY();
                // get the x & y and print
                System.out.println("("+x+", "+y+")"+ pCounter.get(i).getCount()+" pieces");
            }
        }
        // print 75 stars
        for (int i = 0; i < 74; i++)
            System.out.print("*");
        System.out.println("*");
    }
}