package thermalexpansion.gui.client.machine;

import cofh.core.CoFHProps;
import cofh.gui.GuiBaseAdv;
import cofh.gui.element.ElementBase;
import cofh.gui.element.ElementDualScaled;
import cofh.gui.element.ElementEnergyStored;
import cofh.gui.element.ElementFluid;
import cofh.gui.element.ElementFluidTank;
import cofh.gui.element.TabConfiguration;
import cofh.gui.element.TabEnergy;
import cofh.gui.element.TabInfo;
import cofh.gui.element.TabRedstone;
import cofh.gui.element.TabTutorial;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import thermalexpansion.block.machine.TilePrecipitator;
import thermalexpansion.core.TEProps;
import thermalexpansion.gui.container.machine.ContainerPrecipitator;
import thermalexpansion.gui.element.ElementSlotOverlay;

public class GuiPrecipitator extends GuiBaseAdv {

	static final ResourceLocation TEXTURE = new ResourceLocation(TEProps.PATH_GUI_MACHINE + "Precipitator.png");
	static final String INFO = "Uses Redstone Flux to freeze various fluids!\n\nThe selected item is what you'll get, if there is enough fluid in the tank.\n\nHave a snowball fight.";

	TilePrecipitator myTile;

	ElementBase slotInput;
	ElementBase slotOutput;
	ElementFluid progressFluid;
	ElementDualScaled progressOverlay;
	ElementDualScaled speed;

	public GuiPrecipitator(InventoryPlayer inventory, TileEntity theTile) {

		super(new ContainerPrecipitator(inventory, theTile), TEXTURE);
		myTile = (TilePrecipitator) theTile;
		name = myTile.getInventoryName();
	}

	@Override
	public void initGui() {

		super.initGui();

		slotInput = addElement(new ElementSlotOverlay(this, 152, 9).setSlotInfo(0, 3, 2));
		slotOutput = addElement(new ElementSlotOverlay(this, 76, 45).setSlotInfo(3, 1, 2));

		addElement(new ElementEnergyStored(this, 8, 8, myTile.getEnergyStorage()));
		addElement(new ElementFluidTank(this, 152, 9, myTile.getTank()));
		progressFluid = (ElementFluid) addElement(new ElementFluid(this, 112, 49).setFluid(myTile.getTankFluid()).setSize(24, 16));
		progressOverlay = (ElementDualScaled) addElement(new ElementDualScaled(this, 112, 49).setMode(2).setBackground(false).setSize(24, 16)
				.setTexture(TEX_DROP_LEFT, 48, 16));
		speed = (ElementDualScaled) addElement(new ElementDualScaled(this, 44, 49).setSize(16, 16).setTexture(TEX_SNOWFLAKE, 32, 16));

		addTab(new TabEnergy(this, myTile, false));
		addTab(new TabRedstone(this, myTile));
		addTab(new TabConfiguration(this, myTile));
		addTab(new TabInfo(this, INFO));
		addTab(new TabTutorial(this, CoFHProps.tutorialTabRedstone + "\n\n" + CoFHProps.tutorialTabConfiguration + "\n\n" + CoFHProps.tutorialTabFluxRequired));
	}

	@Override
	protected void updateElementInformation() {

		slotInput.setVisible(myTile.hasSide(1));
		slotOutput.setVisible(myTile.hasSide(2));

		progressFluid.setPosition(112 + PROGRESS - myTile.getScaledProgress(PROGRESS), 49);
		progressFluid.setSize(myTile.getScaledProgress(PROGRESS), 16);
		progressOverlay.setQuantity(myTile.getScaledProgress(PROGRESS));
		speed.setQuantity(myTile.getScaledSpeed(SPEED));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {

		super.drawGuiContainerBackgroundLayer(f, x, y);

		mc.renderEngine.bindTexture(texture);
		drawCurSelection();
		drawPrevSelection();
	}

	@Override
	protected void mouseClicked(int mX, int mY, int mButton) {

		if (48 <= mouseX && mouseX < 128 && 18 <= mouseY && mouseY < 36) {
			if (49 <= mouseX && mouseX < 67) {
				myTile.setMode(0);
			} else if (79 <= mouseX && mouseX < 97) {
				myTile.setMode(1);
			} else if (109 <= mouseX && mouseX < 127) {
				myTile.setMode(2);
			}
		} else {
			super.mouseClicked(mX, mY, mButton);
		}
	}

	protected void drawCurSelection() {

		int offset = 32;
		if (myTile.getPrevSelection() == myTile.getCurSelection() && myTile.isActive()) {
			offset = 64;
		}
		switch (myTile.getCurSelection()) {
		case 0:
			drawTexturedModalRect(guiLeft + 42, guiTop + 11, 192, offset, 32, 32);
			break;
		case 1:
			drawTexturedModalRect(guiLeft + 72, guiTop + 11, 192, offset, 32, 32);
			break;
		case 2:
			drawTexturedModalRect(guiLeft + 102, guiTop + 11, 192, offset, 32, 32);
			break;
		}
	}

	protected void drawPrevSelection() {

		switch (myTile.getPrevSelection()) {
		case 0:
			drawTexturedModalRect(guiLeft + 42, guiTop + 11, 224, 32, 32, 32);
			break;
		case 1:
			drawTexturedModalRect(guiLeft + 72, guiTop + 11, 224, 32, 32, 32);
			break;
		case 2:
			drawTexturedModalRect(guiLeft + 102, guiTop + 11, 224, 32, 32, 32);
			break;
		}
	}

}