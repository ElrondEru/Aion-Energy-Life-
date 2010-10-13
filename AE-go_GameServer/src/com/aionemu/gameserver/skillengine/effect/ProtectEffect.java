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
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.AttackType;

/**
 * @author Sippolo
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProtectEffect")
public class ProtectEffect extends BufEffect
{
	@XmlAttribute
	protected boolean	percent;
	@XmlAttribute
	protected int	value;
	@XmlAttribute
	protected int	range;
	@XmlAttribute
	protected AttackType	attacktype;
	@Override
	public void startEffect(final Effect effect)
	{
		super.startEffect(effect);
		
		Creature effector = effect.getEffector();
		Creature effected = effect.getEffected();
		effected.getController().setProtectState(effector,value,range,attacktype);
	}
	
	@Override
	public void endEffect(Effect effect)
	{
		super.endEffect(effect);
		Creature effected = effect.getEffected();
		effected.getController().removeProtectState();
	}

	@Override
	public void calculate(Effect effect)
	{
		super.calculate(effect);
	}
}