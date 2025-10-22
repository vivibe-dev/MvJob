package fr.mvivibe.mvjob;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.Recipe;

import java.util.Random;
import java.util.Collection; // Import pour les recettes

public class Reward {

    private static final Random random = new Random();
    // Durée maximale (en ticks) pour un effet "permanent"
    private static final int PERMANENT_DURATION = Integer.MAX_VALUE;

    // =========================================================================
    // --- PARTIE 1 : APPLICATION DES EFFETS PERMANENTS (Scheduler & Level Up) ---
    // =========================================================================

    /**
     * Applique ou retire les effets de potion permanents basés sur le niveau du métier.
     * Doit être appelée par un Scheduler et après un Level Up dans JobsManager.
     */
    public static void applyPermanentEffects(Player player, Jobs job) {

        // --- MINEUR ---
        // Nv 1: Vision Profonde (Vision Nocturne)
        if (JobsManager.getLevel(job, "miner") >= 1) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, PERMANENT_DURATION, 0, false, false));
        } else {
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        }
        // Nv 3: Résistance Tellurique (Résistance I)
        if (JobsManager.getLevel(job, "miner") >= 3) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, PERMANENT_DURATION, 0, false, false));
        } else {
            player.removePotionEffect(PotionEffectType.RESISTANCE);
        }
        // Nv 4: Force Minière (Hâte I)
        if (JobsManager.getLevel(job, "miner") >= 4) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, PERMANENT_DURATION, 0, false, false));
        } else {
            player.removePotionEffect(PotionEffectType.HASTE);
        }

        // --- BUCHERON ---
        // Nv 4: Poigne du Forgeron (Force I)
        if (JobsManager.getLevel(job, "lumberjack") >= 4) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, PERMANENT_DURATION, 0, false, false));
        } else {
            player.removePotionEffect(PotionEffectType.STRENGTH);
        }

        // --- PECHEUR ---
        // Nv 1: Poumon Marin (Respiration Aquatique)
        if (JobsManager.getLevel(job, "fisher") >= 1) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, PERMANENT_DURATION, 0, false, false));
        } else {
            player.removePotionEffect(PotionEffectType.WATER_BREATHING);
        }
        // Nv 4: Grâce des Dauphins
        if (JobsManager.getLevel(job, "fisher") >= 4) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, PERMANENT_DURATION, 0, false, false));
        } else {
            player.removePotionEffect(PotionEffectType.DOLPHINS_GRACE);
        }

        // --- BUILDER ---
        // Nv 3: Rapidité de Pose (Hâte I)
        if (JobsManager.getLevel(job, "builder") >= 3) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, PERMANENT_DURATION, 0, false, false));
        } else {
            player.removePotionEffect(PotionEffectType.HASTE);
        }

        // --- ATTRIBUTS PERMANENTS ---

        // Hunter Nv 3: Cœur de Traqueur (Santé Maximale 21 PV)
        double maxHealth = 20.0;
        if (JobsManager.getLevel(job, "hunter") >= 3) {
            maxHealth = 21.0;
            player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, PERMANENT_DURATION, 0, false, false));
        } else {
            player.removePotionEffect(PotionEffectType.HEALTH_BOOST);
        }

        if (player.getHealth() > maxHealth) {
            player.setHealth(maxHealth);
        }

        // Builder Nv 1: Stabilité (Knockback Resistance approximée)
        if (JobsManager.getLevel(job, "builder") >= 1) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, PERMANENT_DURATION, 0, false, false));
        } else {
            player.removePotionEffect(PotionEffectType.RESISTANCE);
        }
        if (JobsManager.getLevel(job, "builder") >= 3) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, PERMANENT_DURATION, 2, false, false));
        } else {
            player.removePotionEffect(PotionEffectType.HASTE);
        }
    }

    // =========================================================================
    // --- PARTIE 2 : LOGIQUE D'ÉVÉNEMENT CHANCEUX (Handle Actions) ---
    // =========================================================================

    // --- MINEUR ---
    public static void handleMinerReward(Player player, Block block, Jobs job) {
        int lvlMiner = JobsManager.getLevel(job, "miner");

        // Nv 5: Fusion Arcanique (2% d'Auto-Smelt)
        if (lvlMiner >= 5 && JobsManager.isSmeltable(block.getType()) && random.nextDouble() < 0.02) {
            ItemStack result = JobsManager.getSmeltResult(block.getType());

            if (result != null) {
                // Nécessite que le BlockBreakEvent annule le drop du minerai brut.
                block.getWorld().dropItemNaturally(block.getLocation(), result);
                player.sendMessage("§6Miner §7→ §eAuto Smelt !");
            }
        }
        // Nv 2 (Toucher Délicat) et Nv 3 (Résistance Tellurique) sont gérés ailleurs.
        if (lvlMiner >= 2 && random.nextDouble() < 0.05) { // 20% de chance
            for (int x = -2; x <= 2; x++) {
                for (int y = -2; y <= 2; y++) {
                    for (int z = -2; z <= 2; z++) {
                        Block nearby = block.getRelative(x, y, z);
                        Material type = nearby.getType();

                        // On ne casse que la stone et la deepslate, on laisse les minerais
                        if (type == Material.STONE || type == Material.DEEPSLATE) {
                            nearby.breakNaturally(player.getInventory().getItemInMainHand());
                        }
                    }
                }
            }
            player.sendMessage("§3Miner §7→ §aMini Vein Miner !");
        }
    }

    // --- BUCHERON ---
    public static void handleLumberjackReward(Player player, Block block, Jobs job) {
        int lvlLumberjack = JobsManager.getLevel(job, "lumberjack");

        // --- Nv 1: Sève Énergisante (Vitesse I) ---
        // Le niveau 1 est souvent un effet simple et garanti
        if (lvlLumberjack >= 1 && block.getType().toString().contains("LOG")) {
            if (!(random.nextDouble() < 0.35))return;
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 5, 0));
            player.sendMessage("§3lumberjack §7→ §aEnergising !");
        }

        // --- Nv 2: Morsure de la Hache (Réparation de la hache) ---
        if (lvlLumberjack >= 2 && block.getType().toString().contains("LOG") && random.nextDouble() < 0.2) {

            // La méthode breakNaturally() a déjà fait dropper le bloc principal.
            // On récupère le drop (bûche) pour le doubler.
            ItemStack logDrop = new ItemStack(block.getType(), 1);

            block.getWorld().dropItemNaturally(block.getLocation(), logDrop);
            player.sendMessage("§3lumberjack §7→ §aPowerful hit ! (Double log)");
        }

        // --- Nv 5: Abattage Massif (Récursion - Doit être placé en premier sur les logs!) ---
        if (lvlLumberjack >= 5 && block.getType().toString().contains("LOG")) {
            // Ajout d'une chance de déclenchement (ex: 5%)
            if (random.nextDouble() < 0.30) {

                // Appel de la méthode récursive dans JobsManager (voir section 2)
                JobsManager.breakConnectedLogs(block, player, 1000);

                player.sendMessage("§3lumberjack §7→ §aCut the tree !");

                // NOTE TRÈS IMPORTANTE : Si ceci est déclenché dans un BlockBreakEvent,
                // il FAUT annuler l'événement dans le Listener
                // pour éviter le double loot du premier bloc cassé.

                return; // Sortir après l'Abattage Massif
            }
        }

        // --- Nv 3: Main Verte (Feuilles) ---
        if (lvlLumberjack >= 3 && block.getType().toString().contains("LEAVES") && random.nextDouble() < 0.20) {
            Material saplingType = switch(block.getType().name()) {
                case "OAK_LEAVES" -> Material.OAK_SAPLING;
                case "SPRUCE_LEAVES" -> Material.SPRUCE_SAPLING;
                case "BIRCH_LEAVES" -> Material.BIRCH_SAPLING;
                case "JUNGLE_LEAVES" -> Material.JUNGLE_SAPLING;
                case "ACACIA_LEAVES" -> Material.ACACIA_SAPLING;
                case "DARK_OAK_LEAVES" -> Material.DARK_OAK_SAPLING;
                // Attention, MANGROVE_PROPAGULE n'existe que sur les versions très récentes (1.19+)
                case "MANGROVE_LEAVES" -> Material.MANGROVE_PROPAGULE;
                default -> null;
            };

            if (saplingType != null) {
                block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(saplingType));
                player.sendMessage("§3lumberjack §7→ §aGreen hand !");
            }
        }
    }


    // --- FARMER ---
    public static void handleFarmerReward(Player player, Block block, Jobs job) {
        int lvlFarmer = JobsManager.getLevel(job, "farmer");

        // Nv 1: Bonne Récolte I (5% de chance de rendement double)
        if (lvlFarmer >= 1 && JobsManager.isCrop(block.getType()) && random.nextDouble() < 0.05) {
            Collection<ItemStack> drops = block.getDrops();
            for (ItemStack drop : drops) {
                block.getWorld().dropItemNaturally(block.getLocation(), drop.clone());
            }
            player.sendMessage("§2Farmer §7→ §aGood harvest ! (Double Drop)");
        }

        // Nv 2: Semences Rapides (accélère la pousse des cultures voisines)
        if (lvlFarmer >= 2 && JobsManager.isCrop(block.getType()) && random.nextDouble() < 0.10) { // 10% chance
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        Block nearby = block.getRelative(x, y, z);
                        if (JobsManager.isCrop(nearby.getType())) {
                            // Simuler croissance instantanée (tu peux ajuster selon ton système)
                            nearby.setBlockData(nearby.getBlockData());
                        }
                    }
                }
            }
            player.sendMessage("§2Farmer §7→ §aFast grow !");
        }

        // Nv 3: Fertilisation Divine (5% de chance de drop une nourriture rare)
        if (lvlFarmer >= 3 && JobsManager.isCrop(block.getType()) && random.nextDouble() < 0.05) {
            ItemStack rareDrop = switch(block.getType()) {
                case WHEAT -> new ItemStack(Material.BREAD, 1);
                case CARROTS -> new ItemStack(Material.GOLDEN_CARROT, 1);
                case POTATOES -> new ItemStack(Material.BAKED_POTATO, 1);
                case BEETROOTS -> new ItemStack(Material.BEETROOT, 1);
                default -> null;
            };
            if (rareDrop != null) {
                block.getWorld().dropItemNaturally(block.getLocation(), rareDrop);
                player.getInventory().addItem(new ItemStack(Material.DIAMOND, 3));
                player.getInventory().addItem(new ItemStack(Material.EMERALD, 1));
                player.sendMessage("§2Farmer §7→ §aDivine fertilized ! (Rare drop)");
            }
        }

        // Nv 4: Moisson Sans Effort (1% de chance de régénérer un demi-cœur)

        if (lvlFarmer >= 4 && random.nextDouble() < 0.01) {
            player.setHealth(+2);
            player.sendMessage("§2Farmer §7→ §dEffortless");
        }

        // Nv 5: Vitalité Agricole (10% de chance de drop nourriture supplémentaire)
        if (lvlFarmer >= 5 && JobsManager.isCrop(block.getType()) && random.nextDouble() < 0.10) {
            Collection<ItemStack> drops = block.getDrops();
            for (ItemStack drop : drops) {
                block.getWorld().dropItemNaturally(block.getLocation(), drop.clone());
            }
            player.sendMessage("§2Farmer §7→ §aFarmer's life !");
        }
    }


    // --- HUNTER ---
    public static void handleHunterReward(Player player, Entity entity, Jobs job) {
        int lvlHunter = JobsManager.getLevel(job, "hunter");

        // Nv 1: Sens Aiguisé (Speed 0 pendant 5s)
        if (lvlHunter >= 1) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 5, 0));
        }

        // Nv 2: Butin Abondant (15% de chance de double drop sur les animaux)
        if (lvlHunter >= 2 && JobsManager.isAnimal(entity.getType()) && random.nextDouble() < 0.15) {
            // L'EntityDeathEvent Listener doit dupliquer le loot.
            player.sendMessage("§chunter §7→ §6Mega loot ! (Double Loot)");
        }

        // Nv 4: Visée Précise (5% de chance d’infliger un crit bonus)
        if (lvlHunter >= 4 && JobsManager.isAnimal(entity.getType()) && random.nextDouble() < 0.05) {
            double bonusDamage = 4.0; // 2 cœurs
            if (entity instanceof org.bukkit.entity.LivingEntity living) {
                living.damage(bonusDamage, player);
                player.sendMessage("§chunter §7→ §bCritical shot ! (+2 heart)");
            }
        }

        // Nv 5: Instinct de Survie (5% de chance de régénérer un cœur après kill)
        if (lvlHunter >= 5 && JobsManager.isAnimal(entity.getType())) {
            player.setHealth(+10);
            player.sendMessage("§chunter §7→ §dSurvivor !");
        }
    }


    // --- PECHEUR ---
    public static void handleFisherReward(Player player, PlayerFishEvent event, Jobs job) {
        int lvlFisher = JobsManager.getLevel(job, "fisher");

        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH && event.getCaught() instanceof org.bukkit.entity.Item caughtItem) {
            ItemStack fish = caughtItem.getItemStack();

            // Nv 2: Hameçon Robuste (10% de chance de drop 1 poisson supplémentaire)
            if (lvlFisher >= 2 && JobsManager.isFish(fish.getType()) && random.nextDouble() < 0.10) {
                caughtItem.getWorld().dropItemNaturally(caughtItem.getLocation(), fish.clone());
                player.sendMessage("§bFisher §7→ §aMore and more !");
            }

            // Nv 3: Aimant à Trésor (5% de chance de drop un objet rare)
            if (lvlFisher >= 3 && random.nextDouble() < 0.05) {
                ItemStack treasure = new ItemStack(Material.ENCHANTED_BOOK, 1); // exemple de trésor
                caughtItem.getWorld().dropItemNaturally(caughtItem.getLocation(), treasure);
                player.sendMessage("§bFisher §7→ §6Magnet ! (Rare drop)");
            }

            // Nv 5: Maître des Abysses (25% de chance de looter 10 poissons supplémentaires)
            if (lvlFisher >= 5 && JobsManager.isFish(fish.getType()) && random.nextDouble() < 0.25) {
                fish.setAmount(fish.getAmount() + 10);
                player.sendMessage("§bFisher §7→ §bMaster of the sea ! x10 fish");
            }
        }
    }


    // --- BUILDER ---
    public static void handleBuilderReward(Player player, Block block, Jobs job) {
        int lvlBuilder = JobsManager.getLevel(job, "builder");

        // Nv 1: Stabilité / Saut Amélioré (effet Jump Boost léger)
        if (lvlBuilder >= 1) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 20 * 5, 0));
            player.sendMessage("§9Builder §7→ §aResistance boost !");
        }

        // Nv 2: Récupération (10% de chance de drop le bloc placé)
        if (lvlBuilder >= 2 && random.nextDouble() < 0.10) {
            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(block.getType(), 1));
            player.sendMessage("§9Builder §7→ §aGet back !");
        }

        // Nv 4: Outils Légers (optionnel: bonus de vitesse ou chance de drop supplémentaire)
        if (lvlBuilder >= 4 && random.nextDouble() < 0.05) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 20, 5));
            player.sendMessage("§9Builder §7→ §aFast Builder !");
            // Ici tu pourrais appliquer un effet de minage léger si tu veux
        }

        // Nv 5: Main du Maître (3% de chance de ne pas consommer le bloc)
        if (lvlBuilder >= 5 && random.nextDouble() < 0.03) {
            player.sendMessage("§9Builder §7→ §eMaster builder !");
            // Le vrai annulation de consommation doit être traité dans le BlockPlaceEvent Listener
        }
    }

}