package me.uten2c.simplepacketlistener;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PacketHandler extends ChannelDuplexHandler {

    private static List<Function<ReceivePacket, Boolean>> receiveFunctionList = new ArrayList<>();
    private static List<Function<SendPacket, Boolean>> sendFunctionList = new ArrayList<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        boolean cancel = false;
        for (Function<ReceivePacket, Boolean> function : receiveFunctionList) {
            if (function.apply(new ReceivePacket(PacketListener.channelMap.get(this), msg))) {
                cancel = true;
            }
        }
        if (!cancel) {
            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        boolean cancel = false;
        for (Function<SendPacket, Boolean> function : sendFunctionList) {
            if (function.apply(new SendPacket(PacketListener.channelMap.get(this), msg))) {
                cancel = true;
            }
        }
        if (!cancel) {
            super.write(ctx, msg, promise);
        }
    }

    public static void addReceiveListener(Function<ReceivePacket, Boolean> function) {
        receiveFunctionList.add(function);
    }

    public static void addSendListener(Function<SendPacket, Boolean> function) {
        sendFunctionList.add(function);
    }
}
