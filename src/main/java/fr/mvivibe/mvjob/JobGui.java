package fr.mvivibe.mvjob;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.Arrays;

public class JobGui {
    public static void openGui(Player player) {
        Jobs job = Mvjob.GetJobsByUUID(player.getUniqueId());
        if (job == null) return;

        Inventory inv = Bukkit.createInventory(null, 54, "§6Jobs");

        // Fond décoratif
        ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);
        for (int i = 0; i < 54; i++) inv.setItem(i, glass);

        // Miner
        ItemStack minerLvl = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta minerLvlMeta = minerLvl.getItemMeta();
        minerLvlMeta.setDisplayName("§bMiner §7- §eLevel §f" + job.lvl_miner);
        minerLvlMeta.setLore(Arrays.asList("§7Niveau actuel du métier de mineur"));
        minerLvl.setItemMeta(minerLvlMeta);

        ItemStack minerXp = new ItemStack(Material.COAL_ORE);
        ItemMeta minerXpMeta = minerXp.getItemMeta();
        minerXpMeta.setDisplayName("§bMiner §7- §bXP §f" + job.xp_miner);
        minerXpMeta.setLore(Arrays.asList("§7Expérience totale du métier de mineur"));
        minerXp.setItemMeta(minerXpMeta);

        // Lumberjack
        ItemStack lumberLvl = new ItemStack(Material.IRON_AXE);
        ItemMeta lumberLvlMeta = lumberLvl.getItemMeta();
        lumberLvlMeta.setDisplayName("§aLumberjack §7- §eLevel §f" + job.lvl_lumberjack);
        lumberLvlMeta.setLore(Arrays.asList("§7Niveau actuel du métier de bûcheron"));
        lumberLvl.setItemMeta(lumberLvlMeta);

        ItemStack lumberXp = new ItemStack(Material.OAK_LOG);
        ItemMeta lumberXpMeta = lumberXp.getItemMeta();
        lumberXpMeta.setDisplayName("§aLumberjack §7- §bXP §f" + job.xp_lumberjack);
        lumberXpMeta.setLore(Arrays.asList("§7Expérience totale du métier de bûcheron"));
        lumberXp.setItemMeta(lumberXpMeta);

        // Hunter
        ItemStack hunterLvl = new ItemStack(Material.BOW);
        ItemMeta hunterLvlMeta = hunterLvl.getItemMeta();
        hunterLvlMeta.setDisplayName("§cHunter §7- §eLevel §f" + job.lvl_hunter);
        hunterLvlMeta.setLore(Arrays.asList("§7Niveau actuel du métier de chasseur"));
        hunterLvl.setItemMeta(hunterLvlMeta);

        ItemStack hunterXp = new ItemStack(Material.ARROW);
        ItemMeta hunterXpMeta = hunterXp.getItemMeta();
        hunterXpMeta.setDisplayName("§cHunter §7- §bXP §f" + job.xp_hunter);
        hunterXpMeta.setLore(Arrays.asList("§7Expérience totale du métier de chasseur"));
        hunterXp.setItemMeta(hunterXpMeta);

        // Farmer
        ItemStack farmerLvl = new ItemStack(Material.GOLDEN_HOE);
        ItemMeta farmerLvlMeta = farmerLvl.getItemMeta();
        farmerLvlMeta.setDisplayName("§eFarmer §7- §eLevel §f" + job.lvl_farmer);
        farmerLvlMeta.setLore(Arrays.asList("§7Niveau actuel du métier de fermier"));
        farmerLvl.setItemMeta(farmerLvlMeta);

        ItemStack farmerXp = new ItemStack(Material.WHEAT);
        ItemMeta farmerXpMeta = farmerXp.getItemMeta();
        farmerXpMeta.setDisplayName("§eFarmer §7- §bXP §f" + job.xp_farmer);
        farmerXpMeta.setLore(Arrays.asList("§7Expérience totale du métier de fermier"));
        farmerXp.setItemMeta(farmerXpMeta);

        // Fisher
        ItemStack fisherLvl = new ItemStack(Material.FISHING_ROD);
        ItemMeta fisherLvlMeta = fisherLvl.getItemMeta();
        fisherLvlMeta.setDisplayName("§9Fisher §7- §eLevel §f" + job.lvl_fisher);
        fisherLvlMeta.setLore(Arrays.asList("§7Niveau actuel du métier de pêcheur"));
        fisherLvl.setItemMeta(fisherLvlMeta);

        ItemStack fisherXp = new ItemStack(Material.COD);
        ItemMeta fisherXpMeta = fisherXp.getItemMeta();
        fisherXpMeta.setDisplayName("§9Fisher §7- §bXP §f" + job.xp_fisher);
        fisherXpMeta.setLore(Arrays.asList("§7Expérience totale du métier de pêcheur"));
        fisherXp.setItemMeta(fisherXpMeta);

        // Builder
        ItemStack builderLvl = new ItemStack(Material.BRICKS);
        ItemMeta builderLvlMeta = builderLvl.getItemMeta();
        builderLvlMeta.setDisplayName("§6Builder §7- §eLevel §f" + job.lvl_builder);
        builderLvlMeta.setLore(Arrays.asList("§7Niveau actuel du métier de constructeur"));
        builderLvl.setItemMeta(builderLvlMeta);

        ItemStack builderXp = new ItemStack(Material.OAK_PLANKS);
        ItemMeta builderXpMeta = builderXp.getItemMeta();
        builderXpMeta.setDisplayName("§6Builder §7- §bXP §f" + job.xp_builder);
        builderXpMeta.setLore(Arrays.asList("§7Expérience totale du métier de constructeur"));
        builderXp.setItemMeta(builderXpMeta);

        // Statistiques globales
        ItemStack global = new ItemStack(Material.NETHER_STAR);
        ItemMeta globalMeta = global.getItemMeta();
        globalMeta.setDisplayName("§d§lStatistiques globales");
        globalMeta.setLore(Arrays.asList(
                "§7Total niveaux : §f" + (
                        job.lvl_miner + job.lvl_lumberjack + job.lvl_hunter +
                                job.lvl_farmer + job.lvl_fisher + job.lvl_builder),
                "§7Total XP : §f" + (
                        job.xp_miner + job.xp_lumberjack + job.xp_hunter +
                                job.xp_farmer + job.xp_fisher + job.xp_builder)
        ));
        global.setItemMeta(globalMeta);

        // Placement ordonné
        inv.setItem(11, minerLvl);
        inv.setItem(20, minerXp);

        inv.setItem(13, lumberLvl);
        inv.setItem(22, lumberXp);

        inv.setItem(15, hunterLvl);
        inv.setItem(24, hunterXp);

        inv.setItem(29, farmerLvl);
        inv.setItem(38, farmerXp);

        inv.setItem(31, fisherLvl);
        inv.setItem(40, fisherXp);

        inv.setItem(33, builderLvl);
        inv.setItem(42, builderXp);

        inv.setItem(49, global);

        player.openInventory(inv);
    }
}
