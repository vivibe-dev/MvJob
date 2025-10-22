package fr.mvivibe.mvjob;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class JobGui2 {

    // --- Configuration des icônes de statut des récompenses ---
    private static final Material UNLOCKED_ICON = Material.LIME_STAINED_GLASS_PANE;
    private static final Material LOCKABLE_ICON = Material.YELLOW_STAINED_GLASS_PANE;
    private static final Material LOCKED_ICON = Material.RED_STAINED_GLASS_PANE;

    public static void openGui(Player player) {
        Jobs job = Mvjob.GetJobsByUUID(player.getUniqueId());
        if (job == null) return;

        Inventory inv = Bukkit.createInventory(null, 54, "§6Jobs progress");

        // --- Fond décoratif ---
        ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glassMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        glass.setItemMeta(glassMeta);
        for (int i = 0; i < 54; i++) inv.setItem(i, glass);

        // --- Définition des métiers avec leur slot principal + 5 récompenses ---
        record JobLayout(String key, String name, Material icon, int mainSlot, int... rewardSlots) {}
        JobLayout[] layouts = new JobLayout[] {
                new JobLayout("miner", "§bMiner", Material.DIAMOND_PICKAXE, 1, 2, 3, 4, 5, 6),
                new JobLayout("lumberjack", "§aLumberjack", Material.IRON_AXE, 10, 11, 12, 13, 14, 15),
                new JobLayout("hunter", "§cHunter", Material.BOW, 19, 20, 21, 22, 23, 24),
                new JobLayout("farmer", "§eFarmer", Material.WHEAT, 28, 29, 30, 31, 32, 33),
                new JobLayout("fisher", "§9Fisher", Material.FISHING_ROD, 37, 38, 39, 40, 41, 42),
                new JobLayout("builder", "§6Builder", Material.BRICK, 46, 47, 48, 49, 50, 51)
        };

        // --- Placement automatisé des items ---
        for (JobLayout layout : layouts) {
            inv.setItem(layout.mainSlot(), createJobStatsItem(job, layout.key(), layout.name(), layout.icon()));
            placeRewards(inv, job, layout.key(), layout.rewardSlots());
        }

        player.openInventory(inv);
    }

    // --- Fonctions de Création d'Items ---

    /**
     * Crée l'item principal affichant le Niveau et l'XP total.
     */
    private static ItemStack createJobStatsItem(Jobs job, String jobKey, String displayName, Material lvlIcon) {
        int level = JobsManager.getLevel(job, jobKey);
        float xp = JobsManager.getXp(job, jobKey);

        ItemStack item = new ItemStack(lvlIcon);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(displayName + " §7- §elvl §f" + level);

        List<String> lore = new ArrayList<>();
        lore.add("§7Total xp: §b" + (int)xp);

        // On travaille sur le niveau MAXIMUM (le niveau 5 dans ce cas)
        if (level < 5) {

            // --- CALCUL RÉEL DE L'XP NÉCESSAIRE ---

            // 1. Calculer l'XP nécessaire pour atteindre le PROCHAIN niveau.
            float xpTarget = getXpRequiredForLevel(level + 1);

            // 2. Calculer le montant d'XP qu'il manque au joueur (s'assurer qu'il n'est pas négatif)
            float xpMissing = Math.max(0, xpTarget - xp);

            // Affichage
            lore.add("§7Next level: §f" + (level + 1) + " §7(§b" + (int)xpMissing + " XP§7)");

        } else {
            lore.add("§6Maximal level reached !");
        }

        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }

    private static float getXpRequiredForLevel(int level) {
        if (level <= 0) return 0.0f;

        // Le niveau 1 nécessite 5000 XP
        if (level == 1) return 2500f;

        // Les niveaux suivants suivent la progression 5000 * 4^(N-1)
        // Math.pow retourne un double, on le caste en float
        return (float) (2500.0 * Math.pow(1.5, level - 1));
    }
    /**
     * Crée l'item de récompense pour un niveau spécifique.
     */
    private static ItemStack createRewardItem(Jobs job, String jobKey, int level) {
        int currentLevel = JobsManager.getLevel(job, jobKey);

        Material icon;
        String statusPrefixColor;

        if (currentLevel >= level) {
            icon = UNLOCKED_ICON;
            statusPrefixColor = "§a"; // Débloqué
        } else if (currentLevel == level - 1) {
            icon = LOCKABLE_ICON;
            statusPrefixColor = "§e"; // Prochain
        } else {
            icon = LOCKED_ICON;
            statusPrefixColor = "§c"; // Verrouillé
        }

        ItemStack item = new ItemStack(icon);
        ItemMeta meta = item.getItemMeta();

        // Nom compact
        meta.setDisplayName(statusPrefixColor + "[lvl " + level + "] §7- " + getRewardDescription(jobKey, level));

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Place les 5 items de récompenses dans l'inventaire.
     */
    private static void placeRewards(Inventory inv, Jobs job, String jobKey, int... slots) {
        // Le tableau 'slots' doit contenir exactement 5 slots pour Nv 1 à Nv 5
        for (int i = 0; i < 5; i++) {
            if (i < slots.length) {
                inv.setItem(slots[i], createRewardItem(job, jobKey, i + 1));
            }
        }
    }

    /**
     * Récupère la description textuelle de la récompense pour le niveau donné.
     * (Logique réutilisée sans attributs génériques)
     */
    private static String getRewardDescription(String jobName, int level) {
        switch (jobName.toLowerCase()) {
            case "miner":
                return switch (level) {
                    case 1 -> "Permanent night vision";
                    case 2 -> "5% chance to mine a 5x5x5 area while keeping ores";
                    case 3 -> "Permanent Resistance I";
                    case 4 -> "Permanent Haste I";
                    case 5 -> "2% chance of Auto-Smelt";
                    default -> "Unknown.";
                };
            case "lumberjack":
                return switch (level) {
                    case 1 -> "35% chance to get Speed";
                    case 2 -> "20% chance to get double logs";
                    case 3 -> "20% chance to get +1 sapling";
                    case 4 -> "Permanent Strength I";
                    case 5 -> "Cuts the entire tree";
                    default -> "Unknown.";
                };
            case "farmer":
                return switch (level) {
                    case 1 -> "5% chance of double yield";
                    case 2 -> "10% chance of instant growth";
                    case 3 -> "5% chance to drop rare food or ore";
                    case 4 -> "1% chance to restore 0.5 heart";
                    case 5 -> "10% chance of additional drops";
                    default -> "Unknown.";
                };
            case "hunter":
                return switch (level) {
                    case 1 -> "Speed I after a kill";
                    case 2 -> "15% chance of double animal drops";
                    case 3 -> "Permanent Absorption";
                    case 4 -> "5% chance to deal 2 extra hearts";
                    case 5 -> "Regenerate 5 hearts per kill";
                    default -> "Unknown.";
                };
            case "fisher":
                return switch (level) {
                    case 1 -> "Permanent Water Breathing";
                    case 2 -> "10% chance of double drop";
                    case 3 -> "5% more treasure";
                    case 4 -> "Permanent Dolphin's Grace";
                    case 5 -> "25% chance to drop 10x fish";
                    default -> "Unknown.";
                };
            case "builder":
                return switch (level) {
                    case 1 -> "Resistance II infinite";
                    case 2 -> "10% chance to recover the placed block";
                    case 3 -> "Permanent Haste II";
                    case 4 -> "5% chance for 20s of Haste V";
                    case 5 -> "3% chance to not consume the block";
                    default -> "Unknown.";
                };
            default:
                return "Unknown job.";
        }
    }
}