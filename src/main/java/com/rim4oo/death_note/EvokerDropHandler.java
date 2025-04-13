package com.rim4oo.death_note;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "death_note", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EvokerDropHandler {
    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (event.getEntity() instanceof Evoker) {
            if (event.getEntity().getRandom().nextFloat() < 0.05) {
                ItemStack deathNote = new ItemStack(Death_note.DN_ITEM.get());
                event.getDrops().add(new ItemEntity(event.getEntity().level(),
                        event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), deathNote));
            }
        }
    }
}