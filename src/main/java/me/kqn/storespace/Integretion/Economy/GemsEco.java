package me.kqn.storespace.Integretion.Economy;

import me.xanium.gemseconomy.api.GemsEconomyAPI;
import me.xanium.gemseconomy.currency.Currency;
import org.bukkit.OfflinePlayer;

public class GemsEco implements Economy{
    private GemsEconomyAPI api;
    public GemsEco (){
        api=new GemsEconomyAPI();
    }
    @Override
    public void take(OfflinePlayer player, double amount) {
        
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return false;
    }

    @Override
    public double get(OfflinePlayer player) {
        return 0;
    }

    @Override
    public void give(OfflinePlayer player, double amount) {

    }
}
