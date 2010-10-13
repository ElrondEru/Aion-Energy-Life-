/*
 Aion-Core by veigar
 */
package com.aionemu.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.controllers.movement.ActionObserver;
import com.aionemu.gameserver.controllers.movement.ActionObserver.ObserverType;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.Skill;

/**
 * @author Rama and Sippolo
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ChangeMpConsumptionEffect")
public class ChangeMpConsumptionEffect extends BufEffect
{
	@XmlAttribute
	protected boolean	percent;
	@XmlAttribute
	protected int	value;
	@Override
	public void startEffect(final Effect effect)
	{
		super.startEffect(effect);
		
		ActionObserver observer = new ActionObserver(ObserverType.SKILLUSE){

			@Override
			public void skilluse(Skill skill)
			{
				skill.setChangeMpConsumption(value);
			}
		};
		
		effect.getEffected().getObserveController().addObserver(observer);
		effect.setActionObserver(observer, position);
	}
	
	@Override
	public void endEffect(Effect effect)
	{
		super.endEffect(effect);
		ActionObserver observer = effect.getActionObserver(position);
		effect.getEffected().getObserveController().removeObserver(observer);
	}

	@Override
	public void calculate(Effect effect)
	{
		super.calculate(effect);
	}
}
