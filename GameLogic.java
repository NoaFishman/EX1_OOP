import java.util.ArrayList;
import java.util.Comparator;
import java.util.Stack;

public class GameLogic implements PlayableLogic {

    private final ConcretePlayer p1 = new ConcretePlayer(true);
    private final ConcretePlayer p2 = new ConcretePlayer(false);
    private ConcretePiece[][] board;
    private ArrayList<ConcretePiece> pice ;
    // stack that save string with the info of eacb move
    private Stack<String> back;
    private boolean player2Turn;
    private boolean isFinished;
    private int[][] count;
    private  String s;

    public GameLogic() {
        player2Turn = true;
        isFinished = false;
        reset();

    }

    @Override
    public boolean move(Position a, Position b) {
        s="!"+a.getX()+","+a.getY()+"!"+"%"+b.getX()+","+b.getY()+"%^";
        //check that it is the player turn
        if (board[a.getX()][a.getY()].getOwner().isPlayerOne() != player2Turn && isFinished == false) {
            //check if the wanted destination is clear
            if (board[b.getX()][b.getY()] == null) {
                //check if want to walk strait
                if (a.getX() == b.getX() || a.getY() == b.getY()) {
                    //check if the way is free
                    boolean free = true;
                    if (a.getX() == b.getX()) {
                        for (int i = Math.min(a.getY(), b.getY()) + 1; i < Math.max(a.getY(), b.getY()); i++) {
                            if (board[a.getX()][i] != null)
                                free = false;
                        }
                    } else if (a.getY() == b.getY()) {
                        for (int i = Math.min(a.getX(), b.getX()) + 1; i < Math.max(a.getX(), b.getX()); i++) {
                            if (board[i][a.getY()] != null)
                                free = false;
                        }
                    }
                    if (free) {
                        // if pawn so cant go to corner
                        if (((b.getX() == 0 && b.getY() == 0) || (b.getX() == 0 && b.getY() == 10) || (b.getX() == 10 && b.getY() == 0) || (b.getX() == 10 && b.getY() == 10)) && board[a.getX()][a.getY()].getType() != "♔") {
                            return false;
                        } else {
                            ConcretePiece current;
                            current = board[a.getX()][a.getY()];
                            board[b.getX()][b.getY()] = current;
                            board[a.getX()][a.getY()] = null;
                            board[b.getX()][b.getY()].addStep(b);
                            count[b.getX()][b.getY()]++;
                            player2Turn = !player2Turn; // change the turn
                            //האם זה עוזר לי שזה בוליאני????
                            //check if in this turn we killed a pawn
                            if (isKilled(b)) {

                            }
                            // checking if the king is ded
                            // לבדוק אולי אפשר להפעיל רק כשתות שחקן 2 ואז זה מצמצם בחצי את הריצה
                            //האם זה עוזר לי שזה בוליאני????
                            if (kingKilled()) {
                                p2.addWin();
                            }
                            // help function that check each of the corners and return true if the king is in one of them
                            if (kingWin()) {
                                // count wins
                                p1.addWin();
                                isFinished = true;
                               // player2Turn = true;
                            }
                            back.add(s);
                            isGameFinished();


                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Piece getPieceAtPosition(Position position) {
        return board[position.getX()][position.getY()];
    }

    @Override
    public Player getFirstPlayer() {
        return p1;
    }

    @Override
    public Player getSecondPlayer() {
        return p2;
    }

    @Override
    public boolean isGameFinished() {
        if (isFinished) {
            printWin(p1);
            player2Turn=true;
        }
        return isFinished;
    }

    @Override
    public boolean isSecondPlayerTurn() {
        return player2Turn;
    }

    @Override
    public void reset() {
        isFinished = false;
        player2Turn = true;
        board = new ConcretePiece[11][11];
        count = new int[11][11];
        pice = new ArrayList<>();
        back = new Stack<String>();
        //מיקום החיילים
        board[3][0] = new Pawn(p2);count[3][0]++;pice.add(board[3][0]);board[3][0].addSN(1);
        board[4][0] = new Pawn(p2);count[4][0]++;pice.add(board[4][0]);board[4][0].addSN(2);
        board[5][0] = new Pawn(p2);count[5][0]++;pice.add(board[5][0]);board[5][0].addSN(3);
        board[6][0] = new Pawn(p2);count[6][0]++;pice.add(board[6][0]);board[6][0].addSN(4);
        board[7][0] = new Pawn(p2);count[7][0]++;pice.add(board[7][0]);board[7][0].addSN(5);
        board[5][1] = new Pawn(p2);count[5][1]++;pice.add(board[5][1]);board[5][1].addSN(6);
        board[3][10] = new Pawn(p2);count[3][10]++;pice.add(board[3][10]);board[3][10].addSN(20);
        board[4][10] = new Pawn(p2);count[4][10]++;pice.add(board[4][10]);board[4][10].addSN(21);
        board[5][10] = new Pawn(p2);count[5][10]++;pice.add(board[5][10]);board[5][10].addSN(22);
        board[6][10] = new Pawn(p2);count[6][10]++;pice.add(board[6][10]);board[6][10].addSN(23);
        board[7][10] = new Pawn(p2);count[7][10]++;pice.add(board[7][10]);board[7][10].addSN(24);
        board[5][9] = new Pawn(p2);count[5][9]++;pice.add(board[5][9]);board[5][9].addSN(19);
        board[10][3] = new Pawn(p2);count[10][3]++;pice.add(board[10][3]);board[10][3].addSN(8);
        board[10][4] = new Pawn(p2);count[10][4]++;pice.add(board[10][4]);board[10][4].addSN(10);
        board[10][5] = new Pawn(p2);count[10][5]++;pice.add(board[10][5]);board[10][5].addSN(14);
        board[10][6] = new Pawn(p2);count[10][6]++;pice.add(board[10][6]);board[10][6].addSN(16);
        board[10][7] = new Pawn(p2);count[10][7]++;pice.add(board[10][7]);board[10][7].addSN(18);
        board[9][5] = new Pawn(p2);count[9][5]++;pice.add(board[9][5]);board[9][5].addSN(13);
        board[0][3] = new Pawn(p2);count[0][3]++;pice.add(board[0][3]);board[0][3].addSN(7);
        board[0][4] = new Pawn(p2);count[0][4]++;pice.add(board[0][4]);board[0][4].addSN(9);
        board[0][5] = new Pawn(p2);count[0][5]++;pice.add(board[0][5]);board[0][5].addSN(11);
        board[0][6] = new Pawn(p2);count[0][6]++;pice.add(board[0][6]);board[0][6].addSN(15);
        board[0][7] = new Pawn(p2);count[0][7]++;pice.add(board[0][7]);board[0][7].addSN(17);
        board[1][5] = new Pawn(p2);count[1][5]++;pice.add(board[1][5]);board[1][5].addSN(12);
        board[5][5] = new King(p1);count[5][5]++;pice.add(board[5][5]);board[5][5].addSN(7);
        board[3][5] = new Pawn(p1);count[3][5]++;pice.add(board[3][5]);board[3][5].addSN(5);
        board[4][5] = new Pawn(p1);count[4][5]++;pice.add(board[4][5]);board[4][5].addSN(6);
        board[6][5] = new Pawn(p1);count[6][5]++;pice.add(board[6][5]);board[6][5].addSN(8);
        board[7][5] = new Pawn(p1);count[7][5]++;pice.add(board[7][5]);board[7][5].addSN(9);
        board[4][4] = new Pawn(p1);count[4][4]++;pice.add(board[4][4]);board[4][4].addSN(2);
        board[4][6] = new Pawn(p1);count[4][6]++;pice.add(board[4][6]);board[4][6].addSN(10);
        board[6][6] = new Pawn(p1);count[6][6]++;pice.add(board[6][6]);board[6][6].addSN(12);
        board[6][4] = new Pawn(p1);count[6][4]++;pice.add(board[6][4]);board[6][4].addSN(4);
        board[5][4] = new Pawn(p1);count[5][4]++;pice.add(board[5][4]);board[5][4].addSN(3);
        board[5][3] = new Pawn(p1);count[5][3]++;pice.add(board[5][3]);board[5][3].addSN(1);
        board[5][6] = new Pawn(p1);count[5][6]++;pice.add(board[5][6]);board[5][6].addSN(11);
        board[5][7] = new Pawn(p1);count[5][7]++;pice.add(board[5][7]);board[5][7].addSN(13);

        for(int i=0;i<11;i++){
            for (int j=0;j<11;j++){
                if(board[i][j]!=null) {
                    board[i][j].addStep(new Position(i, j));
                }
            }
        }
    }

    @Override
    public void undoLastMove() {
        if(!back.empty()) {
            player2Turn = !player2Turn;
            String last;
            last = back.pop();
            String current;
            boolean flag = true;
            //int i=1;
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
            count[x2][y2]--; // the piece is now didn't stand there
            //moving the piece back
            ConcretePiece curr;
            curr = board[x2][y2];
            board[x1][y1] = curr;
            board[x2][y2] = null;
            a = last.split("@");
            int killed = Integer.parseInt(a[1]);
            // remove from the counter of how many piece this piece killed the amount he killed in this move
            board[x1][y1].setKill(killed);
            if (killed > 0) {
                a = last.split("\\^");
                current = a[1];
                String[] B = current.split("~");
                String[] c;
                String viktim;
                for (int i = 1; i < killed + 1; i++) {
                    viktim = B[i];
                    c = viktim.split(",");
                    int xc = Integer.parseInt(c[0]);
                    int yc = Integer.parseInt(c[1]);
                    boolean player1 = Boolean.parseBoolean(c[2]);
                    int snc = Integer.parseInt(c[3]);
                    boolean flag1 = true;
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

    @Override
    public int getBoardSize() {
        return 11;
    }

    public boolean isKilled(Position b) {
        boolean killed = false;
        int ckilled=0;
        int x = b.getX();
        int y = b.getY();
        // checking if the left piece was killed &not king
        if (board[x][y].getType() != "♔") {
            if (x > 0 && board[x - 1][y] != null && board[x - 1][y].getType() != "♔") {
                if (board[x][y].getOwner() != board[x - 1][y].getOwner() && x - 1 == 0) {
                    s=s+"~"+(x-1)+","+y+","+board[x - 1][y].owner.isPlayerOne()+","+board[x - 1][y].sn;
                    board[x - 1][y] = null;
                    ckilled++;
                } else if (x - 1 > 0 && board[x - 2][y] != null && board[x][y].getOwner() != board[x - 1][y].getOwner() && board[x - 2][y].getOwner() != board[x - 1][y].getOwner() && board[x - 2][y].getType() != "♔") {
                    s=s+"~"+(x-1)+","+y+","+board[x - 1][y].owner.isPlayerOne()+","+board[x - 1][y].sn;
                    board[x - 1][y] = null;
                    ckilled++;
                }
            }
            // checking if the right piece was killed &not king
            if (x < 10 && board[x + 1][y] != null && board[x + 1][y].getType() != "♔") {
                if (board[x][y].getOwner() != board[x + 1][y].getOwner() && x + 1 == 10) {
                    s=s+"~"+(x+1)+","+y+","+board[x + 1][y].owner.isPlayerOne()+","+board[x + 1][y].sn;
                    board[x + 1][y] = null;
                    ckilled++;
                } else if (x + 1 < 10 && board[x + 2][y] != null && board[x][y].getOwner() != board[x + 1][y].getOwner() && board[x + 2][y].getOwner() != board[x + 1][y].getOwner() && board[x + 2][y].getType() != "♔") {
                    s=s+"~"+(x+1)+","+y+","+board[x + 1][y].owner.isPlayerOne()+","+board[x + 1][y].sn;
                    board[x + 1][y] = null;
                    ckilled++;
                }
            }
            // checking if the above piece was killed &not king
            if (y < 10 && board[x][y + 1] != null && board[x][y + 1].getType() != "♔") {
                if (board[x][y].getOwner() != board[x][y + 1].getOwner() && y + 1 == 10) {
                    s=s+"~"+x+","+(y+1)+","+board[x][y+1].owner.isPlayerOne()+","+board[x][y+1].sn;
                    board[x][y + 1] = null;
                    ckilled++;
                } else if (y + 1 < 10 && board[x][y + 2] != null && board[x][y].getOwner() != board[x][y + 1].getOwner() && board[x][y + 2].getOwner() != board[x][y + 1].getOwner() && board[x][y + 2].getType() != "♔") {
                    s=s+"~"+x+","+(y+1)+","+board[x][y+1].owner.isPlayerOne()+","+board[x][y+1].sn;
                    board[x][y + 1] = null;
                    ckilled++;
                }
            }
            // checking if the bottom piece was killed &not king
            if (y > 0 && board[x][y - 1] != null && board[x][y - 1].getType() != "♔") {
                if (board[x][y].getOwner() != board[x][y - 1].getOwner() && y - 1 == 0) {
                    s=s+"~"+x+","+(y-1)+","+board[x][y-1].owner.isPlayerOne()+","+board[x][y-1].sn;
                    board[x][y - 1] = null;
                    ckilled++;
                } else if (y - 1 > 0 && board[x][y - 2] != null && board[x][y].getOwner() != board[x][y - 1].getOwner() && board[x][y - 2].getOwner() != board[x][y - 1].getOwner() && board[x][y - 2].getType() != "♔") {
                    s=s+"~"+x+","+(y-1)+","+board[x][y-1].owner.isPlayerOne()+","+board[x][y-1].sn;
                    board[x][y - 1] = null;
                    ckilled++;
                }
            }
        }
        // add the killed  if the pawn eat with cornner
        ckilled=ckilled+killedCornner(b);
        s=s+"~^@"+ckilled+"@";
        if (ckilled>0) {
            killed=true;
            while(ckilled>0){
                // add to the pawn the amount killed he did
                board[x][y].addKill();
                ckilled--;
            }

        }

        return killed;

    }
    // check if the pawn eat another pawn with the cornner
    public int killedCornner(Position p){
        int killed=0;
        if(board[p.getX()][p.getY()].getType()=="♔")
            return 0;
        // becauce ther is only 8 options to eat with cornner i will check each one of them
        if(p.getX()==2&&p.getY()==0 &&board[1][0]!=null &&board[1][0].getType()!="♔" &&board[1][0].owner!=board[p.getX()][p.getY()].getOwner()){
            s=s+"~1,0,"+board[1][0].owner.isPlayerOne()+","+board[1][0].sn;
            board[1][0] = null;
            killed++;
        }
        if(p.getX()==0 && p.getY()==2 && board[0][1]!=null &&board[0][1].getType()!="♔"&& board[0][1].owner!=board[p.getX()][p.getY()].getOwner()){
            s=s+"~0,1,"+board[0][1].owner.isPlayerOne()+","+board[0][1].sn;
            board[0][1]=null;
            killed++;
        }
        if(p.getX()==8 && p.getY()==0 && board[9][0]!=null &&board[9][0].getType()!="♔"&& board[9][0].owner!=board[p.getX()][p.getY()].getOwner()){
            s=s+"~9,0,"+board[9][0].owner.isPlayerOne()+","+board[9][0].sn;
            board[9][0]=null;
            killed++;
        }
        if(p.getX()==10 && p.getY()==2 &&board[10][1]!=null&&board[10][1].getType()!="♔"&& board[10][1].owner!=board[p.getX()][p.getY()].getOwner()){
            s=s+"~10,1,"+board[10][1].owner.isPlayerOne()+","+board[10][1].sn;
            board[10][1]=null;
            killed++;
        }
        if(p.getX()==0 && p.getY()==8 &&board[0][9]!=null&&board[0][9].getType()!="♔"&& board[0][9].owner!=board[p.getX()][p.getY()].getOwner()){
            s=s+"~0,9,"+board[0][9].owner.isPlayerOne()+","+board[0][9].sn;
            board[0][9]=null;
            killed++;
        }
        if(p.getX()==2 && p.getY()==10 &&board[1][10]!=null&&board[1][10].getType()!="♔"&& board[1][10].owner!=board[p.getX()][p.getY()].getOwner()){
            s=s+"~1,10,"+board[1][10].owner.isPlayerOne()+","+board[1][10].sn;
            board[1][10]=null;
            killed++;
        }
        if(p.getX()==8 && p.getY()==10 &&board[9][10]!=null&&board[9][10].getType()!="♔"&& board[9][10].owner!=board[p.getX()][p.getY()].getOwner()){
            s=s+"~9,10,"+board[9][10].owner.isPlayerOne()+","+board[9][10].sn;
            board[9][10]=null;
            killed++;
        }
        if(p.getX()==10 && p.getY()==8 &&board[10][9]!=null&&board[10][9].getType()!="♔"&& board[10][9].owner!=board[p.getX()][p.getY()].getOwner()){
            s=s+"~10,9,"+board[10][9].owner.isPlayerOne()+","+board[10][9].sn;
            board[10][9]=null;
            killed++;
        }
        return killed;
    }
    public boolean kingKilled() {
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (board[i][j] != null && board[i][j].getType() == "♔") {
                    int help = 0;
                    // check that the king is srounde and count by how many pawns (need 4 pawns)
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
                        //    board[i][j]=null;
                        // count wins !!!!!!!!!!!!!!!
                        isFinished = true;
                        //player2Turn = true;
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public boolean kingWin() {
        //check each corrner if the the king is ther
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
                    return false;
                }
            }
        }
        return true;
    }

    Comparator<ConcretePiece> CompSteps = new Comparator<ConcretePiece>() {
        @Override
        public int compare(ConcretePiece o1, ConcretePiece o2) {
            if (o1.steps.size() > o2.steps.size())
                return 1;
            if (o1.steps.size() < o2.steps.size())
                return -1;
            if (o1.steps.size() == o2.steps.size()) {
                if (o1.sn > o2.sn)
                    return 1;
                if ((o1.sn <= o2.sn))
                    return -1;
            }
            return 0;
        }
    };
    Comparator<ConcretePiece> CompDis = new Comparator<ConcretePiece>() {
        @Override
        public int compare(ConcretePiece o1, ConcretePiece o2) {
            if (o1.disAllGame() > o2.disAllGame())
                return -1;
            if (o1.disAllGame() < o2.disAllGame())
                return 1;
            if (o1.disAllGame() == o2.disAllGame()) {
                if (!player2Turn){
                    if(o1.owner==o2.owner){
                        if (o1.sn > o2.sn)
                            return 1;
                        if (o1.sn < o2.sn)
                            return -1;
                    }
                    else if(o1.owner==p2){
                        return 1;
                    }
                    else if(o1.owner==p1){
                        return -1;
                    }
                }
                else{
                    if(o1.owner==o2.owner){
                        if (o1.sn > o2.sn)
                            return 1;
                        if (o1.sn < o2.sn)
                            return -1;
                    }
                    else if(o1.owner==p2){
                        return -1;
                    }
                    else if(o1.owner==p1){
                        return 1;
                    }

                }
            }
            return 0;
        }
    };
    Comparator<ConcretePiece> CompKill = new Comparator<ConcretePiece>() {
        @Override
        public int compare(ConcretePiece o1, ConcretePiece o2) {
            if (o1.kills > o2.kills)
                return -1;
            if (o1.kills < o2.kills)
                return 1;
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
    Comparator<Position> CompPiceAtPos= new Comparator<Position>() {
        @Override
        public int compare(Position p1, Position p2) {
            if (p1.getCount() > p2.getCount())
                return -1;
            if (p1.getCount() < p2.getCount())
                return 1;
            if (p1.getCount() == p2.getCount()) {
                if (p1.getX() < p2.getX())
                    return -1;
                if (p1.getX() > p2.getX())
                    return 1;
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

    public void printWin(ConcretePlayer p) {
        //System.out.println("*****************    1   **************");
        pice.sort(CompSteps);
        if (player2Turn) {
            for (int i = 0; i < pice.size(); i++) {
                if (pice.get(i).owner == p1 && pice.get(i).steps.size() > 1) {
                    if (pice.get(i).sn == 7) {
                        System.out.println("K" + pice.get(i).sn + ": " + pice.get(i).toStringSteps());
                    } else {
                        System.out.println("D" + pice.get(i).sn + ": " + pice.get(i).toStringSteps());

                    }
                }
            }
            for (int i = 0; i < pice.size(); i++) {
                if (pice.get(i).owner == p2 && pice.get(i).steps.size() > 1)
                    System.out.println("A" + pice.get(i).sn + ": " + pice.get(i).toStringSteps());
            }

        } else {
            for (int i = 0; i < pice.size(); i++) {
                if (pice.get(i).owner == p2 && pice.get(i).steps.size() > 1)
                    System.out.println("A" + pice.get(i).sn + ": " + pice.get(i).toStringSteps());
            }
            for (int i = 0; i < pice.size(); i++) {
                if (pice.get(i).owner == p1 && pice.get(i).steps.size() > 1)
                    if (pice.get(i).sn == 7) {
                        System.out.println("K" + pice.get(i).sn + ": " + pice.get(i).toStringSteps());
                    } else {
                        System.out.println("D" + pice.get(i).sn + ": " + pice.get(i).toStringSteps());
                    }
            }
        }


        //System.out.println("*****************    2   **** need to count **********");
        for (int i = 0; i < 74; i++)
            System.out.print("*");
        System.out.println("*");

        pice.sort(CompKill);
        for (int i = 0; i < pice.size(); i++) {
            if (pice.get(i).getType() == "♟︎" && pice.get(i).kills >0)
                System.out.println("A" + pice.get(i).sn + ": " + pice.get(i).kills + " kills");
            if (pice.get(i).getType() == "♙" && pice.get(i).kills >0)
                System.out.println("D" + pice.get(i).sn + ": " + pice.get(i).kills + " kills");
            if (pice.get(i).getType() == "♔" && pice.get(i).kills >0)
                System.out.println("K" + pice.get(i).sn + ": " + pice.get(i).kills + " kills");
        }
        //System.out.println("*****************    3   **************");
        for (int i = 0; i < 74; i++)
            System.out.print("*");
        System.out.println("*");
        pice.sort(CompDis);
        for (int i = 0; i < pice.size(); i++) {
            if (pice.get(i).getType() == "♟︎" && pice.get(i).disAllGame() > 0)
                System.out.println("A" + pice.get(i).sn + ": " + pice.get(i).disAllGame() + " squares");
            if (pice.get(i).getType() == "♙" && pice.get(i).disAllGame() > 0)
                System.out.println("D" + pice.get(i).sn + ": " + pice.get(i).disAllGame() + " squares");
            if (pice.get(i).getType() == "♔" && pice.get(i).disAllGame() > 0)
                System.out.println("K" + pice.get(i).sn + ": " + pice.get(i).disAllGame() + " squares");
        }

        // ************* how many piece was here ****************
        //System.out.println("*****************    4   **************");
        for (int i = 0; i < 74; i++)
            System.out.print("*");
        System.out.println("*");
        ArrayList<Position> pCounter = new ArrayList<Position>();
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                pCounter.add((new Position(i,j,count[i][j])));
            }
        }
        pCounter.sort(CompPiceAtPos);
        for(int i=0; i<pCounter.size(); i++){
            if(pCounter.get(i).getCount()>=2){
                int x=pCounter.get(i).getX();
                int y=pCounter.get(i).getY();
                System.out.println("("+x+", "+y+")"+ pCounter.get(i).getCount()+" pieces");
            }
        }
        for (int i = 0; i < 74; i++)
            System.out.print("*");
        System.out.println("*");
    }

    }