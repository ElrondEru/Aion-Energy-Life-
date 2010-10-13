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
package com.aionemu.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.TeleportService;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldPosition;

/**
 * @author Bio
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RecallInstantEffect")
public class RecallInstantEffect extends EffectTemplate
{

	@Override
	public void applyEffect(Effect effect)
	{
		final Creature effector = effect.getEffector();
		final Player effected = (Player) effect.getEffected();
		
		/** TODO effected cannot be in combat, also need to confirm if cannot be summoned while on abnormal effects
		  * stunned, sleeping, feared, etc.
		  * if(effected.isInCombat())
		  * {
		  * 	PacketSendUtility.sendPacket(effector, SM_SYSTEM_MESSAGE.RECALL_CANNOT_ACCEPT_EFFECT(effected.getName()));
		  *		return;
		  * }
		  */

		RequestResponseHandler rrh = new RequestResponseHandler(effector){
			@Override
			public void denyRequest(Creature effector, Player effected)
			{
				PacketSendUtility.sendPacket((Player) effector, SM_SYSTEM_MESSAGE.RECALL_REJECTED_EFFECT(effected.getName()));
				PacketSendUtility.sendPacket(effected, SM_SYSTEM_MESSAGE.RECALL_REJECT_EFFECT(effector.getName()));
			}

			@Override
			public void acceptRequest(Creature effector, Player effected)
			{
				TeleportService.teleportTo(effected, effector.getWorldId(), effector.getInstanceId(), effector.getX(), effector.getY(), effector.getZ(), effector.getHeading(), 0);
			}
		};

		effected.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_SUMMON_PARTY_DO_YOU_ACCEPT_REQUEST, rrh);
		PacketSendUtility.sendPacket(effected,
			new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_SUMMON_PARTY_DO_YOU_ACCEPT_REQUEST, 0, effector.getName(), "Summon Group Member", 30));
	}
	
	@Override
	public void calculate(Effect effect)
	{
		final Creature effector = effect.getEffector();
		final Creature effected = effect.getEffected();
		if(effected != null && effected instanceof Creature && effector.getWorldId() == effected.getWorldId() && !effector.isInInstance() && !(((Player) effector).isEnemyPlayer((Player) effected)) )
			effect.addSucessEffect(this);
	}
}