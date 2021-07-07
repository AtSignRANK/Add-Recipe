package io.github.plugin.addrecipe

import io.github.plugin.addrecipe.command.RecipeCommand
import io.github.plugin.addrecipe.listener.AddRecipeListener
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException

class AddRecipe: JavaPlugin() {
    val addrecipe = Component.text("${ChatColor.YELLOW}AddRecipe")
    val recipelist = Component.text("${ChatColor.YELLOW}RecipeList")

    val addrecipeclickable = listOf(2, 3, 4, 11, 12, 13, 20, 21, 22, 15)

    val recipelistclicknotable = listOf(45, 46, 47, 48, 49, 50, 51, 52, 53)

    var serverfile = File("plugins/AddRecipe/recipe.yml")
    var serverconfig = YamlConfiguration.loadConfiguration(serverfile)

    fun loadConfig() {
        try {
            if (!serverfile.exists()) {
                (serverconfig).save(serverfile)
            }
            (serverconfig).load(serverfile)
        } catch (localException: Exception) {
            localException.printStackTrace()
        }
    }

    override fun saveConfig() {
        try {
            serverconfig.save(serverfile)
        } catch (E: IOException) {
            E.printStackTrace()
        }
    }

    override fun onEnable() {
        server.pluginManager.registerEvents(AddRecipeListener(this), this)
        server.getPluginCommand("addrecipe")?.setExecutor(RecipeCommand(this))
        server.getPluginCommand("recipelist")?.setExecutor(RecipeCommand(this))
        loadConfig()
        if (!serverconfig.isSet("number")) {
            serverconfig.set("number", 0)
        }
        for (i in 0..(number - 1))
    }

    fun getIntInString(string: String, front: String, min: Int, max: Int): Int {
        for (i in min..max) {
            if (string == "$front$i") {
                return i
            }
        }
        return 0
    }

    fun getRecipe(number: Int): Inventory {
        val newInventory = Bukkit.createInventory(null, 3 * 9, Component.text("${ChatColor.YELLOW}Recipe"))
        val barrier = ItemStack(Material.BARRIER)
        var meta = barrier.itemMeta
        meta.displayName(Component.text(" "))
        barrier.itemMeta = meta
        for (i in 0..26) {
            if (!addrecipeclickable.contains(i)) newInventory.setItem(i, barrier)
        }
        if (serverconfig.getString("$number.type") == "ItemStack") {
            for (i in 1 until (addrecipeclickable.size)) {
                newInventory.setItem(addrecipeclickable[i], serverconfig.getItemStack("$number.$i"))
            }
            newInventory.setItem(15, serverconfig.getItemStack("$number.item"))
        } else {
            for (i in 1 until (addrecipeclickable.size)) {
                newInventory.setItem(addrecipeclickable[i], ItemStack(serverconfig.get("$number.$i") as Material))
            }
            newInventory.setItem(15, serverconfig.getItemStack("$number.item"))
        }
        return newInventory
    }

    fun recipe_delete_restore(number: Int, bool: Boolean) {
        serverconfig.set("${number}.delete", bool)
    }
}
