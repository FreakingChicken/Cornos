package me.constantindev.ccl.module.ext;

import me.constantindev.ccl.etc.MType;
import me.constantindev.ccl.etc.base.Module;

public class BuildLimit extends Module {
    public BuildLimit() {
        super("BuildLimit", "Adds an extra layer of building height", MType.WORLD);
    }
    // Logic: PacketTryUseItemOnBlockHook.java
}
