package fr.mvivibe.mvjob;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection; // Import ajouté pour les helpers

public class JobsManager {

    private static final double BASE_XP = 2500;
    private static final double EXP_GROWTH = 1.5; // exponentiel

    // XP gagnée par bloc/poisson/action
    private static final Map<Material, Float> minerXP = new HashMap<>();
    private static final Map<Material, Float> lumberjackXP = new HashMap<>();
    private static final Map<Material, Float> farmerXP = new HashMap<>();
    private static final Map<Material, Float> hunterXP = new HashMap<>();
    private static final Map<Material, Float> fisherXP = new HashMap<>();
    private static final Map<Material, Float> builderXP = new HashMap<>();

    static {
        // MINER
        minerXP.put(Material.COAL_ORE, 3f);
        minerXP.put(Material.DEEPSLATE_COAL_ORE, 4f);
        minerXP.put(Material.IRON_ORE, 6f);
        minerXP.put(Material.DEEPSLATE_IRON_ORE, 8f);
        minerXP.put(Material.GOLD_ORE, 10f);
        minerXP.put(Material.DEEPSLATE_GOLD_ORE, 15f);
        minerXP.put(Material.DIAMOND_ORE, 25f);
        minerXP.put(Material.DEEPSLATE_DIAMOND_ORE, 35f);
        minerXP.put(Material.EMERALD_ORE, 40f);
        minerXP.put(Material.DEEPSLATE_EMERALD_ORE, 50f);
        minerXP.put(Material.NETHER_QUARTZ_ORE, 20f);
        minerXP.put(Material.ANCIENT_DEBRIS, 100f);
        minerXP.put(Material.LAPIS_ORE, 15f);
        minerXP.put(Material.DEEPSLATE_LAPIS_ORE, 20f);
        minerXP.put(Material.REDSTONE_ORE, 10f);
        minerXP.put(Material.DEEPSLATE_REDSTONE_ORE, 15f);

        // LUMBERJACK
        lumberjackXP.put(Material.OAK_LOG, 3f);
        lumberjackXP.put(Material.BIRCH_LOG, 3f);
        lumberjackXP.put(Material.SPRUCE_LOG, 4f);
        lumberjackXP.put(Material.JUNGLE_LOG, 4f);
        lumberjackXP.put(Material.ACACIA_LOG, 5f);
        lumberjackXP.put(Material.DARK_OAK_LOG, 5f);
        lumberjackXP.put(Material.MANGROVE_LOG, 6f);
        lumberjackXP.put(Material.OAK_LEAVES, 1f);
        lumberjackXP.put(Material.BIRCH_LEAVES, 1f);
        lumberjackXP.put(Material.SPRUCE_LEAVES, 1f);
        lumberjackXP.put(Material.JUNGLE_LEAVES, 1f);
        lumberjackXP.put(Material.ACACIA_LEAVES, 1f);
        lumberjackXP.put(Material.DARK_OAK_LEAVES, 1f);
        lumberjackXP.put(Material.MANGROVE_LEAVES, 1f);

        // FARMER
        farmerXP.put(Material.WHEAT, 2f);
        farmerXP.put(Material.CARROTS, 3f);
        farmerXP.put(Material.POTATOES, 3f);
        farmerXP.put(Material.BEETROOTS, 4f);
        farmerXP.put(Material.SWEET_BERRIES, 5f);
        farmerXP.put(Material.NETHER_WART, 6f);
        farmerXP.put(Material.MELON, 5f);
        farmerXP.put(Material.PUMPKIN, 5f);

        // HUNTER
        hunterXP.put(Material.BEEF, 4f);
        hunterXP.put(Material.PORKCHOP, 4f);
        hunterXP.put(Material.CHICKEN, 3f);
        hunterXP.put(Material.RABBIT, 6f);
        hunterXP.put(Material.ROTTEN_FLESH, 2f);
        hunterXP.put(Material.MUTTON, 5f);
        hunterXP.put(Material.SPIDER_EYE, 3f);

        // FISHER
        fisherXP.put(Material.COD, 3f);
        fisherXP.put(Material.SALMON, 4f);
        fisherXP.put(Material.TROPICAL_FISH, 8f);
        fisherXP.put(Material.PUFFERFISH, 6f);
        fisherXP.put(Material.ENCHANTED_BOOK, 50f);
        fisherXP.put(Material.BOW, 20f);

        // BUILDER
        builderXP.put(Material.OAK_PLANKS, 2f);
        builderXP.put(Material.BIRCH_PLANKS, 2f);
        builderXP.put(Material.SPRUCE_PLANKS, 2f);
        builderXP.put(Material.JUNGLE_PLANKS, 2f);
        builderXP.put(Material.ACACIA_PLANKS, 3f);
        builderXP.put(Material.DARK_OAK_PLANKS, 3f);
        builderXP.put(Material.MANGROVE_PLANKS, 3f);
        builderXP.put(Material.STRIPPED_OAK_LOG, 2f);
        builderXP.put(Material.STRIPPED_BIRCH_LOG, 2f);
        builderXP.put(Material.STRIPPED_SPRUCE_LOG, 2f);
        // ... tous les bois restants

        // PIERRES & BRICKS
        builderXP.put(Material.STONE, 1f);
        builderXP.put(Material.COBBLESTONE, 1f);
        builderXP.put(Material.SMOOTH_STONE, 2f);
        builderXP.put(Material.STONE_BRICKS, 4f);
        builderXP.put(Material.CRACKED_STONE_BRICKS, 5f);
        builderXP.put(Material.MOSSY_STONE_BRICKS, 5f);
        builderXP.put(Material.CHISELED_STONE_BRICKS, 6f);
        builderXP.put(Material.BRICKS, 4f);
        builderXP.put(Material.NETHER_BRICKS, 5f);
        builderXP.put(Material.RED_NETHER_BRICKS, 6f);
        builderXP.put(Material.DEEPSLATE, 2f);
        builderXP.put(Material.DEEPSLATE_TILES, 10f);
        builderXP.put(Material.DEEPSLATE_BRICKS, 9f);
        builderXP.put(Material.POLISHED_DIORITE, 3f);
        builderXP.put(Material.POLISHED_GRANITE, 3f);
        builderXP.put(Material.POLISHED_ANDESITE, 3f);

        // CONCRETES & TERRACOTTA
        builderXP.put(Material.WHITE_CONCRETE, 4f);
        builderXP.put(Material.ORANGE_CONCRETE, 4f);
        builderXP.put(Material.MAGENTA_CONCRETE, 4f);
        builderXP.put(Material.LIGHT_BLUE_CONCRETE, 4f);
        builderXP.put(Material.YELLOW_CONCRETE, 4f);
        builderXP.put(Material.LIME_CONCRETE, 4f);
        builderXP.put(Material.PINK_CONCRETE, 4f);
        builderXP.put(Material.GRAY_CONCRETE, 4f);
        builderXP.put(Material.LIGHT_GRAY_CONCRETE, 4f);
        builderXP.put(Material.CYAN_CONCRETE, 4f);
        builderXP.put(Material.PURPLE_CONCRETE, 4f);
        builderXP.put(Material.BLUE_CONCRETE, 4f);
        builderXP.put(Material.BROWN_CONCRETE, 4f);
        builderXP.put(Material.GREEN_CONCRETE, 4f);
        builderXP.put(Material.RED_CONCRETE, 4f);
        builderXP.put(Material.BLACK_CONCRETE, 4f);
        builderXP.put(Material.WHITE_TERRACOTTA, 3f);
        builderXP.put(Material.ORANGE_TERRACOTTA, 3f);
        builderXP.put(Material.MAGENTA_TERRACOTTA, 3f);
        // ... toutes les autres couleurs

        // VERRE
        builderXP.put(Material.GLASS, 3f);
        builderXP.put(Material.WHITE_STAINED_GLASS, 4f);
        builderXP.put(Material.BLACK_STAINED_GLASS, 4f);
        // ... toutes les couleurs

        // BLOCS RARES / NETHER / END
        builderXP.put(Material.OBSIDIAN, 10f);
        builderXP.put(Material.END_STONE, 5f);
        builderXP.put(Material.NETHER_QUARTZ_ORE, 8f);
        builderXP.put(Material.PRISMARINE, 8f);
        builderXP.put(Material.PRISMARINE_BRICKS, 9f);
        builderXP.put(Material.DARK_PRISMARINE, 9f);

        // ETC
        builderXP.put(Material.SANDSTONE, 2f);
        builderXP.put(Material.CHISELED_SANDSTONE, 4f);
        builderXP.put(Material.RED_SANDSTONE, 2f);
        builderXP.put(Material.CHISELED_RED_SANDSTONE, 4f);
        builderXP.put(Material.MOSS_CARPET, 1f);
        builderXP.put(Material.CLAY, 2f);
        builderXP.put(Material.TERRACOTTA, 3f);
        builderXP.put(Material.SOUL_SAND, 3f);
        builderXP.put(Material.SOUL_SOIL, 2f);
        builderXP.put(Material.CRYING_OBSIDIAN, 10f);
    }

