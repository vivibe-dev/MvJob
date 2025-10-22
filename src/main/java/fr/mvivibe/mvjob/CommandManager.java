package fr.mvivibe.mvjob;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.BiConsumer;

public class CommandManager implements CommandExecutor, TabCompleter {

    private final Map<String, BiConsumer<CommandSender, String[]>> commands = new HashMap<>();
    public static Data Data;

    public CommandManager(Data dataa) {
        Data = dataa;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Vérifie que c'est bien un joueur
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly Players can use this command !");
            return true;
        }

        Player player = (Player) sender;

        if (command.getName().equalsIgnoreCase("mvjob")) {

            // Si aucun argument
            if (args.length == 0) {
                JobGui.openGui(player);
                return true;
            }

            // Gestion des sous-commandes
            if (args[0].equalsIgnoreCase("help")) {
                player.sendMessage("§6===== §aMVJob §6=====");
                player.sendMessage("§a/mvjob help - show this ");
                player.sendMessage("§a/mvjob version - show the build");
                player.sendMessage("§a/mvjob job - open job's menu");
                player.sendMessage("§a/mvjob progress - open progress menu");
                player.sendMessage("§6===================");
                return true; // IMPORTANT : on retourne après le help
            }
            else if (args[0].equalsIgnoreCase("version")){
                player.sendMessage("§aBuild 1.0");
                return true;
            }
            else if (args[0].equalsIgnoreCase("job")){
                JobGui.openGui(player);
                return true;
            }
            else if (args[0].equalsIgnoreCase("pseudo")) {
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /mvjob pseudo [uuid]");
                    return true;
                }

                String uuid_given = args[1];

                try {
                    UUID uuid = UUID.fromString(uuid_given);
                    OfflinePlayer target = Bukkit.getOfflinePlayer(uuid);

                    if (target != null && target.getName() != null) {
                        player.sendMessage("§aThe username for this uuid is: §6" + target.getName());
                    } else {
                        player.sendMessage("§cNo player found with this uuid.");
                    }

                } catch (IllegalArgumentException e) {
                    player.sendMessage("§cinvalid UUID.");
                }
                return true;
                }
                else if (args[0].equalsIgnoreCase("addxp")) {
                    if (!sender.isOp()) {
                        sender.sendMessage("§cYou don't have to permission to use this command.");
                        return true;
                    }

                    if (args.length < 4) {
                        sender.sendMessage("§cUsage: /mvjob addxp [player] [job] [amount]");
                        return true;
                    }

                    String targetName = args[1];
                    String jobName = args[2];
                    float amount;

                    try {
                        amount = Float.parseFloat(args[3]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage("§cThe amount must be valid.");
                        return true;
                    }

                    // Vérifier si le métier est valide
                    if (!Arrays.asList("miner", "lumberjack", "farmer", "hunter", "fisher", "builder").contains(jobName.toLowerCase())) {
                        sender.sendMessage("§cInvalid Job.");
                        return true;
                    }

                    // Trouver l'OfflinePlayer (pour l'UUID)
                    OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
                    if (target == null || target.getUniqueId() == null) {
                        sender.sendMessage("§cNo one found with the username: " + targetName);
                        return true;
                    }

                    // Récupérer l'objet Jobs
                    Jobs job = Mvjob.GetJobsByUUID(target.getUniqueId());
                    if (job == null) {
                        sender.sendMessage("§cimpossible to load data for " + targetName);
                        return true;
                    }

                    // Ajouter l'XP (utiliser la version de JobsManager pour la console/offline)
                    // *NOTE: Nous devons modifier JobsManager.addXp pour accepter CommandSender ou Player*
                    JobsManager.addXp(job, jobName, amount, (Player) target);

                    // Message de succès
                    sender.sendMessage("§aSuccess ! §6" + amount + " XP §aadded to §b" + targetName + " §afor §e" + jobName + ".");
                    return true;
                }
                else if (args[0].equalsIgnoreCase("progress")){
                    JobGui2.openGui(player);
                    return true;
                }



            // Ici, d'autres commandes : job, list, add, remove
            // ...

            // Si aucun des cas n'a matché
            player.sendMessage("§cCommand Unfound /mvjob help to get more information !");
            return true;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();
        Player player = (Player) sender;

        if (!(sender instanceof Player)) return suggestions;

        // Sous-commandes principales
        if (args.length == 1) {
            suggestions.add("job");
            suggestions.add("progress");
            suggestions.add("pseudo");
            suggestions.add("help");
            suggestions.add("version");
            if (player.isOp()){
                suggestions.add("addxp");
            }
            return suggestions;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("addxp") && player.isOp()) {
            // ici tu peux rajouter tous les UUID connus
            List<String> players = Mvjob.GetPlayersName();
            for (String player1 : players){
                suggestions.add(player1);
            }

            return suggestions;
        }

        // /mvjob pseudo [uuid]
        if (args.length == 3 && args[0].equalsIgnoreCase("addxp") && player.isOp()) {
            // ici tu peux rajouter tous les UUID connus
            suggestions.add("miner");
            suggestions.add("lumberjack");
            suggestions.add("hunter");
            suggestions.add("farmer");
            suggestions.add("fisher");
            suggestions.add("builder");
            return suggestions;
        }

        if (args.length == 4 && args[0].equalsIgnoreCase("addxp") && player.isOp()) {
            // ici tu peux rajouter tous les UUID connus
            suggestions.add("1");
            suggestions.add("10");
            suggestions.add("25");
            suggestions.add("50");
            suggestions.add("100");
            suggestions.add("1000");
            suggestions.add("10000");
            return suggestions;
        }

        return suggestions;
    }

}
