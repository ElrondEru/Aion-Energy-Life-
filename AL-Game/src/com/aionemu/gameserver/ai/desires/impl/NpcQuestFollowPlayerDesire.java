/*  
 *  This file is part of aion-unique <aion-unique.org>.
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
package com.aionemu.gameserver.ai.desires.impl;

import org.apache.log4j.Logger;

import com.aionemu.gameserver.ai.AI;
import com.aionemu.gameserver.ai.desires.AbstractDesire;
import com.aionemu.gameserver.ai.desires.MoveDesire;
import com.aionemu.gameserver.ai.state.AIState;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.services.PlayerService;


/**
 * @author Ascharot
 *
 */
public class NpcQuestFollowPlayerDesire extends AbstractDesire implements MoveDesire
{
	private Npc owner;
	private Player target;	
	private static final Logger log = Logger.getLogger(NpcQuestFollowPlayerDesire.class);
	/**
	 * @param crt 
	 * @param desirePower
	 */
	public NpcQuestFollowPlayerDesire(Npc owner, Player target, int desirePower)
	{
		super(desirePower);
		this.owner = owner;
		this.target = target;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean handleDesire(AI ai)
	{
		if (owner == null || owner.getLifeStats().isAlreadyDead())
		{
			if(owner != null)
			{
				if(owner.getQuestPlayer() != null)
					QuestEngine.getInstance().onNpcLostPlayer(new QuestEnv(owner, owner.getQuestPlayer(), 0 , 0));
				
				owner.setTarget(null);
				owner.setIsQuestBusy(false);
				owner.setQuestPlayer(null);
				owner.getController().resetZone();
			}
			return false;
		}
		
		if(target == null || target.getLifeStats().isAlreadyDead() || !target.getCommonData().isOnline() || target.getQuestFollowingNpc() != owner)
		{
			if(target.getCommonData().isOnline())
				if(owner.getQuestPlayer() != null)
					QuestEngine.getInstance().onNpcLostPlayer(new QuestEnv(owner, owner.getQuestPlayer(), 0 , 0));
			
			owner.setTarget(null);
			owner.setIsQuestBusy(false);
			owner.setQuestPlayer(null);
			owner.getController().resetZone();
			
			ai.setAiState(AIState.MOVINGTOHOME);
			return false;
		}
		
		owner.setTarget(target);
		owner.getMoveController().setFollowTarget(true);

		if(!owner.getMoveController().isScheduled())
			owner.getMoveController().schedule();
		
		//need move this in movecontroller ?
		owner.getController().onMove();
		
		//check distance to the desired target
		double distance = owner.getMoveController().getDistanceToTarget(target);
		
		if(distance > 150)
		{
			
			if(owner.getQuestPlayer() != null)
				QuestEngine.getInstance().onNpcLostPlayer(new QuestEnv(owner, owner.getQuestPlayer(), 0 , 0));

			owner.setTarget(null);
			owner.setIsQuestBusy(false);
			owner.setQuestPlayer(null);
			owner.getController().resetZone();
			
			ai.setAiState(AIState.MOVINGTOHOME);
			return false;
		}
		
		return true;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o)
			return true;
		if(!(o instanceof NpcQuestFollowPlayerDesire))
			return false;

		NpcQuestFollowPlayerDesire that = (NpcQuestFollowPlayerDesire) o;

		return target.equals(that.target);
	}

	/**
	 * @return the target
	 */
	public Creature getTarget()
	{	
		return target;
	}

	@Override
	public int getExecutionInterval()
	{
		return 1;
	}

	@Override
	public void onClear()
	{
		owner.getMoveController().stop();
	}
}