    // --- Fonctions d'accès au niveau (Rendu public pour Reward) ---
    public static int getLevel(Jobs job, String name) {
        return switch (name.toLowerCase()) {
            case "miner" -> job.lvl_miner;
            case "lumberjack" -> job.lvl_lumberjack;
            case "farmer" -> job.lvl_farmer;
            case "hunter" -> job.lvl_hunter;
            case "fisher" -> job.lvl_fisher;
            case "builder" -> job.lvl_builder;
            default -> 0;
        };
    }

    public static float getXp(Jobs job, String name) {
        return switch (name.toLowerCase()) {
            case "miner" -> job.xp_miner;
            case "lumberjack" -> job.xp_lumberjack;
            case "farmer" -> job.xp_farmer;
            case "hunter" -> job.xp_hunter;
            case "fisher" -> job.xp_fisher;
            case "builder" -> job.xp_builder;
            default -> 0f;
        };
    }

    private static void setXpAndLevel(Jobs job, String name, float xp, int lvl) {
        switch (name.toLowerCase()) {
            case "miner" -> { job.xp_miner = xp; job.lvl_miner = lvl; }
            case "lumberjack" -> { job.xp_lumberjack = xp; job.lvl_lumberjack = lvl; }
            case "farmer" -> { job.xp_farmer = xp; job.lvl_farmer = lvl; }
            case "hunter" -> { job.xp_hunter = xp; job.lvl_hunter = lvl; }
            case "fisher" -> { job.xp_fisher = xp; job.lvl_fisher = lvl; }
            case "builder" -> { job.xp_builder = xp; job.lvl_builder = lvl; }
        }
    }
    // ----------------------------------------------------------------

