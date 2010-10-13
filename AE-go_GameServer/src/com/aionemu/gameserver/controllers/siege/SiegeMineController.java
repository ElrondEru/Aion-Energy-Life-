/*
 * This file is part of aion-lightning <aion-lightning.com>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.controllers.siege;

import com.aionemu.gameserver.ai.events.Event;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.siege.SiegeMine;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_LOOKATOBJECT;
import com.aionemu.gameserver.services.RespawnService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author ViAl
 *
 */
public class SiegeMineController extends SiegeNpcController
{

	public void see(VisibleObject object)
	{
		SiegeMine owner = getOwner();
		if (object instanceof Player)
		{
			if ( ((Player) object).getCommonData().getRace().getRaceId()!=owner.getObjectTemplate().getRace().getRaceId())
			{
				//super.useSkill(owner.getNpcSkillList().getNpcSkills().get(0).getSkillid()); //all mines have only one skill
				useSkill(18406);
				owner.setCasting(null);
				owner.getEffectController().removeAllEffects();
				owner.getMoveController().stop();
				owner.setState(CreatureState.DEAD);
				addTask(TaskId.DECAY, RespawnService.scheduleDecayTask(this.getOwner()));
				scheduleRespawn();
				PacketSendUtility.broadcastPacket(owner,
					new SM_EMOTION(owner, EmotionType.DIE, 0, object == null ? 0 : object.getObjectId()));
				owner.setTarget(null);
				PacketSendUtility.broadcastPacket(owner, new SM_LOOKATOBJECT(owner));
			}
		}
	}
	
	@Override
	public SiegeMine getOwner()
	{
		return (SiegeMine) super.getOwner();
	}
	
}
