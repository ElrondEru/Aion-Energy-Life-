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
package quest.beluslan;

import java.util.Collections;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.quest.QuestItems;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.ItemService;
import com.aionemu.gameserver.services.ZoneService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.zone.ZoneName;
 

/**
 * @author Hellboy aion4Free
 * 
 */
public class _2061SuppressingtheBakarmaLegion extends QuestHandler
{
	
	
	
	private final static int	questId	= 2061;
	private final static int[]	npc_ids	= { 204702, 278001, 204807, 700295, 204052};

	public _2061SuppressingtheBakarmaLegion()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setQuestEnterZone(ZoneName.MALEK_MINE_220040000).add(questId);		
		qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(700290).addOnKillEvent(questId);
		qe.setNpcQuestData(214026).addOnKillEvent(questId);
		for(int npc_id : npc_ids)
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() != QuestStatus.LOCKED || player.getCommonData().getLevel() < 39)
			return false;

		int[] quests = {2051, 2052, 2053, 2054, 2055, 2056, 2057, 2058, 2059, 2060};
		for (int id : quests)
		{
			QuestState qs2 = player.getQuestStateList().getQuestState(id);
			if (qs2 == null || qs2.getStatus() != QuestStatus.COMPLETE)
				return false;
		}
		qs.setStatus(QuestStatus.START);
		updateQuestStatus(player, qs);
		return true;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() != QuestStatus.START)
			return false;

		int var = qs.getQuestVarById(0);
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		switch(targetId)
			{				
				case 700290:
					if (qs.getQuestVarById(0) >=5 && qs.getQuestVarById(0) <8)
						{
						qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
						updateQuestStatus(player, qs);	
						}
					break;
				case 214026:
					if (qs.getQuestVarById(0) == 12)
						{
						qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
						updateQuestStatus(player, qs);	
						}
					break;		
			}		
		return false;
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

		if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 204052)
			{
				if(env.getDialogId() == -1)
					return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 10002);
				else return defaultQuestEndDialog(env);
			}
			return false;
		}
		else if(qs.getStatus() != QuestStatus.START)
		{
			return false;
		}
		if(targetId == 204702)
		{
			switch(env.getDialogId())
			{
				case 25:
					if(var == 0)
						return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 1011);
					else if(var == 2)
						return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 1693);	
					else if(var == 13)
						return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 2375);		
				case 1694:
					PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 255));
						break;			
				case 10000:
					if(var == 0)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(player, qs);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}	
				case 10002:
					if(var == 2)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(player, qs);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
				case 10255:
					if(var == 13)
					{
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(player, qs);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}	
			}
		}
		else if(targetId == 278001)
		{
			switch(env.getDialogId())
			{
				case 25:
					if(var == 1)
						return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 1352);	
				case 10001:
					if(var == 1)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(player, qs);
						ItemService.addItems(player, Collections.singletonList(new QuestItems(182204320, 1)));
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}		
			}
		}
		else if(targetId == 204807)
		{
			switch(env.getDialogId())
			{
				case 25:
					if(var == 3)
						return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 2034);						
				case 10003:
					if(var == 3)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(player, qs);						
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
			}
		}
		else if(targetId == 700295 && var >= 4 && var <12)
		{
			if (env.getDialogId() == -1)
			{
				if(var == 11)
				{
					player.getInventory().removeFromBagByItemId(182204320, 1);
				}
				final int targetObjectId = env.getVisibleObject().getObjectId();
				PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 1));
				PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0, targetObjectId), true);
				ThreadPoolManager.getInstance().schedule(new Runnable(){
					@Override
					public void run()
					{
						PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000, 0));
						PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0, targetObjectId), true);						
						qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
						updateQuestStatus(player, qs);
					}
				}, 3000);
			} return true;
		}		
		return false;
	}
}
