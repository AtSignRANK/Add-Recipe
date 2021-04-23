package io.github.craftrecipe;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.inventory.BrewerInventory.*;

import java.io.File;
import java.io.IOException;

public class CraftRecipe extends JavaPlugin implements Listener {
    File file = new File("plugins/CraftRecipe/config.yml");
    FileConfiguration config;
    String PluginName = "Craft-Recipe";
    Inventory invCreate = Bukkit.createInventory(null, 27, "CreateRecipe");
    Inventory invList = Bukkit.createInventory(null,  54, "RecipeList");
    Inventory invRecipe = Bukkit.createInventory(null, 27, "Recipe");
    ItemStack Nothing = new ItemStack(Material.BARRIER);
    ItemMeta NothingMeta = Nothing.getItemMeta();
    int number = 1, ListPage = 1, ItemNumber;

    public void loadConfig() {
        config = YamlConfiguration.loadConfiguration(file);
        try {
            if (!file.exists()) {
                config.save(file);
            }
            config.load(file);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            config.save(file);
        } catch (IOException E) {
        }
    }

    @Override
    public void onEnable() {
        System.out.println("[" + PluginName +"] Enabled");
        getServer().getPluginManager().registerEvents(this, this);
        loadConfig();
        number = config.getInt("number");
        int i;
        for(i=1;i<=number;i++) {
            String p_s = Integer.toString(i);
            if(config.getBoolean(p_s + ".delete")) continue;
            ItemStack IMDI = config.getItemStack(p_s + ".0");
            Material C1 = config.getItemStack(p_s + ".1").getType();
            Material C2 = config.getItemStack(p_s + ".2").getType();
            Material C3 = config.getItemStack(p_s + ".3").getType();
            Material C4 = config.getItemStack(p_s + ".4").getType();
            Material C5 = config.getItemStack(p_s + ".5").getType();
            Material C6 = config.getItemStack(p_s + ".6").getType();
            Material C7 = config.getItemStack(p_s + ".7").getType();
            Material C8 = config.getItemStack(p_s + ".8").getType();
            Material C9 = config.getItemStack(p_s + ".9").getType();
            ShapedRecipe NewRecipe = new ShapedRecipe(IMDI);
            NewRecipe.shape(new String[]{"abc","def","ghi"})
                    .setIngredient('a', C1).setIngredient('b', C2).setIngredient('c', C3)
                    .setIngredient('d', C4).setIngredient('e', C5).setIngredient('f', C6)
                    .setIngredient('g', C7).setIngredient('h', C8).setIngredient('i', C9);
            getServer().addRecipe(NewRecipe);
        }
        NothingMeta.setDisplayName("§c삭제된 레시피");
        Nothing.setItemMeta(NothingMeta);
    }

    @Override
    public void onDisable() {
        System.out.println("[" + PluginName + "] Disabled");
    }

    public void MakeRecipe(Player p) {
        int i;
        for(i=0;i<=26;i++) {
            ItemStack barrier = new ItemStack(Material.BARRIER);
            ItemMeta barriermeta = barrier.getItemMeta();
            barriermeta.setDisplayName(" ");
            barrier.setItemMeta(barriermeta);
            invCreate.setItem(i, barrier);
        }
        invCreate.setItem(2, new ItemStack(Material.AIR));
        invCreate.setItem(3, new ItemStack(Material.AIR));
        invCreate.setItem(4, new ItemStack(Material.AIR));
        invCreate.setItem(11, new ItemStack(Material.AIR));
        invCreate.setItem(12, new ItemStack(Material.AIR));
        invCreate.setItem(13, new ItemStack(Material.AIR));
        invCreate.setItem(15, new ItemStack(Material.AIR));
        invCreate.setItem(20, new ItemStack(Material.AIR));
        invCreate.setItem(21, new ItemStack(Material.AIR));
        invCreate.setItem(22, new ItemStack(Material.AIR));
        ItemStack it = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta im = it.getItemMeta();
        im.setDisplayName("§a레시피 제작");
        it.setItemMeta(im);
        invCreate.setItem(26, it);
        p.openInventory(invCreate);
    }

