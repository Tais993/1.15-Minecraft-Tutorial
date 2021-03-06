package com.championash5357.tutorial.block;

import com.championash5357.tutorial.tileentity.TileEntityJar;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockJar extends Block {

	public BlockJar() {
		super(Properties.create(Material.GLASS).hardnessAndResistance(.2f));
		setRegistryName("jar");
	}
	
	@Override
	public ActionResultType func_225533_a_(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		TileEntityJar jar = (TileEntityJar) worldIn.getTileEntity(pos);
		if(jar.amount() < 20 && !player.getHeldItem(hand).isEmpty() && !player.func_225608_bj_() && player.getHeldItem(hand).getItem().equals(Items.COOKIE)) {
			player.getHeldItem(hand).shrink(1);
			jar.addCookie();
			return ActionResultType.SUCCESS;
		} else if(player.func_225608_bj_() && jar.amount() > 0){
			player.addItemStackToInventory(new ItemStack(Items.COOKIE));
			jar.removeCookie();
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}
	
	@Override
	public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, TileEntity te, ItemStack stack) {
		TileEntityJar jar = (TileEntityJar) te;
		while(jar.amount() > 0) {
			if(!worldIn.isRemote) worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.COOKIE)));
			jar.removeCookie();
		}
		super.harvestBlock(worldIn, player, pos, state, te, stack);
	}
	
	/*@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}*/
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 13.0D, 13.0D);
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityJar();
	}
}
