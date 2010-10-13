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
package com.aionemu.gameserver.ai.state.handler;

import com.aionemu.gameserver.ai.AI;
import com.aionemu.gameserver.ai.desires.impl.NpcQuestFollowPlayerDesire;
import com.aionemu.gameserver.ai.state.AIState;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.gameobjects.stats.StatEnum;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_LOOKATOBJECT;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Ascharot
 *
 */
public class QuestFollowingStateHandler extends StateHandler
{
	@Override
	public AIState getState()
	{
		return AIState.QUEST_FOLLOW_PLAYER;
	}

	/**
	 * State ATTACKING
	 * AI MonsterAi
	 * AI AggressiveAi
	 */
	@Override
	public void handleState(AIState state, AI<?> ai)
	{
		ai.clearDesires();
		Npc owner = ((Npc)ai.getOwner());
		VisibleObject target = owner.getTarget();
		if(target == null)
		{
			ai.setAiState(AIState.MOVINGTOHOME);
			owner.setQuestPlayer(null);
			return;
		}
		
		//need to start refreshing zones during follow
		owner.getController().refreshZoneImpl();
		
		owner.setQuestPlayer((Player)target);
		owner.setIsQuestBusy(true);
		
		PacketSendUtility.broadcastPacket(owner, new SM_LOOKATOBJECT(owner));

		owner.setState(CreatureState.WALKING);
		PacketSendUtility.broadcastPacket(owner,
			new SM_EMOTION(owner, EmotionType.RUN, 0, target.getObjectId()));
		
		if (owner.getGameStats().getCurrentStat(StatEnum.SPEED) != 0)
			ai.addDesire(new NpcQuestFollowPlayerDesire(owner, (Player)target, AIState.QUEST_FOLLOW_PLAYER.getPriority()));

		ai.schedule();
	}
}