    public void RecipeList(Player p, int page) {
        int i;
        for(i=45*(page-1);i<45 * page;i++) {
            String p_s = Integer.toString(i+1);
            if(config.getBoolean(p_s + ".delete") && config.getItemStack(p_s + ".0") != null) {
                invList.setItem(i, Nothing);
                continue;
            }
            invList.setItem(i, config.getItemStack(p_s + ".0"));
        }
        ItemStack it = new ItemStack(Material.ARROW);
        ItemMeta im = it.getItemMeta();
        im.setDisplayName("§a이전으로");
        it.setItemMeta(im);
        invList.setItem(45, it);
        im.setDisplayName("§a다음으로");
        it.setItemMeta(im);
        invList.setItem(53, it);
        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta barriermeta = barrier.getItemMeta();
        barriermeta.setDisplayName(" ");
        barrier.setItemMeta(barriermeta);
        for(i=46;i<=52;i++) {
            invList.setItem(i, barrier);
        }
        it = new ItemStack(Material.BOOK);
        im = it.getItemMeta();
        im.setDisplayName("§a현재 페이지 : " + Integer.toString(page));
        it.setItemMeta(im);
        invList.setItem(49, it);
        p.openInventory(invList);
    }

    public Material ReturnMaterial(int a) {
        if(invCreate.getItem(a) == null) {
            return Material.AIR;
        }
        return invCreate.getItem(a).getType();
    }

    public ItemMeta ReturnItemMeta(int a) {
        if(invCreate.getItem(a) == null) {
            return null;
        }
        return invCreate.getItem(a).getItemMeta();
    }

    public int RepresValBool(boolean bool) {
        if(bool) {
            return 0;
        }
        return 1;
    }

