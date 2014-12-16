package pl.asie.computronics.integration.waila.providers;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;
import li.cil.oc.api.network.Node;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import pl.asie.computronics.integration.waila.ConfigValues;
import pl.asie.computronics.reference.Mods;
import pl.asie.computronics.tile.TileEntityPeripheralBase;
import pl.asie.computronics.util.StringUtil;

import java.util.List;

/**
 * @author Vexatos
 */
public class WailaPeripheral extends ComputronicsWailaProvider {

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
		IWailaConfigHandler config) {

		NBTTagCompound nbt = accessor.getNBTData();
		if(Loader.isModLoaded(Mods.OpenComputers) && ConfigValues.OCAddress.getValue(config)) {
			currenttip = getWailaOCBody(nbt, currenttip);
		}
		if(Loader.isModLoaded(Mods.NedoComputers) && ConfigValues.NCAddress.getValue(config)) {
			currenttip = getWailaNCBody(nbt, currenttip);
		}
		return currenttip;
	}

	@Optional.Method(modid = Mods.OpenComputers)
	private List<String> getWailaOCBody(NBTTagCompound nbt, List<String> currenttip) {
		NBTTagCompound node = nbt.getCompoundTag("oc:node");
		if(node.hasKey("address")) {
			currenttip.add(StringUtil.localizeAndFormat("oc:gui.Analyzer.Address", node.getString("address")));
		}
		return currenttip;
	}

	@Optional.Method(modid = Mods.NedoComputers)
	private List<String> getWailaNCBody(NBTTagCompound nbt, List<String> currenttip) {
		if(nbt.hasKey("nc:bus")) {
			currenttip.add(StringUtil.localizeAndFormat(
				"tooltip.computronics.waila.base.bus", nbt.getShort("nc:bus")));
		}
		return currenttip;
	}

	@Override
	public NBTTagCompound getNBTData(TileEntity te, NBTTagCompound tag, World world, int x, int y, int z) {
		if(te != null && te instanceof TileEntityPeripheralBase) {
			TileEntityPeripheralBase tile = (TileEntityPeripheralBase) te;
			if(Loader.isModLoaded(Mods.OpenComputers)) {
				tag = getNBTData_OC(tile, tag);
			}
			if(Loader.isModLoaded(Mods.NedoComputers)) {
				tag = getNBTData_NC(tile, tag);
			}
		}
		return tag;
	}

	@Optional.Method(modid = Mods.OpenComputers)
	public NBTTagCompound getNBTData_OC(TileEntityPeripheralBase tile, NBTTagCompound tag) {
		Node node = tile.node();
		if(node != null && node.host() == tile) {
			final NBTTagCompound nodeNbt = new NBTTagCompound();
			node.save(nodeNbt);
			tag.setTag("oc:node", nodeNbt);
		}
		return tag;
	}

	@Optional.Method(modid = Mods.NedoComputers)
	private NBTTagCompound getNBTData_NC(TileEntityPeripheralBase tile, NBTTagCompound tag) {
		if(tile.getBusId() != 0) {
			tag.setShort("nc:bus", (short) tile.getBusId());
		}
		return tag;
	}
}