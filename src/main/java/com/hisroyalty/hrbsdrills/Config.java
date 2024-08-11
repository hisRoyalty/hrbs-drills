package com.hisroyalty.hrbsdrills;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = DrillsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue DROP_BLOCK = BUILDER
            .comment("Whether to drop blocks when drilling")
            .define("dropBlock", true);



    public static final ForgeConfigSpec.IntValue FUEL_EFFICIENCY = BUILDER
            .comment("Fuel efficiency of the drill. Bigger the value, the less efficient the drill is.")
            .defineInRange("fuelEfficiency", 12, 1, Integer.MAX_VALUE);



   /* public static final ForgeConfigSpec.ConfigValue<TagKey<Block>> BlOCKS_THAT_CAN_BE_MINED = BUILDER
            .comment("Blocks that can be mined by the drill")
            .define("blocksThatCanBeMined", BlockTags.MINEABLE_WITH_PICKAXE);

*/



    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean dropBlock;
    public static int fuelEfficiency;
/*
    public static TagKey<Block> blocksThatCanBeMined;
*/

    private static boolean validateItemName(final Object obj)
    {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        dropBlock = DROP_BLOCK.get();
        fuelEfficiency = FUEL_EFFICIENCY.get();


        }
}