    @EventHandler
    public void InvClick(InventoryClickEvent e) {
        Inventory inv2 = e.getClickedInventory();
        Player p = (Player) e.getWhoClicked();
        Material m = e.getCurrentItem().getType();
        if(inv2.equals(invCreate)) {
            if(!(e.getSlot() == 2 || e.getSlot() == 3 || e.getSlot() == 4 || e.getSlot() == 11 || e.getSlot() == 12
                    || e.getSlot() == 13 || e.getSlot() == 20 || e.getSlot() == 21 || e.getSlot() == 22 || e.getSlot() == 15))
                e.setCancelled(true);
            if(m.equals(Material.EMERALD_BLOCK)) {
                Material C1 = ReturnMaterial(2);
                ItemMeta IM1 = ReturnItemMeta(2);
                ItemStack I1 = new ItemStack(C1);
                I1.setItemMeta(IM1);
                Material C2 = ReturnMaterial(3);
                ItemMeta IM2 = ReturnItemMeta(3);
                ItemStack I2 = new ItemStack(C2);
                I2.setItemMeta(IM2);
                Material C3 = ReturnMaterial(4);
                ItemMeta IM3 = ReturnItemMeta(4);
                ItemStack I3 = new ItemStack(C3);
                I3.setItemMeta(IM3);
                Material C4 = ReturnMaterial(11);
                ItemMeta IM4 = ReturnItemMeta(11);
                ItemStack I4 = new ItemStack(C4);
                I4.setItemMeta(IM4);
                Material C5 = ReturnMaterial(12);
                ItemMeta IM5 = ReturnItemMeta(12);
                ItemStack I5 = new ItemStack(C5);
                I5.setItemMeta(IM5);
                Material C6 = ReturnMaterial(13);
                ItemMeta IM6 = ReturnItemMeta(13);
                ItemStack I6 = new ItemStack(C6);
                I6.setItemMeta(IM6);
                Material C7 = ReturnMaterial(20);
                ItemMeta IM7 = ReturnItemMeta(20);
                ItemStack I7 = new ItemStack(C7);
                I7.setItemMeta(IM7);
                Material C8 = ReturnMaterial(21);
                ItemMeta IM8 = ReturnItemMeta(21);
                ItemStack I8 = new ItemStack(C8);
                I8.setItemMeta(IM8);
                Material C9 = ReturnMaterial(22);
                ItemMeta IM9 = ReturnItemMeta(22);
                ItemStack I9 = new ItemStack(C9);
                I9.setItemMeta(IM9);
                Material MDI = ReturnMaterial(15);
                ItemStack IMDI = inv2.getItem(15);

                if (MDI.equals(Material.AIR)) {
                    p.sendMessage("§c공기를 제작할 수 없습니다!");
                    return;
                }
                else if(IMDI.equals(Nothing)) {
                    p.sendMessage("§cBarrier에 §\"§삭제된 레시피\"라는 이름을 제작 할 수 없습니다!");
                }

                ShapedRecipe NewRecipe = new ShapedRecipe(IMDI);
                NewRecipe.shape(new String[]{"abc", "def", "ghi"})
                        .setIngredient('a', C1).setIngredient('b', C2).setIngredient('c', C3)
                        .setIngredient('d', C4).setIngredient('e', C5).setIngredient('f', C6)
                        .setIngredient('g', C7).setIngredient('h', C8).setIngredient('i', C9);
                getServer().addRecipe(NewRecipe);
                p.closeInventory();
                number++;
                String p_s = Integer.toString(number);
                config.set("number", number);
                config.set(p_s + ".0", IMDI);
                config.set(p_s + ".1", I1);
                config.set(p_s + ".2", I2);
                config.set(p_s + ".3", I3);
                config.set(p_s + ".4", I4);
                config.set(p_s + ".5", I5);
                config.set(p_s + ".6", I6);
                config.set(p_s + ".7", I7);
                config.set(p_s + ".8", I8);
                config.set(p_s + ".9", I9);
                config.set(p_s + ".delete", false);
                saveConfig();
                p.sendMessage("§aShaped Recipe가 정상적으로 추가 되었습니다.");
            }
        }
        else if(inv2.equals(invList)) {
            e.setCancelled(true);
            if(e.getSlot() == 45) {
                if(ListPage < 1) {
                    p.closeInventory();
                    RecipeList(p, ListPage - 1);
                }
            }
            else if(e.getSlot() == 53) {
                if(ListPage > number / 45 + RepresValBool(number % 45 == 0)) {
                    p.closeInventory();
                    RecipeList(p, ListPage + 1);
                }
            }
            else if(e.getSlot() < 45) {
                if(!config.getBoolean(Integer.toString(e.getSlot() + 1) + ".delete")) {
                    OpenRecipeDelete(e.getSlot() + 1, p);
                    ItemNumber = e.getSlot() + 1;
                }
                else {
                    OpenRecipeRestore(e.getSlot() + 1, p);
                    ItemNumber = e.getSlot() + 1;
                }
            }
        }
        else if(inv2.equals(invRecipe)) {
            e.setCancelled(true);
            if(e.getSlot() == 26) {
                if(invRecipe.getItem(26).getType().equals(Material.REDSTONE_BLOCK)) {
                    config.set(Integer.toString(ItemNumber) + ".delete", true);
                }
                else if(invRecipe.getItem(26).getType().equals(Material.EMERALD_BLOCK)) {
                    config.set(Integer.toString(ItemNumber) + ".delete", false);
                }
                saveConfig();
                p.closeInventory();
                RecipeList(p, ListPage);
            }
        }
    }

