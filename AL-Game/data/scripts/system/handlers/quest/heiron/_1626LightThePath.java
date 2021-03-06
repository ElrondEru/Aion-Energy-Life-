 /*
 * This file is part of aion-unique <aion-unique.org>.
 *
 * aion-unique is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-unique is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.heiron;

import java.util.Collections;

import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.quest.QuestItems;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;


/**
 * @author Balthazar
 */
 
public class _1626LightThePath extends QuestHandler
{
	private final static int	questId	= 1626;
	
	public _1626LightThePath()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(204592).addOnQuestStart(questId);
		qe.setNpcQuestData(204592).addOnTalkEvent(questId);
		qe.setNpcQuestData(700221).addOnTalkEvent(questId);
		qe.setNpcQuestData(700222).addOnTalkEvent(questId);
		qe.setNpcQuestData(700223).addOnTalkEvent(questId);
		qe.setNpcQuestData(700224).addOnTalkEvent(questId);
		qe.setNpcQuestData(700225).addOnTalkEvent(questId);
		qe.setNpcQuestData(700226).addOnTalkEvent(questId);
		qe.setNpcQuestData(700227).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		
		if(qs == null || qs.getStatus() == QuestStatus.NONE) 
		{
			if(targetId == 204592)
			{
				switch(env.getDialogId())
				{
					case 25:
					{
						return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 4762);
					}
					case 1002:
					{
						if(player.getInventory().getItemCountByItemId(182201788) == 0)
						{
							if (!ItemService.addItems(player, Collections.singletonList(new QuestItems(182201788, 1))))
							{
								return true;
							}
						}
					}
					default: return defaultQuestStartDialog(env);
				}
			}
		}
		
		if(qs == null)
			return false;
			
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(targetId)
			{
				case 700221:
				{
					switch(env.getDialogId())
					{
						case -1:
						{
							long itemCount1 = player.getInventory().getItemCountByItemId(182201788);
							if(qs.getQuestVarById(0) == 0 && itemCount1 == 1)
							{
								final int targetObjectId = env.getVisibleObject().getObjectId();
								PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
								PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
								ThreadPoolManager.getInstance().schedule(new Runnable()
								{
									@Override
									public void run()
									{
										if(!player.isTargeting(targetObjectId))
											return;
										PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
										PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
										qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
										updateQuestStatus(player, qs);
									}
								}, 3000);
								return true;
							}
						}
					}
				}
				case 700222:
				{
					switch(env.getDialogId())
					{
						case -1:
						{
							long itemCount1 = player.getInventory().getItemCountByItemId(182201788);
							if(qs.getQuestVarById(0) == 1 && itemCount1 == 1)
							{
								final int targetObjectId = env.getVisibleObject().getObjectId();
								PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
								PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
								ThreadPoolManager.getInstance().schedule(new Runnable()
								{
									@Override
									public void run()
									{
										if(!player.isTargeting(targetObjectId))
											return;
										PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
										PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
										qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
										updateQuestStatus(player, qs);
									}
								}, 3000);
								return true;
							}
						}
					}
				}
				case 700223:
				{
					switch(env.getDialogId())
					{
						case -1:
						{
							long itemCount1 = player.getInventory().getItemCountByItemId(182201788);
							if(qs.getQuestVarById(0) == 2 && itemCount1 == 1)
							{
								final int targetObjectId = env.getVisibleObject().getObjectId();
								PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
								PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
								ThreadPoolManager.getInstance().schedule(new Runnable()
								{
									@Override
									public void run()
									{
										if(!player.isTargeting(targetObjectId))
											return;
										PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
										PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
										qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
										updateQuestStatus(player, qs);
									}
								}, 3000);
								return true;
							}
						}
					}
				}
				case 700224:
				{
					switch(env.getDialogId())
					{
						case -1:
						{
							long itemCount1 = player.getInventory().getItemCountByItemId(182201788);
							if(qs.getQuestVarById(0) == 3 && itemCount1 == 1)
							{
								final int targetObjectId = env.getVisibleObject().getObjectId();
								PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
								PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
								ThreadPoolManager.getInstance().schedule(new Runnable()
								{
									@Override
									public void run()
									{
										if(!player.isTargeting(targetObjectId))
											return;
										PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
										PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
										qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
										updateQuestStatus(player, qs);
									}
								}, 3000);
								return true;
							}
						}
					}
				}
				case 700225:
				{
					switch(env.getDialogId())
					{
						case -1:
						{
							long itemCount1 = player.getInventory().getItemCountByItemId(182201788);
							if(qs.getQuestVarById(0) == 4 && itemCount1 == 1)
							{
								final int targetObjectId = env.getVisibleObject().getObjectId();
								PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
								PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
								ThreadPoolManager.getInstance().schedule(new Runnable()
								{
									@Override
									public void run()
									{
										if(!player.isTargeting(targetObjectId))
											return;
										PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
										PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
										qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
										updateQuestStatus(player, qs);
									}
								}, 3000);
								return true;
							}
						}
					}
				}
				case 700226:
				{
					switch(env.getDialogId())
					{
						case -1:
						{
							long itemCount1 = player.getInventory().getItemCountByItemId(182201788);
							if(qs.getQuestVarById(0) == 5 && itemCount1 == 1)
							{
								final int targetObjectId = env.getVisibleObject().getObjectId();
								PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
								PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
								ThreadPoolManager.getInstance().schedule(new Runnable()
								{
									@Override
									public void run()
									{
										if(!player.isTargeting(targetObjectId))
											return;
										PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
										PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
										qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
										updateQuestStatus(player, qs);
									}
								}, 3000);
								return true;
							}
						}
					}
				}
				case 700227:
				{
					switch(env.getDialogId())
					{
						case -1:
						{
							long itemCount1 = player.getInventory().getItemCountByItemId(182201788);
							if(qs.getQuestVarById(0) == 6 && itemCount1 == 1)
							{
								final int targetObjectId = env.getVisibleObject().getObjectId();
								PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
								PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
								ThreadPoolManager.getInstance().schedule(new Runnable()
								{
									@Override
									public void run()
									{
										if(!player.isTargeting(targetObjectId))
											return;
										PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
										PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
										player.getInventory().removeFromBagByItemId(182201788, 1);
										qs.setStatus(QuestStatus.REWARD);
										updateQuestStatus(player, qs);
									}
								}, 3000);
								return true;
							}
						}
					}
				}
			}
		}		
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 204592)
			{
				if(env.getDialogId() == 1009)
					return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 5);
				else return defaultQuestEndDialog(env);
			}
		}	
		return false;
	}
}