package com.championash5357.tutorial.tileentity;

import javax.annotation.Nullable;

import com.championash5357.tutorial.init.TutorialTileEntities;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityAdvancedCraftingTable extends TileEntity {

	private ItemStackHandler inventory = new ItemStackHandler(25);
	
	public TileEntityAdvancedCraftingTable() {
		super(TutorialTileEntities.ADVANCED_CRAFTING_TABLE);
	}
	
	public boolean interact(PlayerEntity player) {
		return this.world.getTileEntity(pos) != this ? false : player.getDistanceSq(player) <= 64.0D;
	}
	
	public void save() {
		this.markDirty();
	}
	
	@Override
	public void read(CompoundNBT compound) {
		this.inventory.deserializeNBT(compound.getCompound("inventory"));
		super.read(compound);
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		compound.put("inventory", inventory.serializeNBT());
		return super.write(compound);
	}
	
	@Override
	public CompoundNBT getUpdateTag() {
		return this.write(new CompoundNBT());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.read(pkt.getNbtCompound());
	}
	
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(pos, 0, this.getUpdateTag());
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return LazyOptional.of(() -> this.inventory).cast();
		return super.getCapability(cap, side);
	}
	
	public void setStackInSlot(int slot, @Nullable ItemStack stack) {
		inventory.setStackInSlot(slot, stack);
	}
	
	public ItemStack getStackInSlot(int slot) {
		return inventory.getStackInSlot(slot);
	}
}
