package Main;

/**
 * Created by Wyatt on 12/29/2014.
 */
class WarmVector_Client_Singleplayer {

    public static void main(String[] args) {
        Game game = new Game();
        new Thread(game).start();
    }

}
