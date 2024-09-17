//package dev.niuren.systems.modules.player;
//
//import dev.niuren.ic.Setting;
//import dev.niuren.systems.modules.Module;
//
//import dev.niuren.utils.block.BlockUtil;
//import dev.niuren.utils.math.Timer;
//import dev.niuren.utils.player.InventoryUtil;
//import dev.niuren.utils.render.ColorUtil;
//import dev.niuren.utils.render.Render3DUtils;
//import meteordevelopment.orbit.EventHandler;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.Blocks;
//import net.minecraft.client.gui.screen.ChatScreen;
//import net.minecraft.client.gui.screen.ingame.InventoryScreen;
//import net.minecraft.client.util.math.MatrixStack;
//import net.minecraft.enchantment.EnchantmentHelper;
//import net.minecraft.enchantment.Enchantments;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.ItemEntity;
//import net.minecraft.entity.decoration.ArmorStandEntity;
//import net.minecraft.entity.decoration.EndCrystalEntity;
//import net.minecraft.entity.effect.StatusEffects;
//import net.minecraft.item.AirBlockItem;
//import net.minecraft.item.ItemStack;
//import net.minecraft.item.Items;
//import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
//import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
//import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
//import net.minecraft.util.Hand;
//import net.minecraft.util.math.*;
//import org.apache.http.util.EntityUtils;
//
//import java.awt.*;
//import java.text.DecimalFormat;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.atomic.AtomicInteger;
//
//@Module.Info(name = "PacketMine", chineseName = "秒挖", category = Module.Category.Player, description = "")
//
//public class PacketMine extends Module {
//	public static final List<Block> godBlocks = Arrays.asList(Blocks.COMMAND_BLOCK, Blocks.LAVA_CAULDRON, Blocks.LAVA, Blocks.WATER_CAULDRON, Blocks.WATER, Blocks.BEDROCK, Blocks.BARRIER, Blocks.END_PORTAL, Blocks.NETHER_PORTAL, Blocks.END_PORTAL_FRAME);
//
//    private final Setting<Double> delay
//        = register("Delay",50, 0, 500, 1);
//    private final Setting<Double> damage
//        = register("Damage",0.7f, 0.0f, 2.0f);
//    private final Setting<Double> range
//        = register("Range",6f, 3.0f, 10.0f);
//    private final Setting<Double> maxBreak
//        = register("MaxBreak", 3, 0, 20, 1);
//
//    private final Setting<Boolean> preferWeb
//        = register("PreferWeb", true);
//    private final Setting<Boolean> instant
//        = register("Instant", false);
//    private final Setting<Boolean> cancelPacket
//        = register("cancelPacket", false);
//    private final Setting<Boolean> wait
//        = register("wait", true);
//
//    private final Setting<Boolean> mineAir
//        = register("mineAir", true);
//    private final Setting<Boolean> farCancel
//        = register("farCancel", false);
//    private final Setting<Boolean> hotBar
//        = register("hotBar", false);
//    private final Setting<Boolean> ghostHand
//        = register("ghostHand", true);
//
//    private final Setting<Boolean> checkGround
//        = register("checkGround", true);
//    private final Setting<Boolean> onlyGround
//        = register("onlyGround", true);
//    private final Setting<Boolean> doubleBreak
//        = register("doubleBreak", true);
//    private final Setting<Boolean> usingPause
//        = register("usingPause", false);
//    private final Setting<Boolean> swing
//        = register("swing", true);
//    private final Setting<Boolean> endSwing
//        = register("endSwing", false);
//    private final Setting<Boolean> bypassGround
//        = register("bypassGround", true);
//    private final Setting<Integer> bypassTime
//        = register("bypassTime",  400, 0, 2000);
//    private final Setting<Boolean> rotate
//        = register("rotate", true);
//    private final Setting<Integer> time
//        = register("time", 100, 0, 2000);
//    private final Setting<Boolean> switchReset
//        = register("switchReset", true);
//    private final Setting<Boolean> crystal
//        = register("crystal", true);
//    private final Setting<Boolean> onlyHeadBomber
//        = register("onlyHeadBomber", true);
//    private final Setting<Boolean> waitPlace
//        = register("waitPlace", true);
//    private final Setting<Boolean> spamPlace
//        = register("spamPlace", true);
//    private final Setting<Boolean> afterBreak
//        = register("afterBreak", true);
//    private final Setting<Boolean> checkDamage
//        = register("checkDamage", true);
//    private final Setting<Double> crystalDamage
//        = register("crystalDamage",  0.7f, 0.0f, 1.0f);
//	private final EnumSetting<FadeUtils.Quad> quad = add(new EnumSetting<>("Quad", FadeUtils.Quad.In));
//    private final Setting<Boolean> autoColor
//        = register("autoColor", true);
//    //Color-----------------------------------------------------
//    private final Setting<Integer> red
//        = register("Red", 255, 0, 255);
//    private final Setting<Integer> green
//        = register("Green", 255, 0, 255);
//    private final Setting<Integer> blue
//        = register("Blue", 255, 0, 255);
//    private final Setting<Integer> alpha
//        = register("alpha", 105, 0, 255);
//    //endColor-------------------------------------------------------
//    private final Setting<Boolean> endColor
//        = register("EndColor", true);
//
//    private final Setting<Integer> ered
//        = register("EndRed", 255, 0, 255);
//    private final Setting<Integer> egreen
//        = register("EndGreen", 255, 0, 255);
//    private final Setting<Integer> eblue
//        = register("EndBlue", 255, 0, 255);
//    private final Setting<Integer> ealpha
//        = register("Endalpha", 105, 0, 255);
//    //endboxColor-------------------------------------------------------\
//    private final Setting<Integer> endboxred
//        = register("endboxRed", 255, 0, 255);
//    private final Setting<Integer> endboxgreen
//        = register("endboxGreen", 255, 0, 255);
//    private final Setting<Integer> endboxblue
//        = register("endboxBlue", 255, 0, 255);
//    private final Setting<Integer> endboxalpha
//        = register("endboxalpha", 105, 0, 255);
//    //DoubleColor-----------------------------------------
//    private final Setting<Integer> doublered
//        = register("doubleRed", 255, 0, 255);
//    private final Setting<Integer> doublegreen
//        = register("doubleGreen", 255, 0, 255);
//    private final Setting<Integer> doubleblue
//        = register("doubleBlue", 255, 0, 255);
//    private final Setting<Integer> doublealpha
//        = register("doublealpha", 105, 0, 255);
////---------------------------------------------------------
//
//
//    private final Setting<Boolean> bold
//        = register("bold", false);
//    private final Setting<Integer> lineWidth
//        = register("lineWidth",   4,1,5);
//    private final Setting<Boolean> text
//        = register("text", true);
//    private final Setting<Boolean> box
//        = register("box", true);
//    private final Setting<Boolean> outline
//        = register("outline", true);
//
//	int lastSlot = -1;
//
//	public static PacketMine INSTANCE;
//	public static BlockPos breakPos;
//	public static BlockPos secondPos;
//	public static double progress = 0;
//	public static double secondProgress = 0;
//
//	private final Timer mineTimer = new Timer();
//	private final FadeUtils animationTime = new FadeUtils(1000);
//	private final FadeUtils secondAnim = new FadeUtils(1000);
//	private boolean startMine = false;
//	private int breakNumber = 0;
//	public final Timer secondTimer = new Timer();
//	private final Timer delayTimer = new Timer();
//	private final Timer placeTimer = new Timer();
//	public static boolean sendGroundPacket = false;
//	public PacketMine() {
//		super();
//		INSTANCE = this;
//	}
//
//	@Override
//	public String getInfo() {
//		if (instant.get()) {
//			return "Instant";
//		}
//		return "Aborted";
//	}
//
//	private int findCrystal() {
//		if (!hotBar.get()) {
//			return InventoryUtil.findItemInventorySlot(Items.END_CRYSTAL);
//		} else {
//			return InventoryUtil.findItem(Items.END_CRYSTAL);
//		}
//	}
//	private int findBlock(Block block) {
//		if (!hotBar.get()) {
//			return InventoryUtil.findBlockInventorySlot(block);
//		} else {
//			return InventoryUtil.findBlock(block);
//		}
//	}
//
//	private void doSwap(int slot, int inv) {
//		if (hotBar.get()) {
//			InventoryUtil.switchToSlot(slot);
//		} else {
//			InventoryUtil.inventorySwap(inv, mc.player.getInventory().selectedSlot);
//		}
//	}
//
//	static DecimalFormat df = new DecimalFormat("0.0");
//	@EventHandler
//	public void onRender(MatrixStack matrixStack, float partialTicks) {
//		update();
//		if (!mc.player.isCreative()) {
//			if (secondPos != null) {
//				int slot = getTool(secondPos);
//				if (slot == -1) {
//					slot = mc.player.getInventory().selectedSlot;
//				}
//				double breakTime = getBreakTime(secondPos, slot);
//				secondProgress = (double) secondTimer.getPassedTimeMs() / breakTime;
//
//				if (isAir(secondPos)) {
//					secondPos = null;
//					return;
//				}
//				double iProgress = secondProgress > 1 ? 1 : secondProgress;
//
//				double ease = (1 - secondAnim.getQuad(quad.get())) * 0.5;
//				if (!bold.get()) {
//					Render3DUtils.draw3DBox(matrixStack, new Box(secondPos).shrink(ease, ease, ease).shrink(-ease, -ease, -ease)
//							, ColorUtil.injectAlpha(new Color(doublered.get(),doublegreen.get(),doubleblue.get()), (int) (doublealpha.get() * iProgress))
//							, outline.get()
//							, box.get());
//				} else {
//					Render3DUtils.drawLine(new Box(secondPos).shrink(ease, ease, ease).shrink(-ease, -ease, -ease)
//							, ColorUtil.injectAlpha(new Color(doublered.get(),doublegreen.get(),doubleblue.get()), (int) (doublealpha.get() * iProgress)), lineWidth.get());
//					Render3DUtils.drawFill(matrixStack, new Box(secondPos).shrink(ease, ease, ease).shrink(-ease, -ease, -ease)
//							, ColorUtil.injectAlpha(new Color(doublered.get(),doublegreen.get(),doubleblue.get()), (int) (doublealpha.get() * iProgress))
//					);
//				}
//			} else {
//				secondProgress = 0;
//			}
//			if (breakPos != null) {
//				int slot = getTool(breakPos);
//				if (slot == -1) {
//					slot = mc.player.getInventory().selectedSlot;
//				}
//				double breakTime = getBreakTime(breakPos, slot);
//				progress = (double) mineTimer.getPassedTimeMs() / breakTime;
//				animationTime.setLength((long) getBreakTime(breakPos, slot));
//
//				if (text.get()) {
//					if (isAir(breakPos)) {
//						Render3DUtils.drawText3D("Waiting", breakPos.toCenterPos(), -1);
//					} else {
//						if ((int) mineTimer.getPassedTimeMs() < breakTime) {
//							Render3DUtils.drawText3D(df.format(progress * 100) + "%", breakPos.toCenterPos(), -1);
//						} else {
//							Render3DUtils.drawText3D("100.0%", breakPos.toCenterPos(), -1);
//						}
//					}
//				}
//			} else {
//				progress = 0;
//			}
//		} else {
//			progress = 0;
//			secondProgress = 0;
//		}
//	}
//
//	@Override
//	public void onLogin() {
//		startMine = false;
//		breakPos = null;
//		secondPos = null;
//	}
//
//	@Override
//	public void onDisable() {
//		startMine = false;
//		breakPos = null;
//	}
//
//	@Override
//	public void onUpdate() {
//		update();
//	}
//
//
//	public void update() {
//		if (nullCheck()) return;
//		if (mc.player.isDead()) {
//			secondPos = null;
//		}
//		if (secondPos != null && secondTimer.passed(getBreakTime(secondPos, mc.player.getInventory().selectedSlot, 1.3))) {
//			secondPos = null;
//		}
//		if (secondPos != null && isAir(secondPos)) {
//			secondPos = null;
//		}
//		if (mc.player.isCreative()) {
//			startMine = false;
//			breakNumber = 0;
//			breakPos = null;
//			return;
//		}
//		if (breakPos == null) {
//			breakNumber = 0;
//			startMine = false;
//			return;
//		}
//		if (isAir(breakPos)) {
//			breakNumber = 0;
//		}
//		if (breakNumber > maxBreak.get() - 1 && maxBreak.get() > 0 || !wait.get() && isAir(breakPos) && !instant.get()) {
//			if (breakPos.equals(secondPos)) {
//				secondPos = null;
//			}
//			startMine = false;
//			breakNumber = 0;
//			breakPos = null;
//			return;
//		}
//		if (godBlocks.contains(mc.world.getBlockState(breakPos).getBlock())) {
//			breakPos = null;
//			startMine = false;
//			return;
//		}
//		if (usingPause.get() && EntityUtil.isUsing()) {
//			return;
//		}
//		if (MathHelper.sqrt((float) EntityUtil.getEyesPos().squaredDistanceTo(breakPos.toCenterPos())) > range.get()) {
//			if (farCancel.get()) {
//				startMine = false;
//				breakNumber = 0;
//				breakPos = null;
//			}
//			return;
//		}
//		if (!hotBar.get() && mc.currentScreen != null && !(mc.currentScreen instanceof ChatScreen) && !(mc.currentScreen instanceof InventoryScreen) && !(mc.currentScreen instanceof ClickGuiScreen)) {
//			return;
//		}
//
//		int slot = getTool(breakPos);
//		if (slot == -1) {
//			slot = mc.player.getInventory().selectedSlot;
//		}
//		if (isAir(breakPos)) {
//			if (shouldCrystal()) {
//				for (Direction facing : Direction.values()) {
//					CombatUtil.attackCrystal(breakPos.offset(facing), rotate.get(), true);
//				}
//			}
//			if (placeTimer.passedMs(placeDelay.get())) {
//				if (BlockUtil.canPlace(breakPos) && mc.currentScreen == null) {
//					if (enderChest.isPressed()) {
//						int eChest = findBlock(Blocks.ENDER_CHEST);
//						if (eChest != -1) {
//							int oldSlot = mc.player.getInventory().selectedSlot;
//							doSwap(eChest, eChest);
//							BlockUtil.placeBlock(breakPos, rotate.get(), true);
//							doSwap(oldSlot, eChest);
//							placeTimer.reset();
//						}
//					} else if (obsidian.isPressed()) {
//
//						int obsidian = findBlock(Blocks.OBSIDIAN);
//						if (obsidian != -1) {
//
//							boolean hasCrystal = false;
//							if (shouldCrystal()) {
//								for (Entity entity : mc.world.getNonSpectatingEntities(Entity.class, new Box(breakPos.up()))) {
//									if (entity instanceof EndCrystalEntity) {
//										hasCrystal = true;
//										break;
//									}
//								}
//							}
//
//							if (!hasCrystal || spamPlace.get()) {
//								int oldSlot = mc.player.getInventory().selectedSlot;
//								doSwap(obsidian, obsidian);
//								BlockUtil.placeBlock(breakPos, rotate.get(), true);
//								doSwap(oldSlot, obsidian);
//								placeTimer.reset();
//							}
//						}
//					}
//				}
//			}
//			breakNumber = 0;
//		} else if (canPlaceCrystal(breakPos.up(), true)) {
//			if (waitPlace.get()) {
//				for (Direction i : Direction.values()) {
//					if (breakPos.offset(i).equals(AutoCrystal.crystalPos)) {
//						if (AutoCrystal.INSTANCE.canPlaceCrystal(AutoCrystal.crystalPos, false, false)) {
//							return;
//						}
//						break;
//					}
//				}
//			}
//			if (shouldCrystal()) {
//				if (placeTimer.passedMs(placeDelay.get())) {
//					if (checkDamage.get()) {
//						if (mineTimer.getPassedTimeMs() / getBreakTime(breakPos, slot) >= crystalDamage.get()) {
//							int crystal = findCrystal();
//							if (crystal != -1) {
//								int oldSlot = mc.player.getInventory().selectedSlot;
//								doSwap(crystal, crystal);
//								BlockUtil.placeCrystal(breakPos.up(), rotate.get());
//								doSwap(oldSlot, crystal);
//								placeTimer.reset();
//								if (waitPlace.get()) return;
//							}
//						}
//					} else {
//						int crystal = findCrystal();
//						if (crystal != -1) {
//							int oldSlot = mc.player.getInventory().selectedSlot;
//							doSwap(crystal, crystal);
//							BlockUtil.placeCrystal(breakPos.up(), rotate.get());
//							doSwap(oldSlot, crystal);
//							placeTimer.reset();
//							if (waitPlace.get()) return;
//						}
//					}
//				} else if (startMine) {
//					return;
//				}
//			}
//		}
//		if (!delayTimer.passedMs((long) delay.get())) return;
//		if (startMine) {
//			if (isAir(breakPos)) {
//				return;
//			}
//			if (onlyGround.get() && !mc.player.isOnGround()) return;
//			if (mineTimer.passedMs((long) getBreakTime(breakPos, slot))) {
//				int old = mc.player.getInventory().selectedSlot;
//				boolean shouldSwitch;
//				if (hotBar.get()) {
//					shouldSwitch = slot != old;
//				} else {
//					if (slot < 9) {
//						slot = slot + 36;
//					}
//					shouldSwitch = old + 36 != slot;
//				}
//				if (shouldSwitch) {
//					if (hotBar.get()) {
//						InventoryUtil.switchToSlot(slot);
//					} else {
//						InventoryUtil.inventorySwap(slot, mc.player.getInventory().selectedSlot);
//					}
//				}
//				if (rotate.get()) {
//					EntityUtil.facePosSide(breakPos, BlockUtil.getClickSide(breakPos));
//				}
//				if (endSwing.get()) EntityUtil.swingHand(Hand.MAIN_HAND, CombatSetting.INSTANCE.swingMode.get());
//				mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, breakPos, BlockUtil.getClickSide(breakPos)));
//				if (shouldSwitch && ghostHand.get()) {
//					if (hotBar.get()) {
//						InventoryUtil.switchToSlot(old);
//					} else {
//						InventoryUtil.inventorySwap(slot, mc.player.getInventory().selectedSlot);
//						EntityUtil.syncInventory();
//					}
//				}
//				breakNumber++;
//				delayTimer.reset();
//				if (afterBreak.get() && shouldCrystal()) {
//					for (Direction facing : Direction.values()) {
//						CombatUtil.attackCrystal(breakPos.offset(facing), rotate.get(), true);
//					}
//				}
//			}
//		} else {
//			if (!mineAir.get() && isAir(breakPos)) {
//				return;
//			}
//			animationTime.setLength((long) getBreakTime(breakPos, slot));
//			mineTimer.reset();
//			if (swing.get()) {
//				EntityUtil.swingHand(Hand.MAIN_HAND, CombatSetting.INSTANCE.swingMode.get());
//			}
//			mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, breakPos, BlockUtil.getClickSide(breakPos)));
//			delayTimer.reset();
//		}
//	}
//
//	@EventHandler
//	public void onAttackBlock(ClickBlockEvent event) {
//		if (nullCheck() || mc.player.isCreative()) {
//			return;
//		}
//		event.cancel();
//		if (godBlocks.contains(mc.world.getBlockState(event.getBlockPos()).getBlock())) {
//			return;
//		}
//		if (event.getBlockPos().equals(breakPos)) {
//			return;
//		}
//		breakPos = event.getBlockPos();
//		mineTimer.reset();
//		animationTime.reset();
//		if (godBlocks.contains(mc.world.getBlockState(event.getBlockPos()).getBlock())) {
//			return;
//		}
//		startMine();
//	}
//	public static boolean canPlaceCrystal(BlockPos pos, boolean ignoreItem) {
//		BlockPos obsPos = pos.down();
//		BlockPos boost = obsPos.up();
//		return (BlockUtil.getBlock(obsPos) == Blocks.BEDROCK || BlockUtil.getBlock(obsPos) == Blocks.OBSIDIAN)
//				&& BlockUtil.getClickSideStrict(obsPos) != null
//				&& noEntity(boost, ignoreItem)
//				&& noEntity(boost.up(), ignoreItem)
//				&& (!CombatSetting.INSTANCE.lowVersion.get() || getBlock(boost.up()) == Blocks.AIR);
//	}
//
//	public static boolean noEntity(BlockPos pos, boolean ignoreItem) {
//		for (Entity entity : mc.world.getNonSpectatingEntities(Entity.class, new Box(pos))) {
//			if (entity instanceof ItemEntity && ignoreItem || entity instanceof ArmorStandEntity && CombatSetting.INSTANCE.obsMode.get()) continue;
//			return false;
//		}
//		return true;
//	}
//	public void mine(BlockPos pos) {
//		if (nullCheck() || mc.player.isCreative()) {
//			return;
//		}
//		if (isOff()) {
//			return;
//		}
//		if (godBlocks.contains(mc.world.getBlockState(pos).getBlock())) {
//			return;
//		}
//		if (pos.equals(breakPos)) {
//			return;
//		}
//		if (breakPos != null && preferWeb.get() && BlockUtil.getBlock(breakPos) == Blocks.COBWEB) {
//			return;
//		}
//		breakPos = pos;
//		mineTimer.reset();
//		animationTime.reset();
//		startMine();
//	}
//	private boolean shouldCrystal() {
//		return crystal.get() && (!onlyHeadBomber.get() || obsidian.isPressed()); //|| HeadBomber.INSTANCE.isOn());
//	}
//	private void startMine() {
//		if (rotate.get()) {
//			Vec3i vec3i = BlockUtil.getClickSide(breakPos).getVector();
//			EntityUtil.faceVector(breakPos.toCenterPos().add(new Vec3d(vec3i.getX() * 0.5, vec3i.getY() * 0.5, vec3i.getZ() * 0.5)));
//		}
//		mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, breakPos, BlockUtil.getClickSide(breakPos)));
//		if (doubleBreak.get()) {
//			if (secondPos == null || isAir(secondPos)) {
//				int slot = getTool(breakPos);
//				if (slot == -1) {
//					slot = mc.player.getInventory().selectedSlot;
//				}
//				double breakTime = (getBreakTime(breakPos, slot, 1));
//				secondAnim.reset();
//				secondAnim.setLength((long) breakTime);
//				secondTimer.reset();
//				secondPos = breakPos;
//			}
//			mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, breakPos, BlockUtil.getClickSide(breakPos)));
//			mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, breakPos, BlockUtil.getClickSide(breakPos)));
//		}
//		if (swing.get()) {
//			EntityUtil.swingHand(Hand.MAIN_HAND, CombatSetting.INSTANCE.swingMode.get());
//		}
//		breakNumber = 0;
//	}
//
//	public int getTool(BlockPos pos) {
//		if (hotBar.get()) {
//			int index = -1;
//			float CurrentFastest = 1.0f;
//			for (int i = 0; i < 9; ++i) {
//				final ItemStack stack = mc.player.getInventory().getStack(i);
//				if (stack != ItemStack.EMPTY) {
//					final float digSpeed = EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, stack);
//					final float destroySpeed = stack.getMiningSpeedMultiplier(mc.world.getBlockState(pos));
//					if (digSpeed + destroySpeed > CurrentFastest) {
//						CurrentFastest = digSpeed + destroySpeed;
//						index = i;
//					}
//				}
//			}
//			return index;
//		} else {
//			AtomicInteger slot = new AtomicInteger();
//			slot.set(-1);
//			float CurrentFastest = 1.0f;
//			for (Map.Entry<Integer, ItemStack> entry : InventoryUtil.getInventoryAndHotbarSlots().entrySet()) {
//				if (!(entry.get().getItem() instanceof AirBlockItem)) {
//					final float digSpeed = EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, entry.get());
//					final float destroySpeed = entry.get().getMiningSpeedMultiplier(mc.world.getBlockState(pos));
//					if (digSpeed + destroySpeed > CurrentFastest) {
//						CurrentFastest = digSpeed + destroySpeed;
//						slot.set(entry.getKey());
//					}
//				}
//			}
//			return slot.get();
//		}
//	}
//
//	@EventHandler(priority =  EventPriority.LOW)
//	public void onRotate(RotateEvent event) {
//		if (nullCheck() || mc.player.isCreative()) {
//			return;
//		}
//		if (onlyGround.get() && !mc.player.isOnGround()) return;
//		if (rotate.get() && breakPos != null && !isAir(breakPos) && time.get() > 0) {
//			if (MathHelper.sqrt((float) EntityUtil.getEyesPos().squaredDistanceTo(breakPos.toCenterPos())) > range.get()) {
//				return;
//			}
//			int slot = getTool(breakPos);
//			if (slot == -1) {
//				slot = mc.player.getInventory().selectedSlot;
//			}
//			double breakTime = (getBreakTime(breakPos, slot) - time.get());
//			if (breakTime <= 0 || mineTimer.passedMs((long) breakTime)) {
//				facePosFacing(breakPos, BlockUtil.getClickSide(breakPos), event);
//			}
//		}
//	}
//
//	public static void facePosFacing(BlockPos pos, Direction side, RotateEvent event) {
//		final Vec3d hitVec = pos.toCenterPos().add(new Vec3d(side.getVector().getX() * 0.5, side.getVector().getY() * 0.5, side.getVector().getZ() * 0.5));
//		faceVector(hitVec, event);
//	}
//
//	private static void faceVector(Vec3d vec, RotateEvent event) {
//		float[] rotations = EntityUtil.getLegitRotations(vec);
//		event.setRotation(rotations[0], rotations[1]);
//	}
//
//	@EventHandler(priority = EventPriority.LOWEST)
//	public void onPacketSend(PacketEvent.Send event) {
//		if (nullCheck() || mc.player.isCreative()) {
//			return;
//		}
//		if (event.getPacket() instanceof PlayerMoveC2SPacket) {
//			if (bypassGround.get() && breakPos != null && !isAir(breakPos) && bypassTime.get() > 0 && MathHelper.sqrt((float) breakPos.toCenterPos().squaredDistanceTo(EntityUtil.getEyesPos())) <= range.getFloat() + 2) {
//				int slot = getTool(breakPos);
//				if (slot == -1) {
//					slot = mc.player.getInventory().selectedSlot;
//				}
//				double breakTime = (getBreakTime(breakPos, slot) - bypassTime.get());
//				if (breakTime <= 0 || mineTimer.passedMs((long) breakTime)) {
//					sendGroundPacket = true;
//					((IPlayerMoveC2SPacket) event.getPacket()).setOnGround(true);
//				}
//			} else {
//				sendGroundPacket = false;
//			}
//			return;
//		}
//		if (event.getPacket() instanceof UpdateSelectedSlotC2SPacket packet) {
//			if (packet.getSelectedSlot() != lastSlot) {
//				lastSlot = packet.getSelectedSlot();
//				if (switchReset.get()) {
//					startMine = false;
//					mineTimer.reset();
//					animationTime.reset();
//				}
//			}
//			return;
//		}
//		if (!(event.getPacket() instanceof PlayerActionC2SPacket)) {
//			return;
//		}
//		if (((PlayerActionC2SPacket) event.getPacket()).getAction() == PlayerActionC2SPacket.Action.START_DESTROY_BLOCK) {
//			if (breakPos == null || !((PlayerActionC2SPacket) event.getPacket()).getPos().equals(breakPos)) {
//				if (cancelPacket.get()) event.cancel();
//				return;
//			}
//			startMine = true;
//		} else if (((PlayerActionC2SPacket) event.getPacket()).getAction() == PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK) {
//			if (breakPos == null || !((PlayerActionC2SPacket) event.getPacket()).getPos().equals(breakPos)) {
//				if (cancelPacket.get()) event.cancel();
//				return;
//			}
//			if (!instant.get()) {
//				startMine = false;
//			}
//		}
//	}
//
//	public final double getBreakTime(BlockPos pos, int slot) {
//		return getBreakTime(pos, slot, damage.get());
//	}
//
//	public final double getBreakTime(BlockPos pos, int slot, double damage) {
//		return (1 / getBlockStrength(pos, mc.player.getInventory().getStack(slot)) / 20 * 1000 * damage);
//	}
//
//
//	private boolean canBreak(BlockPos pos) {
//		final BlockState blockState = mc.world.getBlockState(pos);
//		final Block block = blockState.getBlock();
//		return block.getHardness() != -1;
//	}
//
//	public float getBlockStrength(BlockPos position, ItemStack itemStack) {
//		BlockState state = mc.world.getBlockState(position);
//		float hardness = state.getHardness(mc.world, position);
//		if (hardness < 0) {
//			return 0;
//		}
//		if (!canBreak(position)) {
//			return getDigSpeed(state, itemStack) / hardness / 100F;
//		} else {
//			return getDigSpeed(state, itemStack) / hardness / 30F;
//		}
//	}
//
//	public float getDigSpeed(BlockState state, ItemStack itemStack) {
//		float digSpeed = getDestroySpeed(state, itemStack);
//		if (digSpeed > 1) {
//			int efficiencyModifier = EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, itemStack);
//			if (efficiencyModifier > 0 && !itemStack.isEmpty()) {
//				digSpeed += StrictMath.pow(efficiencyModifier, 2) + 1;
//			}
//		}
//		if (mc.player.hasStatusEffect(StatusEffects.HASTE)) {
//			digSpeed *= 1 + (mc.player.getStatusEffect(StatusEffects.HASTE).getAmplifier() + 1) * 0.2F;
//		}
//		if (mc.player.hasStatusEffect(StatusEffects.MINING_FATIGUE)) {
//			float fatigueScale;
//			switch (mc.player.getStatusEffect(StatusEffects.MINING_FATIGUE).getAmplifier()) {
//				case 0 -> fatigueScale = 0.3F;
//				case 1 -> fatigueScale = 0.09F;
//				case 2 -> fatigueScale = 0.0027F;
//				default -> fatigueScale = 8.1E-4F;
//			}
//			digSpeed *= fatigueScale;
//		}
//		if (mc.player.isSubmergedInWater() && !EnchantmentHelper.hasAquaAffinity(mc.player)) {
//			digSpeed /= 5;
//		}
//		if (!mc.player.isOnGround() && INSTANCE.checkGround.get()) {
//			digSpeed /= 5;
//		}
//		return (digSpeed < 0 ? 0 : digSpeed);
//	}
//
//	public float getDestroySpeed(BlockState state, ItemStack itemStack) {
//		float destroySpeed = 1;
//		if (itemStack != null && !itemStack.isEmpty()) {
//			destroySpeed *= itemStack.getMiningSpeedMultiplier(state);
//		}
//		return destroySpeed;
//	}
//
//	private boolean isAir(BlockPos breakPos) {
//		return mc.world.isAir(breakPos) || BlockUtil.getBlock(breakPos) == Blocks.FIRE && BlockUtil.hasCrystal(breakPos);
//	}
//}
