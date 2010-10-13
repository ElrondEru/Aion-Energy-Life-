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

import java.util.Collections;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.quest.QuestItems;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACCEPTED;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.ItemService;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Balthazar
 */
 
public class _3018ForkEarRokes extends QuestHandler
{
	private final static int	questId	= 3018;
	
	public _3018ForkEarRokes()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(730105).addOnQuestStart(questId);
		qe.setNpcQuestData(730105).addOnTalkEvent(questId);
		qe.setNpcQuestData(798150).addOnTalkEvent(questId);
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
			if(targetId == 730105)
			{
				switch(env.getDialogId())
				{
					case 25:
					{
						return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 4762);
					}
					case 10000:
					{
						QuestService.startQuest(env, QuestStatus.START);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(0, 0));
						return true;
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
				case 798150:
				{
					switch(env.getDialogId())
					{
						case 25:
						{
							long itemCount = player.getInventory().getItemCountByItemId(182208009);
							if(itemCount >= 1)
							{
								return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 5);
							}
						}
						case 17:
						{
							qs.setStatus(QuestStatus.COMPLETE);
							qs.setCompliteCount(1);
							player.getInventory().removeFromBagByItemId(182208009, 1);
							int rewardExp = player.getRates().getQuestXpRate() * 75500;
							int rewardKinah = player.getRates().getQuestXpRate() * 13200;
							ItemService.addItems(player, Collections.singletonList(new QuestItems(182400001, rewardKinah)));
							player.getCommonData().addExp(rewardExp);
							PacketSendUtility.sendPacket(player, new SM_QUEST_ACCEPTED(questId, QuestStatus.COMPLETE, 2));
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
							updateQuestStatus(player, qs);
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}