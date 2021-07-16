package io.github.plugin.addrecipe.listener

import io.github.plugin.addrecipe.AddRecipe
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class RecipeListListener(private val plugin: AddRecipe): Listener {
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val player = event.whoClicked
        val inventory = event.inventory
        if (event.view.title() == plugin.recipelist && event.clickedInventory != player.inventory) {
            event.isCancelled = true
            if (plugin.recipelistclicknotable.contains(event.slot)) {
                val page = plugin.getIntInString(inventory.getItem(47)?.displayName()?.insertion()!!,
                    "${ChatColor.GREEN}현재 페이지 : ", 1, 10)
                if (event.slot < 45) {
                    val number = (page - 1) * 45 + event.slot
                    val newInventory = plugin.getRecipe(number)
                    player.openInventory(newInventory)
                } else if (event.slot == 45) {
                    if (page == 1) {
                        player.sendMessage(Component.text("${ChatColor.RED}이전 페이지가 존재하지 않습니다!"))
                        return
                    }
                    player.openInventory(plugin.recipeList(page - 1))
                } else if (event.slot == 53) {
                    if (page == 10) {
                        player.sendMessage(Component.text("${ChatColor.RED}다음 페이지가 존재하지 않습니다!"))
                        return
                    }
                    player.openInventory(plugin.recipeList(page + 1))
                }
            }
        }
    }
}
