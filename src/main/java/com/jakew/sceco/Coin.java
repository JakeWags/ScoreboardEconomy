package com.jakew.sceco;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import static com.jakew.sceco.ScoreboardEconomy.incrementCredits;

public class Coin extends Item {
    public Coin(Settings settings) {
        super(settings);
    }

    /*  COIN use behavior
     *  Expected: When player right clicks with credits in hand, those credits are added to their scoreboard score
     *            and the coin items are removed from the player's hand.
     */
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        int handAmount = playerEntity.getStackInHand(hand).getCount();

        incrementCredits(world.getScoreboard(), playerEntity.getEntityName(), handAmount);
        playerEntity.getStackInHand(hand).decrement(handAmount);

        playerEntity.playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 0.6F, 1.0F);

        return TypedActionResult.success(playerEntity.getStackInHand(hand));
    }
}
