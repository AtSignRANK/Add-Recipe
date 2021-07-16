package io.github.plugin.addrecipe

import io.github.plugin.addrecipe.command.RecipeCommand
import io.github.plugin.addrecipe.listener.AddRecipeListener
import io.github.plugin.addrecipe.listener.RecipeListListener

import net.kyori.adventure.text.Component

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
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
        server.pluginManager.registerEvents(RecipeListListener(this), this)
        server.getPluginCommand("addrecipe")?.setExecutor(RecipeCommand(this))
        server.getPluginCommand("recipelist")?.setExecutor(RecipeCommand(this))
        loadConfig()
        if (!serverconfig.isSet("number")) {
            serverconfig.set("number", 0)
        }
        val number = serverconfig.getInt("number")
        for (i in 0 until number) {
            if (serverconfig.get("$i.type") == "ItemStack") {
                val shapedrecipe = ShapedRecipe(
                    NamespacedKey(this, NamespacedKey.MINECRAFT), serverconfig.getItemStack("$i.item")!!)
                shapedrecipe.shape("abc","def","ghi").setIngredient('a', serverconfig.getItemStack("$i.0")!!)
                    .setIngredient('b', serverconfig.getItemStack("$i.1")!!)
                    .setIngredient('c', serverconfig.getItemStack("$i.2")!!)
                    .setIngredient('d', serverconfig.getItemStack("$i.3")!!)
                    .setIngredient('e', serverconfig.getItemStack("$i.4")!!)
                    .setIngredient('f', serverconfig.getItemStack("$i.5")!!)
                    .setIngredient('g', serverconfig.getItemStack("$i.6")!!)
                    .setIngredient('h', serverconfig.getItemStack("$i.7")!!)
                    .setIngredient('i', serverconfig.getItemStack("$i.8")!!)
                Bukkit.addRecipe(shapedrecipe)
            } else {
                val shapedrecipe = ShapedRecipe(
                    NamespacedKey(this, NamespacedKey.MINECRAFT), serverconfig.getItemStack("$i.item")!!)
                shapedrecipe.shape("abc","def","ghi").setIngredient('a', serverconfig.get("$i.0")!! as Material)
                    .setIngredient('b', serverconfig.get("$i.1")!! as Material)
                    .setIngredient('c', serverconfig.get("$i.2")!! as Material)
                    .setIngredient('d', serverconfig.get("$i.3")!! as Material)
                    .setIngredient('e', serverconfig.get("$i.4")!! as Material)
                    .setIngredient('f', serverconfig.get("$i.5")!! as Material)
                    .setIngredient('g', serverconfig.get("$i.6")!! as Material)
                    .setIngredient('h', serverconfig.get("$i.7")!! as Material)
                    .setIngredient('i', serverconfig.get("$i.8")!! as Material)
                Bukkit.addRecipe(shapedrecipe)
            }
        }
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

    fun recipeList(page: Int): Inventory {
        val inventory = Bukkit.createInventory(null, 6 * 9, recipelist)
        val start = (page - 1) * 5 * 9

        val barrier = ItemStack(Material.BARRIER)
        var meta = barrier.itemMeta
        meta.displayName(Component.text(" "))
        barrier.itemMeta = meta

        for (i in 46..52) {
            inventory.setItem(i, barrier)
        }

        val arrow = ItemStack(Material.ARROW)
        val arrowmeta = arrow.itemMeta
        arrowmeta.displayName(Component.text("${ChatColor.GREEN}이전으로"))
        arrow.itemMeta = arrowmeta

        inventory.setItem(45, arrow)

        arrowmeta.displayName(Component.text("${ChatColor.GREEN}다음으로"))
        arrow.itemMeta = arrowmeta

        inventory.setItem(53, arrow)

        val book = ItemStack(Material.BOOK)
        val bookmeta = book.itemMeta
        bookmeta.displayName(Component.text("${ChatColor.GREEN}현재 페이지 : $page"))
        book.itemMeta = bookmeta

        inventory.setItem(49, book)

        for (i in 0..44) {
            if(!serverconfig.isSet("${start + i}.type")) {
                return inventory
            } else if (serverconfig.getString("${start + i}.type") == "ItemStack") {
                inventory.setItem(i, serverconfig.getItemStack("${start + i}.item"))
            } else if (serverconfig.getString("${start + i}.type") == "Material") {
                inventory.setItem(i, ItemStack(serverconfig.get("${start + i}.item") as Material))
            }
        }
        return inventory
    }
}
