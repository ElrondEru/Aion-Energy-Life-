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

package quest.theobomos;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.ItemService;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Balthazar
 */
 
public class _3050RescuingRuria extends QuestHandler
{
	private final static int	questId	= 3050;
	
	public _3050RescuingRuria()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(798211).addOnQuestStart(questId);
		qe.setNpcQuestData(798211).addOnTalkEvent(questId);
		qe.setNpcQuestData(798208).addOnTalkEvent(questId);
		qe.setNpcQuestData(798190).addOnTalkEvent(questId);
		qe.setQuestMovieEndIds(370).add(questId);
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
			if(targetId == 798211)
			{
				switch(env.getDialogId())
				{
					case 25:
					{
						return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 4762);
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
				case 798211:
				{
					switch(env.getDialogId())
					{
						case -1:
						{
							if(qs.getQuestVarById(0) == 2)
							{
								Npc npc = (Npc)env.getVisibleObject();
								if (MathUtil.getDistance(471, 2101, 54, npc.getX(), npc.getY(), npc.getZ()) > 10)
								{
									if(!npc.getMoveController().isScheduled())
										npc.getMoveController().schedule();
									npc.getMoveController().setFollowTarget(true);
									return true;
								}
								else
								{
									qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
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
								long itemCount = player.getInventory().getItemCountByItemId(182208035);
								if(itemCount >= 1)
								{
									return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 1011);
								}
								else return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 1097);
							}
						}
						case 1012:
						{
						player.getInventory().removeFromBagByItemId(182208035, 1);
						}
						case 10000:
						{
							PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, 370));
							Npc npc = (Npc)env.getVisibleObject();
							npc.getMoveController().setDistance(4);
							npc.getMoveController().setSpeed(6);
							npc.getMoveController().schedule();
							npc.getMoveController().setFollowTarget(true);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
							return true;
						}
					}
				}
				case 798208:
				{
					switch(env.getDialogId())
					{
						case 25:
						{
							return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 2034);
						}
						case 10255:
						{
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(player, qs);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 798190)
			{
				if(env.getDialogId() == 1009)
					return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 5);
				else return defaultQuestEndDialog(env);
			}
		}
		return false;
	}
	
	@Override
	public boolean onMovieEndEvent(QuestEnv env, int movieId)
	{
		if(movieId != 370)
			return false;
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() != QuestStatus.START)
			return false;
		
		qs.setQuestVar(2);
		updateQuestStatus(player, qs);
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
		return true;
	}
}