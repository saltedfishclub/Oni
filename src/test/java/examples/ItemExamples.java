package examples;

import io.ib67.oni.Oni;
import io.ib67.oni.onion.ItemOnion;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ItemExamples {
    private Oni oni;
    private Player player1, player2, player3;
    private Inventory inv1, inv2, inv3;

    public void testWithMeta() {
        ItemOnion io = oni.onionOf(Material.APPLE).get(); //When using of(material) you should mention that we can't make air onion...
        // (上) 注意：当使用这个方法获取 ItemOnion 的时候，不能传入空气，因此你得处理返回的Optional
        ItemOnion io2 = oni.onionOf(new ItemStack(Material.APPLE)); //这个就不用担心
        //so
        //于是
        io2.withMeta(im -> {
            im.setUnbreakable(true);
            im.backToItem().addEnchantment(Enchantment.ARROW_FIRE, 1);
            im.setLocalizedName("NullCat's dUck");
        }); // 相当于getItemMeta处理完set回去。
        //Also..
        // meta 也可以获取所属物品反向操作
        io2.withMeta(im -> im.withItem(item -> item.withMeta(meta -> {
        })));
        //Before give bukkit..
        //在把物品给Bukkit之前，需要export出原生物品
        io2.export(); //The native itemStack.
        // The recommended way to give player item is (it is easily to forget export)
        // 推荐这样给玩家，不然你可能忘记了 export
        io2.oni.send(player1, player2, player3);
        //or
        //或者给物品栏
        io2.oni.send(inv1, inv2, inv3);
    }
}
