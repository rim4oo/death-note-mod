package com.rim4oo.death_note;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.core.particles.ParticleTypes;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class DNItem extends Item {

    private static final ResourceKey<DamageType> DEATH_NOTE_DAMAGE_TYPE =
            ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("death_note", "death_note"));

    public DNItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (!level.isClientSide) {
            double x = player.getX();
            double y = player.getY();
            double z = player.getZ();

            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.SOUL, x, y, z, 25, 1.0, 1.0, 1.0, 0.0);

                List<ServerPlayer> onlinePlayers = serverLevel.players();
                if (!onlinePlayers.isEmpty()) {
                    level.playSound(null, x, y, z, SoundEvents.SOUL_ESCAPE, SoundSource.PLAYERS, 1.0F, 1.0F);

                    Random random = new Random();
                    ServerPlayer targetPlayer = onlinePlayers.get(random.nextInt(onlinePlayers.size()));
                    targetPlayer.playSound(SoundEvents.SOUL_ESCAPE, 1.0F, 1.0F);
                    serverLevel.sendParticles(ParticleTypes.SOUL, targetPlayer.getX(), targetPlayer.getY(), targetPlayer.getZ(), 10, 1.0, 1.0, 1.0, 0.0);

                    Registry<DamageType> damageTypeRegistry = serverLevel.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
                    Holder<DamageType> deathNoteDamageTypeHolder = damageTypeRegistry.getHolderOrThrow(DEATH_NOTE_DAMAGE_TYPE);

                    DamageSource deathNoteSource = new DamageSource(deathNoteDamageTypeHolder);

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            serverLevel.getServer().execute(() -> targetPlayer.hurt(deathNoteSource, Float.MAX_VALUE));
                        }
                    }, 28000);
                }
            }

            level.playSound(null, x, y, z, SoundEvents.SOUL_ESCAPE, SoundSource.PLAYERS, 1.0F, 1.0F);
            player.setItemInHand(hand, ItemStack.EMPTY);
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable("item.death_note.death_note.tooltip"));
    }
}