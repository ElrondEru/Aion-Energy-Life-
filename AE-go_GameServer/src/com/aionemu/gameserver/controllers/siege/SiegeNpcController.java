package com.aionemu.gameserver.controllers.siege;

import com.aionemu.gameserver.controllers.NpcController;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.siege.SiegeNpc;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;

/**
 * 
 * @author ViAl
 * @Modified xTz & Source (2010-09-20)
 */
public class SiegeNpcController extends NpcController 
{

	@Override
	public SiegeNpc getOwner()
	{
		return (SiegeNpc) super.getOwner();
	}
	
	@Override
	public void onDie(Creature lastAttacker)
	{
		super.onDie(lastAttacker);
	}
	
	@Override
	public void onAttack(Creature creature, int skillId, TYPE type, int damage)
	{
		super.onAttack(creature, skillId, type, damage);
	}
	
}
