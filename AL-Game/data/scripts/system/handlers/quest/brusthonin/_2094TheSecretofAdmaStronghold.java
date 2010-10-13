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
package quest.brusthonin;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Dune11
 * 
 */
public class _2094TheSecretofAdmaStronghold extends QuestHandler
{
	
	private final static int	questId	= 2094;
	private final static int[]	npc_ids	= { 205150, 205192, 205155, 730164, 205191, 204057 };

	public _2094TheSecretofAdmaStronghold()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);	
		qe.setNpcQuestData(214700).addOnKillEvent(questId);	
		for(int npc_id : npc_ids)
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);	
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		boolean lvlCheck = QuestService.checkLevelRequirement(questId, player.getCommonData().getLevel());
		
		if(qs == null || qs.getStatus() != QuestStatus.LOCKED || !lvlCheck)
		{
			return false;
		}

		int[] quests = {2092, 2093, 2054};
		for(int id : quests)
		{
			QuestState qs2 = player.getQuestStateList().getQuestState(id);
			if(qs2 == null || qs2.getStatus() != QuestStatus.COMPLETE)
				return false;	
		}
		
		
		qs.setStatus(QuestStatus.START);
		updateQuestStatus(player, qs);
		return true;
	}

	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final Npc npc = (Npc)env.getVisibleObject();
		
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 204057)
				return defaultQuestEndDialog(env);
			return false;
		}
		else if(qs.getStatus() != QuestStatus.START)
		{
			return false;
		}
		if(targetId == 205150)
		{
			switch(env.getDialogId())
			{
				case 25:
					if(var == 0)
						return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 1011);	
					return true;
				case 10000:
					if (var == 0)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(player, qs);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}			
			}			
		}
		else if(targetId == 205192)
		{
			switch(env.getDialogId())
			{
				case 25:
					if(var == 1)
						return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 1352);
					else if(var == 2)
						return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 1693);
					else if(var == 3)
						return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 2034);
					return true;
				case 10001:
					if (var == 1)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(player, qs);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}							
				case 33:
					if (var==2)
					{
						if(QuestService.collectItemCheck(env, true))
						{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(player, qs);
						return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 10001);
						}
						else return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 10008);
					}							
				case 10003:
					if (var==3)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(player, qs);
					}		
			}			
		}
		else if(targetId == 205155)
		{
			switch(env.getDialogId())
			{
				case 25:
					if(var == 5)
						return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 2716);
				case 10005:
					if(var == 5)
					{
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(player, qs);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}						
			}
		}
		else if(targetId == 730164)
		{
			switch(env.getDialogId())
			{
				case -1:
					if(var == 6)
					{
						QuestService.addNewSpawn(220050000, 1, 205191, (float) npc.getX(),
										(float) npc.getY(), (float) npc.getZ(), (byte) 0, true);
						npc.getController().onDespawn(true);
						npc.getController().scheduleRespawn();	
						return true;
					}						
			}
		}
		else if(targetId == 205191)
		{
			switch(env.getDialogId())
			{
				case -1:
					if(var == 6)
					{
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(player, qs);
						return true;
					}				
			}
		}
		return false;
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

		if(targetId == 214700)
		{
			if(var == 4)
			{
				qs.setQuestVarById(0, var + 1);
				updateQuestStatus(player, qs);
				return true;
			}
		}
		return false;
	}

}
