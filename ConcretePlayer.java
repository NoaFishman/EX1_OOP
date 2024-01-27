public class ConcretePlayer implements Player {
    private boolean hagana; //true = hagana-player 1 , false = hatkafa-player 2
    private int v;

    public ConcretePlayer(boolean c) {
        this.hagana = c;
        this.v = 0;
    }

    @Override
    public boolean isPlayerOne() {
        return hagana;
    }

    public void addWin() {
        this.v++;
    }

    @Override
    public int getWins() {
        return v;
    }
}