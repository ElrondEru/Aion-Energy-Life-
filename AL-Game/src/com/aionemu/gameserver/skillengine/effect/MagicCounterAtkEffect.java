/*
 Aion-core by vegar
 */
package com.aionemu.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.controllers.movement.ActionObserver;
import com.aionemu.gameserver.controllers.movement.ActionObserver.ObserverType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.stats.CreatureLifeStats;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.Skill;
import com.aionemu.gameserver.skillengine.model.SkillType;

/**
 * @author ViAl
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MagicCounterAtkEffect")
public class MagicCounterAtkEffect extends EffectTemplate
{
	@XmlAttribute
	protected int	percent;
	@XmlAttribute
	protected int	maxdmg;
	
	@Override
	public void calculate(Effect effect)
	{
		if (calculateEffectResistRate(effect, null)) 
			effect.addSucessEffect(this);
	}
	
	@Override
	public void applyEffect(Effect effect)
	{
		effect.addToEffectedController();
	}
	
	@Override
	public void startEffect(final Effect effect)
	{
		final Creature effector = effect.getEffector();
		final Creature effected = effect.getEffected();
		final CreatureLifeStats<? extends Creature> cls = effect.getEffected().getLifeStats();
		ActionObserver observer = null;
		
		observer = new ActionObserver(ObserverType.SKILLUSE)
			{
				@Override
				public void skilluse(Skill skill)
				{
					if (skill.getSkillTemplate().getType()==SkillType.MAGICAL)
					{
						if (cls.getMaxHp()/100*percent<=maxdmg)
							effected.getController().onAttack(effector, effect.getSkillId(), TYPE.DAMAGE, cls.getMaxHp()/100*percent);
						else
							effected.getController().onAttack(effector, maxdmg);
					}
						
				}
			};

		effect.setActionObserver(observer, position);
		effected.getObserveController().addObserver(observer);
	}
	
	@Override
	public void endEffect(Effect effect)
	{
		ActionObserver observer = effect.getActionObserver(position);
		if (observer != null)
			effect.getEffected().getObserveController().removeObserver(observer);
	}
}
