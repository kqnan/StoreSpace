package me.kqn.storespace.Integretion.Permission;

import org.bukkit.OfflinePlayer;

import java.util.UUID;

public interface Permission {
    public boolean hasPerm(UUID playerID, String perm);
}
