package me.uten2c.simplepacketlistener;

import org.bukkit.entity.Player;

public class SendPacket {

    private Player player;
    private Object packet;

    public SendPacket(Player player, Object packet) {
        this.player = player;
        this.packet = packet;
    }

    public Player getPlayer() {
        return player;
    }

    public Object getPacket() {
        return packet;
    }
}