    public static float getXPForBlock(String jobName, Material block) {
        return switch (jobName.toLowerCase()) {
            case "miner" -> minerXP.getOrDefault(block, 0f);
            case "lumberjack" -> lumberjackXP.getOrDefault(block, 0f);
            case "farmer" -> farmerXP.getOrDefault(block, 0f);
            case "hunter" -> hunterXP.getOrDefault(block, 0f);
            case "fisher" -> fisherXP.getOrDefault(block, 0f);
            case "builder" -> builderXP.getOrDefault(block, 0f);
            default -> 0f;
        };
    }

    public static float getXpRequiredForLevel(int level) {
        if (level <= 1) return (float) BASE_XP;
        return (float) (BASE_XP * Math.pow(EXP_GROWTH, level - 1));
    }

    public static void addXp(Jobs job, String jobName, float amount, Player player) {
        int currentLvl = getLevel(job, jobName);
        float currentXp = getXp(job, jobName);
        float xpNeeded = getXpRequiredForLevel(currentLvl);

        currentXp += amount;

        while (currentLvl < 5 && currentXp >= xpNeeded) {
            currentXp -= xpNeeded;
            currentLvl++;
            player.sendMessage("§a" + jobName + " §7→ §bNiveau §e" + currentLvl + " ! Nouveau passif débloqué.");

            // APPEL : Met à jour les effets permanents après un niveau
            Reward.applyPermanentEffects(player, job);

            xpNeeded = getXpRequiredForLevel(currentLvl);
        }

        setXpAndLevel(job, jobName, currentXp, currentLvl);
        // Note: Assurez-vous que CommandManager et FileManager sont définis ailleurs
        // FileManager.saveData(CommandManager.Data);
    }

    // --- Handlers d'événements (Appelant Reward) ---

    public static void handleBlockBreak(Player player, Block block) {
        Jobs job = Mvjob.GetJobsByUUID(player.getUniqueId());
        if (job == null) return;

        // Appel des récompenses (avant l'XP, pour gérer les drops doubles/smelt)
        Reward.handleMinerReward(player, block, job);
        Reward.handleLumberjackReward(player, block, job);
        Reward.handleFarmerReward(player, block, job);

        addXp(job, "miner", getXPForBlock("miner", block.getType()), player);
        addXp(job, "lumberjack", getXPForBlock("lumberjack", block.getType()), player);
        addXp(job, "farmer", getXPForBlock("farmer", block.getType()), player);
        // Le builder ne gagne pas d'XP en cassant un bloc
    }

    public static void handleEntityKill(Player player, Entity entity) {
        Jobs job = Mvjob.GetJobsByUUID(player.getUniqueId());
        if (job == null) return;

        // Appel des récompenses (pour Sens Aiguisé, Butin Abondant, etc.)
        Reward.handleHunterReward(player, entity, job);

        EntityType type = entity.getType();
        // Le Hunter utilise la fonction spécifique aux entités
        addXp(job, "hunter", getXPForEntity(type), player);
    }

