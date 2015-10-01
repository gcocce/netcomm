package basenetgame.client;

import basenetgame.common.Packet;;

public interface PacketListener {
	void OnPacketReceived(Packet p);
}
