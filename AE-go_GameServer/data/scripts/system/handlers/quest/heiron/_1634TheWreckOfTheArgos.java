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
import com.aionemu.gameserver.services.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;

import org.apache.log4j.Logger;

/**
 * @author Balthazar
 */
 
public class _1634TheWreckOfTheArgos extends QuestHandler
{
	private final static int	questId	= 1634;
	
	private static final Logger log = Logger.getLogger(_1634TheWreckOfTheArgos.class);
	
	public _1634TheWreckOfTheArgos()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(204547).addOnQuestStart(questId);
		qe.setNpcQuestData(204547).addOnTalkEvent(questId);
		qe.setNpcQuestData(204540).addOnTalkEvent(questId);
		qe.setNpcQuestData(790018).addOnTalkEvent(questId);
		qe.setNpcQuestData(204541).addOnTalkEvent(questId);
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
			if(targetId == 204547)
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
				case 204547:
				{
					switch(env.getDialogId())
					{
						case 25:
						{
							long itemCount1 = player.getInventory().getItemCountByItemId(182201760);
							if(qs.getQuestVarById(0) == 0 && itemCount1 >= 3)
							{
								return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 1011);
							}
						}
						case 4763:
						{
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(player, qs);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
							return true;
						}
					}
				}
				case 204540:
				{
					switch(env.getDialogId())
					{
						case 25:
						{
							return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 1693);
						}
						case 1694:
						{
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							player.getInventory().removeFromBagByItemId(182201760, 1);
							updateQuestStatus(player, qs);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
							return true;
						}
					}
				}
				case 790018:
				{
					switch(env.getDialogId())
					{
						case 25:
						{
							return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 2034);
						}
						case 2035:
						{
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							player.getInventory().removeFromBagByItemId(182201760, 1);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(player, qs);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
							return true;
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 204541)
			{
				switch(env.getDialogId())
				{
					case 25:
					{
						return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 2375);
					}
					case 1009:
					{
						return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 5);
					}
					case 17:
					{
						player.getInventory().removeFromBagByItemId(182201760, 1);
					}
					default: return defaultQuestEndDialog(env);
				}
			}
		}	
		return false;
	}
}