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
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Balthazar
 */
 
public class _3013AClueLeftByTheDead extends QuestHandler
{
    private final static int    questId    = 3013;
	
    public _3013AClueLeftByTheDead()
    {
        super(questId);
    }
    
    @Override
    public void register()
    {
        qe.setNpcQuestData(798132).addOnQuestStart(questId);
        qe.setNpcQuestData(798132).addOnTalkEvent(questId);
        qe.setNpcQuestData(798146).addOnTalkEvent(questId);
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
			if(targetId == 798132)
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
				case 798132:
				{
					switch(env.getDialogId())
					{
						case 25:
						{
							if(qs.getQuestVarById(0) == 0)
							{
								long itemCount = player.getInventory().getItemCountByItemId(182208008);
								if(itemCount >= 1)
								{
									return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 1011);
								}
								else return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 10001);
							}
						}
						case 33:
						{
							player.getInventory().removeFromBagByItemId(182208008, 1);
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(player, qs);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
					}
				}
				case 798146 :
				{
					switch(env.getDialogId())
					{
						case 25:
						{
							if(qs.getQuestVarById(0) == 1)
							{
								return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 1352);
							}
						}
						case 10255:
						{
							updateQuestStatus(player, qs);
							qs.setStatus(QuestStatus.REWARD);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 798132)
			{
				switch(env.getDialogId())
				{
					case 25:
					{
						return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 10002);
					}
					case 1009:
					{
						return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 5);
					}
					default: return defaultQuestEndDialog(env);
				}
			}
		}
		return false;
    }
}