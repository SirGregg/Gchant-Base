package me.sirgregg.gchantbase.command;

import me.sirgregg.gchantbase.GchantBase;
import me.sirgregg.gchantbase.enchantsys.BaseEnchant;
import me.sirgregg.gchantbase.util.file.LangFileUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static me.sirgregg.gchantbase.util.StringUtil.format;

public class GchantCommand implements CommandExecutor {
	private LangFileUtil lang = LangFileUtil.getLang();
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(format(lang.getString("player-only")));
			return false;
		}
		Player player = (Player) sender;

		if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
			if (!player.hasPermission("gchant.command.help")) {
				player.sendMessage(format(lang.getString("no-permission")));
				return false;
			}

			for (String string : lang.getStringList("gchant-command.help")) {
				player.sendMessage(format(string));
			}
		} else if (args[0].equalsIgnoreCase("enchant")) {
			if (!player.hasPermission("gchant.command.enchant")) {
				player.sendMessage(format(lang.getString("no-permission")));
				return false;
			}

			/*
			0 -> enchant
			1 -> enchant name
			2 -> level
			 */
			if (args.length < 3) {
				player.sendMessage(format(lang.getString("gchant-command.enchant.incorrect-args")));
				return false;
			}

			if (player.getItemInHand() == null || player.getItemInHand().getType().equals(Material.AIR)) {
				player.sendMessage(format(lang.getString("gchant-command.enchant.nothing-in-hand")));
				return false;
			}

			BaseEnchant enchant = GchantBase.getEnchantManager().getEnchant(args[1]);

			if (enchant == null) {
				player.sendMessage(format(lang.getString("gchant-command.enchant.invalid-enchant")));
				return false;
			}

			int level;

			try {
				level = Integer.parseInt(args[2]);
			} catch (NumberFormatException e) {
				player.sendMessage(format(lang.getString("gchant-command.enchant.incorrect-args")));
				return false;
			}

			ItemStack item = player.getItemInHand();
			ItemMeta meta = item.getItemMeta();

			List<String> lore = meta.getLore();
			lore.add(0, format("&7" + enchant.getName() + " " + GchantBase.getRomanNumberalUtil().encode(level))); // TODO: Add it to the bottom of the enchant list (to avoid weird formatting)

			meta.setLore(lore);
			item.setItemMeta(meta);
		} else {
			if (!player.hasPermission("gchant.command.help")) {
				player.sendMessage(format(lang.getString("no-permission")));
				return false;
			}

			for (String string : lang.getStringList("gchant-command.help")) {
				player.sendMessage(format(string));
			}
		}
		return false;
	}
}