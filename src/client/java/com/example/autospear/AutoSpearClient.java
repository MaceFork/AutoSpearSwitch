package com.example.autospear;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;

public class AutoSpearClient implements ClientModInitializer {
    private static final double RANGE_SQ = 10.0 * 10.0;
    private boolean wasNear = false;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClientPlayerEntity p = client.player;
            if (p == null || client.world == null) {
                wasNear = false;
                return;
            }

            boolean near = false;
            for (PlayerEntity o : client.world.getPlayers()) {
                if (o != p && p.squaredDistanceTo(o) <= RANGE_SQ) { near = true; break; }
            }

            if (near && !wasNear && client.currentScreen == null) {
                selectSpear(p);
            }
            wasNear = near;
        });
    }

    private void selectSpear(ClientPlayerEntity p) {
        for (int i = 0; i < 9; i++) {
            ItemStack s = p.getInventory().getStack(i);
            if (Registries.ITEM.getId(s.getItem()).getPath().endsWith("_spear")) {
                if (p.getInventory().getSelectedSlot() != i) {
                    p.getInventory().setSelectedSlot(i);
                }
                return;
            }
        }
    }
                    }