    public static void handleFish(Player player, PlayerFishEvent event) {
        Jobs job = Mvjob.GetJobsByUUID(player.getUniqueId());
        if (job == null) return;

        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;
        if (!(event.getCaught() instanceof org.bukkit.entity.Item)) return;

        // Appel des récompenses (pour Double Poisson, durabilité canne, etc.)
        Reward.handleFisherReward(player, event, job);

        org.bukkit.entity.Item item = (org.bukkit.entity.Item) event.getCaught();
        Material fish = item.getItemStack().getType();

        addXp(job, "fisher", getXPForBlock("fisher", fish), player);
    }

    public static void handleBlockPlace(Player player, Block block) {
        Jobs job = Mvjob.GetJobsByUUID(player.getUniqueId());
        if (job == null) return;

        // Appel des récompenses (pour le Builder, bien que la plupart soient dans les Listeners)
        Reward.handleBuilderReward(player, block, job);

        addXp(job, "builder", getXPForBlock("builder", block.getType()), player);
    }

    // --- Helpers Internes et Publics pour Reward ---

    // Utilisé par le Hunter
    private static float getXPForEntity(EntityType type) {
        return switch(type) {
            case COW -> hunterXP.getOrDefault(Material.BEEF, 0f);
            case PIG -> hunterXP.getOrDefault(Material.PORKCHOP, 0f);
            case CHICKEN -> hunterXP.getOrDefault(Material.CHICKEN, 0f);
            case RABBIT -> hunterXP.getOrDefault(Material.RABBIT, 0f);
            case ZOMBIE -> hunterXP.getOrDefault(Material.ROTTEN_FLESH, 0f);
            default -> 0f; // Toutes les autres entités donnent 0 XP
        };
    }

    // Utilisé par Mineur Nv 5 (Rendu public pour Reward)
    public static boolean isSmeltable(Material type) {
        return type == Material.IRON_ORE || type == Material.DEEPSLATE_IRON_ORE ||
                type == Material.GOLD_ORE || type == Material.DEEPSLATE_GOLD_ORE;
    }

    // Utilisé par Mineur Nv 5 (Rendu public pour Reward)
    public static ItemStack getSmeltResult(Material type) {
        // Le cast à Collection est nécessaire pour l'itération des recettes
        Collection<org.bukkit.inventory.Recipe> recipes = Bukkit.getServer().getRecipesFor(new ItemStack(type, 1));
        for (org.bukkit.inventory.Recipe recipe : recipes) {
            if (recipe instanceof FurnaceRecipe furnaceRecipe) {
                return furnaceRecipe.getResult();
            }
        }
        return null;
    }

    // Utilisé par Fermier Nv 1, 4 (Rendu public pour Reward)
    public static boolean isCrop(Material type) {
        return farmerXP.containsKey(type);
    }

    // Utilisé par Hunter Nv 2 (Rendu public pour Reward)
    public static boolean isAnimal(EntityType type) {
        return type == EntityType.COW || type == EntityType.PIG || type == EntityType.CHICKEN ||
                type == EntityType.SHEEP || type == EntityType.RABBIT;
    }

    // Utilisé par Pêcheur Nv 5 (Rendu public pour Reward)
    public static boolean isFish(Material type) {
        return type == Material.COD || type == Material.SALMON || type == Material.TROPICAL_FISH ||
                type == Material.PUFFERFISH;
    }

    public static void breakConnectedLogs(Block startBlock, Player player, int maxBlocks) {
        Material logType = startBlock.getType();
        recursiveBreak(startBlock, logType, player, 0, maxBlocks);
    }

    // Fonction interne récursive
    private static int recursiveBreak(Block block, Material logType, Player player, int count, int maxBlocks) {
        // Si la limite est atteinte, ou si ce n'est pas le type de bois, on s'arrête
        if (block == null || block.getType() != logType || count >= maxBlocks) {
            return count;
        }

        // Casser le bloc (déclenche le drop et potentiellement l'XP, si non annulé)
        // IMPORTANT: On utilise le main hand pour le contexte de la casse (pour le Toucher de Soie ou la Fortune)
        block.breakNaturally(player.getInventory().getItemInMainHand());
        count++;

        // Parcourir les 26 blocs autour
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) continue; // Ignorer le bloc central

                    Block nearby = block.getRelative(x, y, z);

                    if (nearby.getType() == logType) {
                        count = recursiveBreak(nearby, logType, player, count, maxBlocks);
                    }
                }
            }
        }

        return count;
    }
}