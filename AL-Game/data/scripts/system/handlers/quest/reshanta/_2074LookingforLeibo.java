/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.reshanta;

import java.util.Collections;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.zone.ZoneName;
import com.aionemu.gameserver.services.ZoneService;
import com.aionemu.gameserver.world.zone.ZoneInstance;
import com.aionemu.gameserver.services.ItemService;
import com.aionemu.gameserver.model.templates.quest.QuestItems;
 


/**
 * @author Hellboy aion4Free
 *
 */
public class _2074LookingforLeibo extends QuestHandler
{	
	

	
	
	private final static int	questId	= 2074;
	
	public _2074LookingforLeibo()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(278036).addOnTalkEvent(questId);
		qe.setNpcQuestData(203550).addOnTalkEvent(questId);
		qe.setNpcQuestData(204207).addOnTalkEvent(questId);
		qe.setNpcQuestData(798067).addOnTalkEvent(questId);
		qe.setNpcQuestData(279029).addOnTalkEvent(questId);
		qe.setQuestItemIds(188020000).add(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);	
		
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		if(qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				case 278036:
				{
					switch(env.getDialogId())
					{
						case 25:
							if(var == 0)
								return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 1011);
						case 10000:
							if (var == 0)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(player, qs);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				} break;
				case 204207:
				{
					switch(env.getDialogId())
					{
						case 25:
							if(var == 2)
								return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 1693);
						case 10002:
							if (var == 2)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(player, qs);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				} break;
				case 798067:
				{
					switch(env.getDialogId())
					{
						case 25:
							if(var == 3)
								return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 2034);
						case 10003:
							if (var == 3)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(player, qs);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				} break;
				case 279029:
				{
					switch(env.getDialogId())
					{
						case 25:
							if(var == 4)
								return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 2375);
							else if(var == 6)
								return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 3057);	
						case 10004:
							if (var == 4)
							{
								qs.setQuestVarById(0, var + 2);
								updateQuestStatus(player, qs);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
						case 10255:
							if (var == 6)
							{
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(player, qs);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}	
					}
				} break;
				case 203550:
				{
					switch(env.getDialogId())
					{
						case 25:
							if(var == 1)
								return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 1352);
						case 10001:
							if (var == 1)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(player, qs);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				}
				
			}
		}
		else if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 278036)
			{
				if(env.getDialogId() == -1)
					return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 10002);
				else if(env.getDialogId() == 1009)
					return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 5);
				else return defaultQuestEndDialog(env);
			}
			return false;
		}
		return false;
	}
	
	@Override
	public boolean onItemUseEvent(QuestEnv env, Item item)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();
		
		if (qs.getQuestVarById(0) != 5)
			return false;
		if(id != 188020000)
			return false;
		if(!ZoneService.getInstance().isInsideZone(player, ZoneName.LEIBO_ISLAND_400010000))
			return false;		
		if(qs == null)
			return false;
		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
				player.getInventory().removeFromBagByObjectId(itemObjId, 1);
				qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
				updateQuestStatus(player, qs);
				PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 291));
			}
		}, 3000);
		return true;
	}

		@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() != QuestStatus.LOCKED || player.getCommonData().getLevel() < 26)
			return false;

		QuestState qs2 = player.getQuestStateList().getQuestState(2701);
		if(qs2 == null || qs2.getStatus() != QuestStatus.COMPLETE)
			return false;
		qs.setStatus(QuestStatus.START);
		updateQuestStatus(player, qs);
		return true;
	}
}
