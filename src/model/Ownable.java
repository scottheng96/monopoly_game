package model;

import model.player.Player;

@Deprecated
public interface Ownable {
    public Player getOwner();
    public void changeOwner(Player newOwner);
}
