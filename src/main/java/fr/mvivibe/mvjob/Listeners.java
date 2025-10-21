package fr.mvivibe.mvjob;

import com.sun.tools.javac.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Listeners implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§6Job");

        List<String> lore = new ArrayList<>();
        lore.add("§bCliquez dessus");
        lore.add("§bPour ouvrir le menu de progression");
        lore.add("§0§kID_JOB_ITEM");
        meta.setLore(lore);

// Marque spéciale pour la reconnaître plus tard
        item.setItemMeta(meta);
        player.getInventory().setItem(8, item);


        // Vérifie si le joueur a déjà un job enregistré
        boolean joueurExiste = CommandManager.Data.jobsList.stream()
                .anyMatch(job -> job.uuid.equals(uuid));

        if (!joueurExiste) {
            // C'est la première connexion : on crée un job par défaut pour ce joueur
            Jobs nouveauJob = new Jobs();
            nouveauJob.uuid = player.getUniqueId();
            nouveauJob.xp_miner = 0;
            nouveauJob.lvl_miner = 0;
            nouveauJob.xp_lumberjack = 0;
            nouveauJob.lvl_lumberjack = 0;
            nouveauJob.xp_hunter = 0;
            nouveauJob.lvl_hunter = 0;
            nouveauJob.xp_farmer = 0;
            nouveauJob.lvl_farmer = 0;
            nouveauJob.xp_fisher = 0;
            nouveauJob.lvl_fisher = 0;
            nouveauJob.xp_builder = 0;
            nouveauJob.lvl_builder = 0;
            CommandManager.Data.jobsList.add(nouveauJob);
            Mvjob.savedata();
        }
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        onInventory(event);
        if (event.getView().getTitle().equals("§6Jobs") || event.getView().getTitle().equals("§6Jobs progress") ) {
            event.setCancelled(true); // empêche de déplacer les items
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        JobsManager.handleBlockBreak(event.getPlayer(), event.getBlock());
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (!(event.getEntity().getKiller() instanceof Player)) return;
        JobsManager.handleEntityKill((Player) event.getEntity().getKiller(), event.getEntity());
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        JobsManager.handleFish(event.getPlayer(), event);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        JobsManager.handleBlockPlace(event.getPlayer(), event.getBlockPlaced());
    }

    @EventHandler
    public void onInventory(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();

        ItemStack item = event.getCurrentItem();
        if (isJobItem(item)) {
            event.setCancelled(true);
            player.closeInventory();
            JobGui2.openGui(player);
        }
    }

    // --- Clic dans le vide / dans le monde ---
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (isJobItem(item)) {
            event.setCancelled(true);
            JobGui2.openGui(player);
        }
    }

    private boolean isJobItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta.getLore() == null) return false;
        return meta.getLore().contains("§0§kID_JOB_ITEM");
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        if (isJobItem(item)) {
            event.setCancelled(true);
        }
    }

}
