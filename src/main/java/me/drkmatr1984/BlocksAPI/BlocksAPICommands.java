package me.drkmatr1984.BlocksAPI;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.material.PistonExtensionMaterial;

import me.drkmatr1984.BlocksAPI.tasks.DelayedRegenTask;
import me.drkmatr1984.BlocksAPI.utils.SBlock;
import me.drkmatr1984.BlocksAPI.utils.Utils;

@SuppressWarnings({ "unchecked" })
public class BlocksAPICommands implements org.bukkit.command.CommandExecutor
{
	private BlocksAPI plugin;	
	
	public BlocksAPICommands(BlocksAPI plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		try {
		    if (cmd.getName().equalsIgnoreCase("rollback")) {
		    	ArrayList<SBlock> blocks = (ArrayList<SBlock>) Utils.setToList(plugin.getBlocksBroken());
		    	for(SBlock sb : blocks){
		    		Location l = new Location(Bukkit.getServer().getWorld(sb.world),sb.x,sb.y,sb.z);
		    		Block bl = l.getBlock();
		    		BlockState blockState = bl.getState();
		    		Material mat = Material.valueOf(sb.mat);
		    		bl.setTypeIdAndData(mat.getId(), sb.data, true);
		    		if(Utils.isOtherAttachable(mat) || mat.equals(Material.CACTUS) || mat.equals(Material.SUGAR_CANE_BLOCK) || blockState.getData() instanceof PistonExtensionMaterial){
		    			plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new DelayedRegenTask(sb) {
		    			}, 2L);
		    		}else{
		    			plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new DelayedRegenTask(sb) {
		    			}, 1L);
		    		}		    		
		    		if(!plugin.removeFromList(sb) && plugin.debugMessages){
		    			Bukkit.getServer().getLogger().info(ChatColor.DARK_RED + "Cannot remove from List");
		    		}
		    	}		    		
		    }
		}catch (Exception e) {
			e.printStackTrace();
			sender.sendMessage(ChatColor.DARK_RED + "An Error has Occured");
	    }
		return false;
	}
}