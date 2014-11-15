package pl.asie.computronics.integration.waila.providers;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.SpecialChars;
import net.minecraft.item.ItemStack;
import pl.asie.computronics.Computronics;
import pl.asie.computronics.api.tape.IItemTapeStorage;
import pl.asie.computronics.integration.waila.ConfigValues;
import pl.asie.computronics.tile.TileTapeDrive;
import pl.asie.computronics.util.StringUtil;

import java.util.List;
import java.util.Locale;

public class WailaTapeDrive extends ComputronicsWailaProvider {

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip,
		IWailaDataAccessor accessor, IWailaConfigHandler config) {

		if(!ConfigValues.Tape.getValue(config)) {
			return currenttip;
		}

		TileTapeDrive drive = (TileTapeDrive) accessor.getTileEntity();
		ItemStack is = drive.getStackInSlot(0);
		if(is != null && is.getItem() instanceof IItemTapeStorage) {
			String label = Computronics.itemTape.getLabel(is);
			if(label.length() > 0 && ConfigValues.TapeName.getValue(config)) {
				currenttip.add(StringUtil.localizeAndFormat("tooltip.computronics.waila.tape.labeltapeinserted",
					label + SpecialChars.RESET));
			} else {
				currenttip.add(StringUtil.localize("tooltip.computronics.waila.tape.tapeinserted"));
			}
			if(ConfigValues.DriveState.getValue(config)) {
				currenttip.add(StringUtil.localizeAndFormat("tooltip.computronics.waila.tape.state",
					StringUtil.localize("tooltip.computronics.waila.tape.state."
						+ drive.getEnumState().toString().toLowerCase(Locale.ENGLISH))));
			}
		} else {
			currenttip.add(StringUtil.localize("tooltip.computronics.waila.tape.notapeinserted"));
		}
		return currenttip;
	}

	/*@Override
	public void decorateBlock(ItemStack itemStack, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		BlockTapeReader block = (BlockTapeReader) accessor.getBlock();
		int front = block.getFrontSide(accessor.getMetadata());
		String text = "ERROR";
		float rot = 0F;
		float offX = 0.5F;
		float offZ = -0.01F;
		switch(front){
			case 3:{
				rot = 180F;
				offZ = 1.01F;
				break;
			}
			case 4:{
				rot = 90F;
				offX = -0.01F;
				offZ = 0.5F;
				break;
			}
			case 5:{
				rot = 270F;
				offX = 1.01F;
				offZ = 0.5F;
				break;
			}
		}
		TileTapeDrive drive = (TileTapeDrive) accessor.getTileEntity();
		ItemStack is = drive.getStackInSlot(0);
		if(is != null && is.getItem() instanceof IItemTapeStorage) {
			if(ConfigValues.DriveState.getValue(config)) {
				text = StringUtil.localize("tooltip.computronics.waila.tape.state."
					+ drive.getEnumState().toString().toLowerCase(Locale.ENGLISH));
				UIHelper.drawFloatingText(text, accessor.getRenderingPosition(), offX, 0.3F, offZ, 0F,
					rot, 0F);
			}
		}
	}*/
}
