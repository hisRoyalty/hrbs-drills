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

    public static final ResourceLocation GUI = new ResourceLocation(DrillsMod.MODID, "textures/gui/gui1.png");


    public DrillContainerScreen(DrillContainer pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = this.inventoryLabelY + 2;
        this.titleLabelY = this.titleLabelY - 3;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI);

        DrillEntity drill = menu.getDrillEntity();
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        pGuiGraphics.blit(GUI, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
            renderLit(pGuiGraphics, x, y);
    }

    private void renderLit(GuiGraphics guiGraphics, int x, int y) {
        DrillEntity drill = menu.getDrillEntity();
        if (drill.getProgress() > 0) {
            int progress = drill.getProgress();
            int progressScaled = (progress * 13) / drill.getMaxProgress();
            int yOffset = 13 - progressScaled-1;
            guiGraphics.blit(GUI, x + 109, y + 30 + yOffset, 176, 0 + yOffset, 14, (13) - yOffset);
        }
        if (drill.getWProgress() == 0) {
            guiGraphics.blit(GUI, x, y + 12, 176, 33, 16, 14);
        }
        if (drill.getWProgress() > 0) {
            int wprogress = drill.getWProgress();
            int wprogressScaled = (wprogress * 19) / drill.getMaxWProgress();
            int wyOffset = 20 - wprogressScaled-1;

            guiGraphics.blit(GUI, x + 6, y + 35 + wyOffset, 176, 14+wyOffset, 6, (19)-wyOffset);

        }



    }
    @Override
    protected void renderTooltip(GuiGraphics pGuiGraphics, int pX, int pY) {
        super.renderTooltip(pGuiGraphics, pX, pY);
    }
    /*
    @Override
    public void renderScreenBg(GuiGraphics guiGraphics, int x, int y, float partialTicks, PlaneInventoryScreen screen) {
        guiGraphics.blit(PlaneInventoryScreen.GUI, screen.getGuiLeft() + 151, screen.getGuiTop() + 44, 208, 0, 18, 35);

        if (burnTime > 0) {
            int burnLeftScaled = burnTime * 13 / (burnTimeTotal == 0 ? 200 : burnTimeTotal);
            guiGraphics.blit(PlaneInventoryScreen.GUI, screen.getGuiLeft() + 152, screen.getGuiTop() + 57 - burnLeftScaled, 208, 47 - burnLeftScaled, 14, burnLeftScaled + 1);
        }
    }

     */
}