    public void OpenRecipeDelete(int slot, Player p) {
        String p_s = Integer.toString(slot);
        ItemStack IMDI = config.getItemStack(p_s + ".0");
        Material C1 = config.getItemStack(p_s + ".1").getType();
        Material C2 = config.getItemStack(p_s + ".2").getType();
        Material C3 = config.getItemStack(p_s + ".3").getType();
        Material C4 = config.getItemStack(p_s + ".4").getType();
        Material C5 = config.getItemStack(p_s + ".5").getType();
        Material C6 = config.getItemStack(p_s + ".6").getType();
        Material C7 = config.getItemStack(p_s + ".7").getType();
        Material C8 = config.getItemStack(p_s + ".8").getType();
        Material C9 = config.getItemStack(p_s + ".9").getType();
        int i;
        for(i=0;i<27;i++) {
            ItemStack barrier = new ItemStack(Material.BARRIER);
            ItemMeta barriermeta = barrier.getItemMeta();
            barriermeta.setDisplayName(" ");
            barrier.setItemMeta(barriermeta);
            invRecipe.setItem(i, barrier);
        }
        invRecipe.setItem(2, new ItemStack(C1));
        invRecipe.setItem(3, new ItemStack(C2));
        invRecipe.setItem(4, new ItemStack(C3));
        invRecipe.setItem(11, new ItemStack(C4));
        invRecipe.setItem(12, new ItemStack(C5));
        invRecipe.setItem(13, new ItemStack(C6));
        invRecipe.setItem(15, IMDI);
        invRecipe.setItem(20, new ItemStack(C7));
        invRecipe.setItem(21, new ItemStack(C8));
        invRecipe.setItem(22, new ItemStack(C9));
        ItemStack it = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta im = it.getItemMeta();
        im.setDisplayName("§c삭제");
        it.setItemMeta(im);
        invRecipe.setItem(26, it);
        p.openInventory(invRecipe);
    }

    public void OpenRecipeRestore(int slot, Player p) {
        String p_s = Integer.toString(slot);
        ItemStack IMDI = config.getItemStack(p_s + ".0");
        Material C1 = config.getItemStack(p_s + ".1").getType();
        Material C2 = config.getItemStack(p_s + ".2").getType();
        Material C3 = config.getItemStack(p_s + ".3").getType();
        Material C4 = config.getItemStack(p_s + ".4").getType();
        Material C5 = config.getItemStack(p_s + ".5").getType();
        Material C6 = config.getItemStack(p_s + ".6").getType();
        Material C7 = config.getItemStack(p_s + ".7").getType();
        Material C8 = config.getItemStack(p_s + ".8").getType();
        Material C9 = config.getItemStack(p_s + ".9").getType();
        int i;
        for(i=0;i<27;i++) {
            ItemStack barrier = new ItemStack(Material.BARRIER);
            ItemMeta barriermeta = barrier.getItemMeta();
            barriermeta.setDisplayName(" ");
            barrier.setItemMeta(barriermeta);
            invRecipe.setItem(i, barrier);
        }
        invRecipe.setItem(2, new ItemStack(C1));
        invRecipe.setItem(3, new ItemStack(C2));
        invRecipe.setItem(4, new ItemStack(C3));
        invRecipe.setItem(11, new ItemStack(C4));
        invRecipe.setItem(12, new ItemStack(C5));
        invRecipe.setItem(13, new ItemStack(C6));
        invRecipe.setItem(15, IMDI);
        invRecipe.setItem(20, new ItemStack(C7));
        invRecipe.setItem(21, new ItemStack(C8));
        invRecipe.setItem(22, new ItemStack(C9));
        ItemStack it = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta im = it.getItemMeta();
        im.setDisplayName("§a복구");
        it.setItemMeta(im);
        invRecipe.setItem(26, it);
        p.openInventory(invRecipe);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        if(cmd.getName().equalsIgnoreCase("createrecipe")) {
            MakeRecipe(p);
        }
        else if(cmd.getName().equalsIgnoreCase("recipelist")) {
            ListPage = 1;
            RecipeList(p, 1);
        }
        return false;
    }
}
