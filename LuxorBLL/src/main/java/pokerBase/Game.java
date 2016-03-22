package pokerBase;
import java.util.ArrayList;
import java.util.UUID;
import pokerBase.Player;
import pokerBase.Table;

public class Game {

	private UUID GameID;
	private UUID TableID;
	private ArrayList<Player> GamePlayers = new ArrayList<Player>();
	public Game(UUID gameID, UUID tableID, ArrayList<Player> gamePlayers) {
		super();
		GameID = gameID;
		TableID = tableID;
		GamePlayers = gamePlayers;
	}
	public UUID getGameID() {
		return GameID;
	}
	public void setGameID(UUID gameID) {
		GameID = gameID;
	}
	public UUID getTableID() {
		return TableID;
	}
	public void setTableID(UUID tableID) {
		TableID = tableID;
	}
	public ArrayList<Player> getGamePlayers() {
		return GamePlayers;
	}
	public void setGamePlayers(ArrayList<Player> gamePlayers) {
		GamePlayers = gamePlayers;
	}
	public void AddPlayerToGame( Player Player, Table Table){
		GamePlayers.add(Player);
		
	}
}