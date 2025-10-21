package fr.mvivibe.mvjob;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class Mvjob extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new Listeners(), this);
        Data data = FileManager.loadData();
        CommandManager cmd = new CommandManager(data);
        getCommand("mvjob").setExecutor(cmd);
        getCommand("mvjob").setTabCompleter(cmd);
    }

    @Override
    public void onDisable() {
        FileManager.saveData(CommandManager.Data);
    }

    public static void savedata(){
        FileManager.saveData(CommandManager.Data);
    }

    public static Jobs GetJobsByUUID(UUID uuid){
        for (Jobs job : CommandManager.Data.jobsList){
            if(job.uuid.equals(uuid)){
                return job;
            }
        }
        return null;
    }
    public static List<String> GetPlayersName() {
        List<String> players = new ArrayList<>();

        for (Jobs job : CommandManager.Data.jobsList) {
            UUID uuid = job.uuid;

            // Essaye de récupérer le joueur en ligne
            Player onlinePlayer = Bukkit.getPlayer(uuid);
            if (onlinePlayer != null) {
                players.add(onlinePlayer.getName());
                continue;
            }

            // Sinon, récupère le joueur offline
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            if (offlinePlayer != null && offlinePlayer.getName() != null) {
                players.add(offlinePlayer.getName());
            }
        }

        return players;
    }
}
