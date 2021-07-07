package io.github.plugin.addrecipe.listener

import io.github.plugin.addrecipe.AddRecipe
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class RecipeListListener(private val plugin: AddRecipe): Listener {
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked
        val inventory = event.inventory
        if (event.view.title() == plugin.recipelist) {
            if (plugin.recipelistclicknotable.contains(event.slot)) {
                if (event.slot == 45) {
                    val page = plugin.getIntInString(inventory.getItem(47)?.displayName()?.insertion()!!,
                        "${ChatColor.GREEN}현재 페이지 : ", 1, 10)
                    val number = (page - 1) * 45 + event.slot
                    val newInventory = plugin.getRecipe(number)
                    player.openInventory(newInventory)
                }
            }
        }
    }
}
