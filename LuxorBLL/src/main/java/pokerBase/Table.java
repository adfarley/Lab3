package pokerBase;
import java.util.UUID;
import java.util.ArrayList;

public class Table {
	
	private UUID TableID;
	private ArrayList<Player> TablePlayers = new ArrayList<Player>();
	public Table(UUID tableID, ArrayList<Player> tablePlayers) {
		super();
		TableID = tableID;
		TablePlayers = tablePlayers;
	}
	public UUID getTableID() {
		return TableID;
	}
	public void setTableID(UUID tableID) {
		TableID = tableID;
	}
	public ArrayList<Player> getTablePlayers() {
		return TablePlayers;
	}
	public void setTablePlayers(ArrayList<Player> tablePlayers) {
		TablePlayers = tablePlayers;
	}
	
	

}
