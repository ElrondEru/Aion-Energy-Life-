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

import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.quest.QuestItems;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
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
 
public class _1624WeaponsOfTheAncientWarriors extends QuestHandler
{
	private final static int	questId	= 1624;
	
	public _1624WeaponsOfTheAncientWarriors()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(204593).addOnQuestStart(questId);
		qe.setNpcQuestData(204593).addOnTalkEvent(questId);
		qe.setNpcQuestData(700201).addOnTalkEvent(questId);
		qe.setNpcQuestData(700202).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		
		if(qs == null || qs.getStatus() == QuestStatus.NONE) 
		{
			if(targetId == 204593)
			{
				if(env.getDialogId() == 25)
				{
					return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 1011);
				}
				else return defaultQuestStartDialog(env);
			}
		}
		
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(targetId)
			{
				case 204593:
				{
					switch(env.getDialogId())
					{
						case 25:
						{
							long itemCount1 = player.getInventory().getItemCountByItemId(182201753);
							long itemCount2 = player.getInventory().getItemCountByItemId(182201754);
							if(itemCount1 >= 3 && itemCount2 >= 3)
							{
								player.getInventory().removeFromBagByItemId(182201753, 1);
								player.getInventory().removeFromBagByItemId(182201754, 1);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(player, qs);
								return defaultQuestEndDialog(env);
							}
						}
					}
				}
				case 700201:
				{
					switch(env.getDialogId())
					{
						case -1:
						{
							long itemCount3 = player.getInventory().getItemCountByItemId(182201753);
							if(itemCount3 < 3)
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
										PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0, targetObjectId), true);
									}
								}, 3000);
								return true;
							}
						}
					}
				}
				case 700202:
				{
					switch(env.getDialogId())
					{
						case -1:
						{
							long itemCount3 = player.getInventory().getItemCountByItemId(182201754);
							if(itemCount3 < 3)
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
										PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0, targetObjectId), true);
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
			if (targetId == 204593)
			{
				if(env.getDialogId() == 1009)
					return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 5);
				else return defaultQuestEndDialog(env);
			}
		}	
		return false;
	}
}