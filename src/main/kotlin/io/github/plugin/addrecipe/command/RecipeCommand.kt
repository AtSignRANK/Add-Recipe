package io.github.plugin.addrecipe.command

import io.github.plugin.addrecipe.AddRecipe
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class RecipeCommand(private val plugin: AddRecipe): CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return false
        when(command.name) {
            "addrecipe" -> {
                val inventory = Bukkit.createInventory(null, 3 * 9, plugin.addrecipe)
                val barrier = ItemStack(Material.BARRIER)
                var meta = barrier.itemMeta
                meta.displayName(Component.text(" "))
                barrier.itemMeta = meta
                for (i in 0..26) {
                    if (!plugin.addrecipeclickable.contains(i)) inventory.setItem(i, barrier)
                }
                var complete = ItemStack(Material.EMERALD_BLOCK)
                meta = complete.itemMeta
                meta.displayName(Component.text("${ChatColor.GREEN}Add Item Stack Recipe"))
                complete.itemMeta = meta
                inventory.setItem(26, complete)
                complete = ItemStack(Material.DIAMOND_BLOCK)
                meta = complete.itemMeta
                meta.displayName(Component.text("${ChatColor.GREEN}Add Material Recipe"))
                complete.itemMeta = meta
                inventory.setItem(8, complete)
                sender.openInventory(inventory)
            }

            "recipelist" -> {
                sender.openInventory(plugin.recipeList(1))
            }
        }
        return false
    }
}
