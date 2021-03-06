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
package quest.eltnen;

import java.util.Collections;

import org.apache.log4j.Logger;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.quest.QuestItems;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Balthazar
 */
 
public class _1393NewFlightPath extends QuestHandler
{
	private final static int	questId	= 1393;
	
	private static final Logger log = Logger.getLogger(_1393NewFlightPath.class);
	
	public _1393NewFlightPath()
	{
		super(questId);
	}

	@Override
	public void register()
	{
		qe.setNpcQuestData(204041).addOnQuestStart(questId);
		qe.setNpcQuestData(204041).addOnTalkEvent(questId);
		qe.setQuestEnterZone(ZoneName.LEPHARIST_CITADEL_210020000).add(questId);
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
			if(targetId == 204041)
			{
				switch(env.getDialogId())
				{
					case 25:
					{
						return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 1011);
					}
					default : return defaultQuestStartDialog(env);
				}
			}
		}
		if(qs == null)
			return false;
			
		if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 204041)
			{
				switch(env.getDialogId())
				{
					case 1009:
					{
						return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 5);
					}
					case 17:
					{
						qs.setQuestVar(1);
						qs.setStatus(QuestStatus.COMPLETE);
						updateQuestStatus(player, qs);
						return true;
					}
					default: return defaultQuestEndDialog(env);
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName)
	{
		if(zoneName != ZoneName.LEPHARIST_CITADEL_210020000)
			return false;

		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if(qs == null)
			return false;
			
		if (qs.getQuestVarById(0) == 0)
		{
			qs.setStatus(QuestStatus.REWARD);
			updateQuestStatus(player, qs);
			return true;
		}
		return false;
	}
}