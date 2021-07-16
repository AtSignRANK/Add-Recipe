package io.github.plugin.addrecipe.listener

import io.github.plugin.addrecipe.AddRecipe
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack
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
                    event.isCancelled = true
                    return
                }

                for (i in plugin.addrecipeclickable) {
                    if (i == 15) {
                        player.sendMessage("${ChatColor.RED}You can't create ${inventory.getItem(15)?.itemMeta?.displayName()?.insertion()} with AIRs!")
                        event.isCancelled = true
                        return
                    } else if (inventory.getItem(i) != null && inventory.getItem(i)?.type != Material.AIR) {
                        break
                    }
                }

                val recipe = mutableListOf<ItemStack>()
                for (i in 0..8) {
                    var itemstack = inventory.getItem(plugin.addrecipeclickable[i])
                    if (itemstack == null) {
                        itemstack = ItemStack(Material.AIR)
                    }
                    recipe.add(itemstack)
                }

                val shapedrecipe = ShapedRecipe(NamespacedKey(plugin, NamespacedKey.MINECRAFT),
                    inventory.getItem(plugin.addrecipeclickable[9])!!)
                shapedrecipe.shape("abc","def","ghi").setIngredient('a', recipe[0])
                    .setIngredient('b', recipe[1]).setIngredient('c', recipe[2])
                    .setIngredient('d', recipe[3]).setIngredient('e', recipe[4])
                    .setIngredient('f', recipe[5]).setIngredient('g', recipe[6])
                    .setIngredient('h', recipe[7]).setIngredient('i', recipe[8])
                Bukkit.addRecipe(shapedrecipe)
                plugin.serverconfig.set("$number.type", "ItemStack")
                plugin.serverconfig.set("$number.item", inventory.getItem(plugin.addrecipeclickable[9])!!)
                for (i in 0..8) {
                    plugin.serverconfig.set("$number.$i", recipe[i])
                }
            } else if (event.slot == 8) {
                val recipenum = plugin.serverconfig.getInt("number")
                plugin.serverconfig.set("number", recipenum + 1)

                if (inventory.getItem(15) == null || inventory.getItem(15)?.type == Material.AIR) {
                    player.sendMessage("${ChatColor.RED}You can't create AIR!")
                    event.isCancelled = true
                    return
                }

                for (i in plugin.addrecipeclickable) {
                    if (i == 15) {
                        player.sendMessage("${ChatColor.RED}You can't create ${inventory.getItem(15)?.itemMeta?.displayName()?.insertion()} with AIRs!")
                        event.isCancelled = true
                        return
                    } else if (inventory.getItem(i) != null && inventory.getItem(i)?.type != Material.AIR) {
                        break
                    }
                }
                val recipe = mutableListOf<Material>()
                for (i in 0..8) {
                    var material = inventory.getItem(plugin.addrecipeclickable[i])?.type
                    if (material == null) {
                        material = Material.AIR
                    }
                    recipe.add(material)
                }

                val shapedrecipe = ShapedRecipe(NamespacedKey(plugin, NamespacedKey.MINECRAFT),
                    inventory.getItem(plugin.addrecipeclickable[9])!!)
                shapedrecipe.shape("abc","def","ghi").setIngredient('a', recipe[0])
                    .setIngredient('b', recipe[1]).setIngredient('c', recipe[2])
                    .setIngredient('d', recipe[3]).setIngredient('e', recipe[4])
                    .setIngredient('f', recipe[5]).setIngredient('g', recipe[6])
                    .setIngredient('h', recipe[7]).setIngredient('i', recipe[8])
                Bukkit.addRecipe(shapedrecipe)
                plugin.serverconfig.set("$number.type", "Material")
                plugin.serverconfig.set("$number.item", inventory.getItem(plugin.addrecipeclickable[9])!!)
                for (i in 0..8) {
                    plugin.serverconfig.set("$number.$i", recipe[i])
                }
            }
            if (event.clickedInventory != player.inventory && !plugin.addrecipeclickable.contains(event.slot)) {
                event.isCancelled = true
                return
            }
        }
    }
}
