package com.hisroyalty.hrbsdrills.client.screen;

import com.hisroyalty.hrbsdrills.DrillsMod;
import com.hisroyalty.hrbsdrills.container.DrillContainer;
import com.hisroyalty.hrbsdrills.entity.DrillEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class DrillContainerScreen extends AbstractContainerScreen<DrillContainer> {

    public static final ResourceLocation GUI = new ResourceLocation(DrillsMod.MODID, "textures/gui/gui_fresh.png");
    public static final ResourceLocation GUI_STOWAGE = new ResourceLocation(DrillsMod.MODID, "textures/gui/gui_stowage_fresh.png");


    public DrillContainerScreen(DrillContainer pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        DrillEntity drill = pMenu.getDrillEntity();
        boolean hasChestUpgrade = drill.getHasChestUpgrade();
        if (hasChestUpgrade) {
            this.inventoryLabelY = (this.inventoryLabelY) + (220 - this.imageHeight) -21;
            this.titleLabelY = this.titleLabelY - 40;
        }
        this.inventoryLabelY = this.inventoryLabelY - 5;
        this.titleLabelY = this.titleLabelY - 14;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

/*    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI);

        DrillEntity drill = menu.getDrillEntity();
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        //pGuiGraphics.blit(GUI, this.leftPos, this.topPos, 25, 26, this.imageWidth, this.imageHeight, 272, 199);
        if (!drill.upgrades.isEmpty()) {
            for (Upgrade upgrade : drill.upgrades.values()) {
                if (!(upgrade instanceof ChestUpgrade)) {
                    pGuiGraphics.blit(GUI, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
                }
            }
        } else {
            pGuiGraphics.blit(GUI, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
            renderLit(pGuiGraphics, x, y);
        }
    }*/

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        DrillEntity drill = menu.getDrillEntity();
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        boolean hasChestUpgrade = drill.getHasChestUpgrade();
        ResourceLocation guiToRender = hasChestUpgrade ? GUI_STOWAGE : GUI;
        int yPos = hasChestUpgrade ? this.topPos : this.topPos;
        int UWidth = hasChestUpgrade ? this.imageWidth+12 : this.imageWidth;
        int UHeight = hasChestUpgrade ? 220 : this.imageHeight;
        RenderSystem.setShaderTexture(0, guiToRender);
        pGuiGraphics.blit(guiToRender, this.leftPos, yPos, 0, 0, UWidth, UHeight);
        renderLit(pGuiGraphics, x, y);
    }

    private void renderLit(GuiGraphics guiGraphics, int x, int y) {
        DrillEntity drill = menu.getDrillEntity();
        if (drill.getProgress() > 0) {
            int progress = drill.getProgress();
            int progressScaled = (progress * 13) / drill.getMaxProgress();
            int yOffset = 13 - progressScaled-1;
            guiGraphics.blit(GUI, x + 107, y + 39 + yOffset, 207, 4 + yOffset, 13, (14) - yOffset);
        }
        if (drill.getWProgress() == 0) {
            guiGraphics.blit(GUI, x, y + 12, 176, 33, 16, 14);
        }
        if (drill.getWProgress() > 0) {
            int wprogress = drill.getWProgress();
            int wprogressScaled = (wprogress * 47) / drill.getMaxWProgress();
            int wyOffset = 20 - wprogressScaled-1;

            guiGraphics.blit(GUI, x + 112 + wyOffset, y + 12, 206+wyOffset, 37, 47-wyOffset, 9);

        }



    }
    @Override
    protected void renderTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        super.renderTooltip(pGuiGraphics, pX, pY);
    }

}
