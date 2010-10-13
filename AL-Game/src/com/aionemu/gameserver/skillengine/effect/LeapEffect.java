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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_MOVE;
import com.aionemu.gameserver.skillengine.action.DamageType;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;

/**
 * @author Bio
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LeapEffect")
public class LeapEffect extends EffectTemplate
{
	@XmlAttribute(name = "distance")
	private float distance;
	@XmlAttribute(name = "direction")
	private float direction;
	@XmlAttribute(name = "up")
	private float up;

	
	@Override
	public void applyEffect(Effect effect)
	{
		final Player effector = (Player)effect.getEffector();
		
		// Move Effector backwards direction=1 or frontwards direction=0
		double radian = Math.toRadians(MathUtil.convertHeadingToDegree(effector.getHeading()));
		float x1 = (float)(Math.cos(Math.PI * direction + radian) * distance);
		float y1 = (float)(Math.sin(Math.PI * direction + radian) * distance);
		World.getInstance().updatePosition(
			effector,
			effector.getX() + x1,
			effector.getY() + y1,
			effector.getZ() + up,
			effector.getHeading());
		
		PacketSendUtility.sendPacket(effector,
			new SM_PLAYER_MOVE(
				effector.getX(),
				effector.getY(),
				effector.getZ(),
				effector.getHeading()
			)
		);
	}
	
	@Override
	public void calculate(Effect effect)
	{
		effect.addSucessEffect(this);
	}
}