/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.apiculture.tiles;

import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import forestry.api.apiculture.BeeManager;
import forestry.core.config.Constants;
import forestry.core.config.ForestryBlock;
import forestry.core.inventory.InventoryPlain;
import forestry.core.network.GuiId;
import forestry.core.tiles.TileNaturalistChest;
import forestry.core.tiles.TileUtil;

public class TileApiaristChest extends TileNaturalistChest {

	private boolean checkedForLegacyBlock = false;

	public TileApiaristChest() {
		super(BeeManager.beeRoot, GuiId.ApiaristChestGUI.ordinal());
	}

	@Override
	protected void updateServerSide() {
		if (worldObj != null && !checkedForLegacyBlock) {
			Block block = worldObj.getBlock(xCoord, yCoord, zCoord);
			if (ForestryBlock.apiculture.isBlockEqual(block)) {
				migrateFromLegacyBlock();
			}

			checkedForLegacyBlock = true;
		}
	}

	private void migrateFromLegacyBlock() {
		IInventory inventoryCopy = new InventoryPlain(getInternalInventory());

		// clear the inventory so it isn't dropped when the block is replaced
		for (int i = 0; i < getSizeInventory(); i++) {
			setInventorySlotContents(i, null);
		}

		worldObj.setBlockToAir(xCoord, yCoord, zCoord);
		worldObj.setBlock(xCoord, yCoord, zCoord, ForestryBlock.apicultureChest.block(), Constants.DEFINITION_APIARISTCHEST_META, Constants.FLAG_BLOCK_SYNCH_AND_UPDATE);

		TileApiaristChest tile = TileUtil.getTile(worldObj, xCoord, yCoord, zCoord, TileApiaristChest.class);
		for (int i = 0; i < getSizeInventory(); i++) {
			ItemStack stack = inventoryCopy.getStackInSlot(i);
			tile.setInventorySlotContents(i, stack);
		}
		tile.markDirty();
	}

}