package me.uten2c.simplepacketlistener;

import org.bukkit.entity.Player;

public class ReceivePacket {
    private Player player;
    private Object packet;

    public ReceivePacket(Player player, Object packet) {
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