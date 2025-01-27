package org.katok.adminAssist.Commands;

import net.kyori.adventure.text.event.ClickEvent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.katok.adminAssist.Main.*;
import static org.katok.adminAssist.utils.ConfigUtil.*;
import static org.katok.adminAssist.utils.HexUtil.color;

public class re_utils implements CommandExecutor {
    public static final NamespacedKey re_mode = new NamespacedKey(instance, "re_mode");
    public static final HashMap<String, ItemStack[]> inventories = new HashMap<>();
    public static final HashMap<String, Location> locations = new HashMap<>();
    public static final HashMap<String, GameMode> gamemodes = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(getString("console.cannotFromConsole", messages_cfg));
            return true;
        }

        if(strings.length == 0) {
            sender.sendMessage(getString("technicalPeculiarities.needMoreArguments", messages_cfg));
            return true;
        }

        Player spec_player = instance.getServer().getPlayer(strings[0]);

        if(spec_player == null) {
            sender.sendMessage(getString("technicalPeculiarities.specifyToPlayer", messages_cfg));
            return true;
        }

        String player_from_data = player.getPersistentDataContainer().get(re_mode, PersistentDataType.STRING);

        if(!(StringUtils.isEmpty(player_from_data) || player_from_data.equals(strings[0]) || strings.length >= 2 && strings[1].equals("confirm"))) {
            player.sendMessage(MessageFormat.format(getString("re.youStillSpectate", messages_cfg), player.getPersistentDataContainer().get(re_mode, PersistentDataType.STRING), strings[0]));
            player.sendMessage(getString_component("other.forConfirm", messages_cfg).append(getString_component("other.poke", messages_cfg).clickEvent(ClickEvent.runCommand("/re " + strings[0] + " confirm"))));
            return true;
        }
        player.getPersistentDataContainer().set(re_mode, PersistentDataType.STRING, strings[0]);

        if(!inventories.containsKey(player.getName())) {
            inventories.put(player.getName(), player.getInventory().getContents());
        }

        if(!locations.containsKey(player.getName())) {
            locations.put(player.getName(), player.getLocation());
        }

        if(!gamemodes.containsKey(player.getName())) {
            gamemodes.put(player.getName(), player.getGameMode());
        }

        player.setGameMode(GameMode.valueOf(config.getString("re.gamemode")));
        player.getInventory().setContents(getReInventory(spec_player));
        player.teleport(spec_player);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100000,1, false, false));

        return true;
    }

    private static ItemStack[] getReInventory(Player p) {
        ItemStack[] inventory = new ItemStack[41];
        Arrays.fill(inventory, new ItemStack(Material.AIR));

        int slot_shift = 1;

        ItemStack compass = new ItemStack(Material.COMPASS);
        CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();
        compassMeta.setLodestone(p.getLocation());
        compassMeta.setLodestoneTracked(false);
        compassMeta.getPersistentDataContainer().set(item_command, PersistentDataType.STRING, "/tp " + p.getName());
        compassMeta.setDisplayName(MessageFormat.format(getString("re.items.compass.name"), p.getName()));
        compass.setItemMeta(compassMeta);
        inventory[getInt("re.items.compass.slot") - slot_shift] = compass;

        ItemStack recoil_stick = new ItemStack(Material.STICK);
        ItemMeta recoil_stick_meta = recoil_stick.getItemMeta();
        recoil_stick_meta.displayName(getString_component("re.items.recoil_stick.name"));
        recoil_stick_meta.addEnchant(Enchantment.KNOCKBACK, 10, true);
        recoil_stick.setItemMeta(recoil_stick_meta);
        inventory[getInt("re.items.recoil_stick.slot") - slot_shift] = recoil_stick;

        if(getBoolean("optimization.throughwalls")) {
            ItemStack through_walls = new ItemStack(Material.getMaterial(getString("re.items.through_walls.material")));
            ItemMeta through_walls_meta = through_walls.getItemMeta();
            through_walls_meta.displayName(getString_component("re.items.through_walls.name"));
            through_walls_meta.getPersistentDataContainer().set(item_command, PersistentDataType.STRING, "/throughwalls");
            through_walls.setItemMeta(through_walls_meta);
            inventory[getInt("re.items.through_walls.slot") - slot_shift] = through_walls;
        } else {
            slot_shift -= 1;
        }

        ItemStack checkcheater = new ItemStack(Material.getMaterial(getString("re.items.checkcheater.material")));
        ItemMeta checkcheater_meta = checkcheater.getItemMeta();
        checkcheater_meta.displayName(getString_component("re.items.checkcheater.name"));
        checkcheater_meta.getPersistentDataContainer().set(item_command, PersistentDataType.STRING, "/checkcheater " + p.getName());
        checkcheater.setItemMeta(checkcheater_meta);
        inventory[getInt("re.items.checkcheater.slot") - slot_shift] = checkcheater;

        ItemStack quit = new ItemStack(Material.getMaterial(getString("re.items.quit.material")));
        ItemMeta quit_meta = quit.getItemMeta();
        quit_meta.displayName(getString_component("re.items.quit.name"));
        quit_meta.getPersistentDataContainer().set(item_command, PersistentDataType.STRING, "/unre");
        quit.setItemMeta(quit_meta);
        inventory[getInt("re.items.quit.slot") - 1] = quit;



        List<String> ignore_words = new ArrayList<String>(List.of("material", "color_of_reason", "command", "prefix"));

        for(String label: config.getConfigurationSection("re.bans").getKeys(false)) {
            boolean must_break = false;
            for(String ignore_word: ignore_words) {
                if(label.equals(ignore_word)) {
                    must_break = true;
                    break;
                }
            }
            if(must_break) continue;

            ItemStack itemStack = new ItemStack(Material.valueOf(getString("re.bans.material")));
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(color(MessageFormat.format(config.getString("re.bans.prefix"), config.getString("re.bans." + label + ".reason"))));
            itemMeta.getPersistentDataContainer().set(item_command, PersistentDataType.STRING, MessageFormat.format(config.getString("re.bans.command"), p.getName(), config.getString("re.bans." + label + ".time"), config.getString("re.bans." + label + ".reason")));
            itemStack.setItemMeta(itemMeta);

            for(int slot = 0; slot < 41; slot++) {
                if(inventory[slot].getType().equals(Material.AIR)) {
                    inventory[slot] = itemStack;
                    break;
                }
            }
        }

        return inventory;
    }
}
