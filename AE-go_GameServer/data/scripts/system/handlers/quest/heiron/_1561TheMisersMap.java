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
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.quest.QuestItems;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author Balthazar
 * 
 */
public class _1561TheMisersMap extends QuestHandler
{
	private final static int	questId	= 1561;

	public _1561TheMisersMap()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(700188).addOnTalkEvent(questId);
		qe.setQuestItemIds(182201728).add(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);

		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
			
		if(targetId == 0)
		{
			if(env.getDialogId() == 1002)
			{
				QuestService.startQuest(env, QuestStatus.START);
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
				return true;
			}
			else if(env.getDialogId() == 17)
			{
				qs.setStatus(QuestStatus.COMPLETE);
				qs.setCompliteCount(1);
				updateQuestStatus(player, qs);
				int rewardExp = player.getRates().getQuestXpRate() * 270000;
				ItemService.addItems(player, Collections.singletonList(new QuestItems(152000105, 10)));
				player.getCommonData().addExp(rewardExp);
				PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(questId, QuestStatus.COMPLETE, 2));
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
				return true;
			}
		}
		else if(targetId == 700188)
		{
			if(qs.getStatus() == QuestStatus.START)
			{
				switch(env.getDialogId())
				{
					case -1:
					{
						final int targetObjectId = env.getVisibleObject().getObjectId();
						PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
						ThreadPoolManager.getInstance().schedule(new Runnable()
						{
							@Override
							public void run()
							{
								if(!player.isTargeting(targetObjectId))
									return;
							
								PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
								QuestState qs = player.getQuestStateList().getQuestState(questId);
								sendQuestDialog(player, 0, 5);
								PacketSendUtility.broadcastPacket(player.getTarget(), new SM_EMOTION((Creature)player.getTarget(), EmotionType.DIE, 128, 0));								
							}
						}, 3000);
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean onItemUseEvent(QuestEnv env, Item item)
	{
		final Player player = env.getPlayer();
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);

		if(id != 182201728)
			return false;
			
		if(qs != null ) 
		{
			if(qs.getStatus() == QuestStatus.COMPLETE)
			{
			player.getInventory().removeFromBagByItemId(182201728, 1);
				return false;
			}
		}

		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
		ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
				sendQuestDialog(player, 0, 4);
			}
		}, 3000);
		return true;
	}	
}