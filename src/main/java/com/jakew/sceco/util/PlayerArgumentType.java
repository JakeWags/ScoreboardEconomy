package com.jakew.sceco.util;

import net.minecraft.command.argument.EntityArgumentType;


public class PlayerArgumentType extends EntityArgumentType {
    protected PlayerArgumentType(boolean singleTarget, boolean playersOnly) {
        super(singleTarget, playersOnly);
    }

    public static PlayerArgumentType playerArgumentType(boolean singleTarget) {
        return new PlayerArgumentType(singleTarget, true);
    }
}
