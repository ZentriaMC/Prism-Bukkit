package me.botsko.prism.serializers.items;

import me.botsko.prism.actions.GenericAction;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created for use for the Add5tar MC Minecraft server
 * Created by benjamincharlton on 29/10/2020.
 */
public class StorageItemStackSerializer extends ItemStackSerializer {
    public List<ItemStackSerializer> inventoryContents = new ArrayList<>();

    @Override
    public ItemStack toBukkit() {
        //todo
        return super.toBukkit();
    }

    public static StorageItemStackSerializer deserialize(String data) {
        if (data == null || !data.startsWith("{")) {
            return null;
        }
        return GenericAction.gson().fromJson(data, StorageItemStackSerializer.class);
    }
}
