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

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Balthazar 
 */
 
public class _1562CrossedDestiny extends QuestHandler
{
	private final static int	questId	= 1562;
	
	public _1562CrossedDestiny()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.setNpcQuestData(204589).addOnQuestStart(questId);
		qe.setNpcQuestData(204589).addOnTalkEvent(questId);
		qe.setNpcQuestData(204616).addOnTalkEvent(questId);		
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
			if(targetId == 204589)
			{			
				if(env.getDialogId() == 25)
					return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 4762);
				else
					return defaultQuestStartDialog(env);
			}
		}
		
		if(qs == null)
			return false;
			
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(targetId)
			{
				case 204616:
				{
					switch(env.getDialogId())
					{
						case -1:
						{
							if(qs.getQuestVarById(0) == 2)
							{
								Npc npc = (Npc)env.getVisibleObject();
								if (MathUtil.getDistance(1123, 1701, 110, npc.getX(), npc.getY(), npc.getZ()) > 5)
								{
									if(!npc.getMoveController().isScheduled())
										npc.getMoveController().schedule();
									npc.getMoveController().setFollowTarget(true);
									return true;
								}
								else
								{
									qs.setQuestVar(2);
									qs.setStatus(QuestStatus.REWARD);
									updateQuestStatus(player, qs);
									npc.getController().onDie(null);
									npc.getController().onDespawn(false);								
									return true;
								}
							}
						}
						case 25:
						{
							if(qs.getQuestVarById(0) == 0)
							{
								qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							}
							else if(qs.getQuestVarById(0) == 1)
							{
								return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 1352);
							}
						}
						case 10001:
						{
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(player, qs);
							Npc npc = (Npc)env.getVisibleObject();
							npc.getMoveController().setDistance(4);
							npc.getMoveController().setFollowTarget(true);
							npc.getMoveController().schedule();						
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
							return true;
						}
					}
				}
			}
		}
		else if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 204589)
			{
				if(env.getDialogId() == 1009)
					return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 10002);
				else return defaultQuestEndDialog(env);
			}
			return false;
		}
		return false;
	}
}