package com.greazi.addonbase;

import me.TechsCode.UltraRegions.UltraRegions;
import me.TechsCode.UltraRegions.base.item.XMaterial;
import me.TechsCode.UltraRegions.flags.Flag;
import me.TechsCode.UltraRegions.flags.calculator.Result;
import me.TechsCode.UltraRegions.storage.FlagValue;
import me.TechsCode.UltraRegions.storage.ManagedWorld;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Optional;

public class DenySleep extends Flag {

	/**
	 * This is the main name of your flag all in caps
	 * Spaces are allowed but needed to be given with a; _
	 *
	 *
	 * @param plugin This will be registered inside Ultra Regions
	 */
	public DenySleep(UltraRegions plugin) {
		super(plugin, "SLEEP_DENY");
	}

	/**
	 * The name of the flag it self
	 * The value can only be a string
	 *
	 * @return The name of the flag
	 */
	public String getName() {
		return "Sleep Deny";
	}

	/**
	 * This will hold the description of the flag you are creating
	 * The value can only be a string
	 *
	 * @return The description of the flag
	 */
	public String getDescription() {
		return "If disallowed, players won't be able to sleep in this region.";
	}

	/**
	 * The input for the icon that will shown in the flags list
	 * Possible icon's can be found here: https://helpch.at/docs/1.7.10/org/bukkit/Material.html
	 * !!-WARNING-!! ONLY USE ICON'S THAT ARE AVAILABLE ON 1.8 (if wanted you can add a check for the server version)
	 *
	 * @return The material of the icon that is shown in the flags list
	 */
	public XMaterial getIcon() {
		return XMaterial.WHITE_BED;
	}

	/**
	 * What is the default falgs value?
	 * This means when you did not add the flag it will be allowed or not.
	 * !!-WARNING-!! WHEN SET TO "DISALLOW" THINGS CAN BREAK!
	 *
	 * @return The default flag value
	 * 	Possible values are:
	 * 	 FlagValue.ALLOW or FlagValue.ALLOW
	 */
	public FlagValue getDefaultValue() {
		return FlagValue.ALLOW;
	}

	/** Is this flag connected to players?
	 * Possible values are:
	 * 		true / false
	 * @return If the flag is player specific
	 */
	public boolean isPlayerSpecificFlag() {
		return true;
	}

	/**
	 * Get the player enter event of entering the bed
	 * because of this you are able to get the information about that specific player
	 *
	 * @param e Will return the player Bed Enter Event
	 */
	@EventHandler
	public void onInteract(PlayerBedEnterEvent e) {
		// If the person who trigers the event == a player
		if (e.getPlayer() instanceof Player) {
			// e.getPlayer will be called as player from now on
			Player player = e.getPlayer();
			// If the world == the world where the flag is added (the player is in that region)
			Optional<ManagedWorld> optional = this.plugin.getWorlds().find(player.getWorld());
			if (optional.isPresent()) {
				// e.getBed will return the location of the bed and will now be called with block
				Block block = e.getBed();
				// If block is not nothing
				if (block != null) {
					// A check if the location of the bed is at the player
					Result result = calculate(block.getLocation(), player);
					// If the result is true and the flag is set to disallow
					if (result != null && result.isSetToDisallowed())
						// Cancel the enter bed event.
						e.setCancelled(true);
				}
			}
		}
	}
}
