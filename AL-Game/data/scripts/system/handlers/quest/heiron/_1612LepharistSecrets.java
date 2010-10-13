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
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author Balthazar
 */
 
public class _1612LepharistSecrets extends QuestHandler
{
	private final static int	questId	= 1612;
	
	public _1612LepharistSecrets()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(204530).addOnQuestStart(questId);
		qe.setNpcQuestData(204530).addOnTalkEvent(questId);
		qe.setNpcQuestData(700352).addOnTalkEvent(questId);

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
			if(targetId == 204530)
			{
				if(env.getDialogId() == 25)
				{
					return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 4762);
				}
				else return defaultQuestStartDialog(env);
			}
		}
		
		if(qs == null)
			return false;
			
		if(qs.getStatus() == QuestStatus.START)
		{
			switch(targetId)
			{
				case 700352:
				{
					switch(env.getDialogId())
					{
						case -1:
						{
							switch(qs.getQuestVarById(0))
							{
								case 0:
								case 1:
								case 2:
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
											qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
											updateQuestStatus(player, qs);
											PacketSendUtility.broadcastPacket(player.getTarget(), new SM_EMOTION((Creature)player.getTarget(), EmotionType.DIE, 128, 0));
										}
									}, 3000);
									return true;
								}
								case 3:
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
											qs.setStatus(QuestStatus.REWARD);
											updateQuestStatus(player, qs);
											PacketSendUtility.broadcastPacket(player.getTarget(), new SM_EMOTION((Creature)player.getTarget(), EmotionType.DIE, 128, 0));
										}
									}, 3000);
									return true;
								}
							}
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 204530)
			{
				if(env.getDialogId() == 1009)
					return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 5);
				else return defaultQuestEndDialog(env);
			}
		}
		return false;
	}
}