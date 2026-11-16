package com.example.autospear;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;

public class AutoSpearClient implements ClientModInitializer {
    private static final double RANGE_SQ = 6.0 * 6.0;
    private static final double FALL_MIN = 8.0;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClientPlayerEntity p = client.player;
            if (p == null || client.world == null || client.currentScreen != null) return;

            boolean near = false;
            for (PlayerEntity o : client.world.getPlayers()) {
                if (o != p && p.squaredDistanceTo(o) <= RANGE_SQ) { near = true; break; }
            }
            if (!near) return;

            if (!p.isOnGround() && p.fallDistance >= FALL_MIN) {
                select(p, true);
            } else if (p.isOnGround()) {
                select(p, false);
            }
        });
    }

    private void select(ClientPlayerEntity p, boolean spear) {
        for (int i = 0; i < 9; i++) {
            ItemStack s = p.getInventory().getStack(i);
            boolean match = spear
                ? Registries.ITEM.getId(s.getItem()).getPath().endsWith("_spear")
                : s.isOf(Items.GOLDEN_APPLE) || s.isOf(Items.ENCHANTED_GOLDEN_APPLE);
            if (match) {
                if (p.getInventory().getSelectedSlot() != i) {
                    p.getInventory().setSelectedSlot(i);
                }
                return;
            }
        }
    }
        }
