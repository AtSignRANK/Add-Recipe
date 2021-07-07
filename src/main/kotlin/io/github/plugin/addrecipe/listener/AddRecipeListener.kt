package io.github.plugin.addrecipe.listener

import io.github.plugin.addrecipe.AddRecipe
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ShapedRecipe

class AddRecipeListener(private val plugin: AddRecipe): Listener {
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked
        val inventory = event.inventory
        if (event.view.title() == plugin.addrecipe) {
            val number = plugin.serverconfig.getInt("number")
            if (event.slot == 26) {
                val recipenum = plugin.serverconfig.getInt("number")
                plugin.serverconfig.set("number", recipenum + 1)

                if (inventory.getItem(15) == null || inventory.getItem(15)?.type == Material.AIR) {
                    player.sendMessage("${ChatColor.RED}You can't create AIR!")
                    return
                }

                for (i in plugin.addrecipeclickable) {
                    if (i == 15) {
                        player.sendMessage("You can't create ${inventory.getItem(15)?.displayName()} with AIRs!")
                    } else if (inventory.getItem(i) != null || inventory.getItem(i)?.type != Material.AIR) {
                        break
                    }
                }

                val shapedrecipe = ShapedRecipe(NamespacedKey(plugin, NamespacedKey.MINECRAFT),
                    inventory.getItem(plugin.addrecipeclickable[9])!!)
                shapedrecipe.shape("abc","def","ghi").setIngredient('a',
                    inventory.getItem(plugin.addrecipeclickable[0])!!)
                    .setIngredient('b', inventory.getItem(plugin.addrecipeclickable[1])!!)
                    .setIngredient('c', inventory.getItem(plugin.addrecipeclickable[2])!!)
                    .setIngredient('d', inventory.getItem(plugin.addrecipeclickable[3])!!)
                    .setIngredient('e', inventory.getItem(plugin.addrecipeclickable[4])!!)
                    .setIngredient('f', inventory.getItem(plugin.addrecipeclickable[5])!!)
                    .setIngredient('g', inventory.getItem(plugin.addrecipeclickable[6])!!)
                    .setIngredient('h', inventory.getItem(plugin.addrecipeclickable[7])!!)
                    .setIngredient('i', inventory.getItem(plugin.addrecipeclickable[8])!!)
                Bukkit.addRecipe(shapedrecipe)
                plugin.serverconfig.set("$number.type", "ItemStack")
                plugin.serverconfig.set("$number.item", inventory.getItem(plugin.addrecipeclickable[9])!!)
                for (i in 0..8) {
                    plugin.serverconfig.set("$number.$i", inventory.getItem(plugin.addrecipeclickable[i])!!)
                }
            } else if (event.slot == 8) {
                val recipenum = plugin.serverconfig.getInt("number")
                plugin.serverconfig.set("number", recipenum + 1)

                if (inventory.getItem(15) == null || inventory.getItem(15)?.type == Material.AIR) {
                    player.sendMessage("${ChatColor.RED}You can't create AIR!")
                    return
                }

                for (i in plugin.addrecipeclickable) {
                    if (i == 15) {
                        player.sendMessage("You can't create ${inventory.getItem(15)?.displayName()} with AIRs!")
                    } else if (inventory.getItem(i) != null || inventory.getItem(i)?.type != Material.AIR) {
                        break
                    }
                }

                val shapedrecipe = ShapedRecipe(NamespacedKey(plugin, NamespacedKey.MINECRAFT),
                    inventory.getItem(plugin.addrecipeclickable[9])!!)
                shapedrecipe.shape("abc","def","ghi").setIngredient('a', inventory.getItem(plugin.addrecipeclickable[0])!!.type)
                    .setIngredient('b', inventory.getItem(plugin.addrecipeclickable[1])!!.type)
                    .setIngredient('c', inventory.getItem(plugin.addrecipeclickable[2])!!.type)
                    .setIngredient('d', inventory.getItem(plugin.addrecipeclickable[3])!!.type)
                    .setIngredient('e', inventory.getItem(plugin.addrecipeclickable[4])!!.type)
                    .setIngredient('f', inventory.getItem(plugin.addrecipeclickable[5])!!.type)
                    .setIngredient('g', inventory.getItem(plugin.addrecipeclickable[6])!!.type)
                    .setIngredient('h', inventory.getItem(plugin.addrecipeclickable[7])!!.type)
                    .setIngredient('i', inventory.getItem(plugin.addrecipeclickable[8])!!.type)
                Bukkit.addRecipe(shapedrecipe)
                plugin.serverconfig.set("$number.type", "Material")
                plugin.serverconfig.set("$number.item", inventory.getItem(plugin.addrecipeclickable[9])!!)
                for (i in 0..8) {
                    plugin.serverconfig.set("$number.$i", inventory.getItem(plugin.addrecipeclickable[i])!!.type)
                }
            } else if (!plugin.addrecipeclickable.contains(event.slot)) {
                event.isCancelled = true
                return
            }
        }
    }
}
