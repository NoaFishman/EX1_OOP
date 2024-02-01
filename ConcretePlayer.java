public class ConcretePlayer implements Player {
    private boolean hagana; //true = hagana-player 1 , false = hatkafa-player 2
    private int v; // count the players wins
    // constractor tht get a boolean that said if it is p1 or not
    public ConcretePlayer(boolean c) {
        this.hagana = c;
        // at atart don't have wins
        this.v = 0;
    }
    // return if it is player 1by the olayer boolean that said if it is player 1
    @Override
    public boolean isPlayerOne() {
        return hagana;
    }
    // add win to the player wins counter
    public void addWin() {
        this.v++;
    }
    //return the player amount of wins
    @Override
    public int getWins() {
        return v;
    }
}