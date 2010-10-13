/*
 * This file is part of the requirements for the Illusion Gate Skill.
 * Code References from ATracer's Trap.java of Aion-Unique
 */
package com.aionemu.gameserver.model.gameobjects;

import com.aionemu.gameserver.controllers.NpcController;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.VisibleObjectTemplate;
import com.aionemu.gameserver.model.templates.spawn.SpawnTemplate;

/**
 * @author LokiReborn
 *
 */
public class GroupGate extends Npc
{

	/**
	 * Creator of this GroupGate.
	 */
	private Creature creator;
	
	/**
	 *
	 * @param objId
	 * @param controller
	 * @param spawnTemplate
	 * @param objectTemplate
	 */
	public GroupGate(int objId, NpcController controller, SpawnTemplate spawnTemplate, VisibleObjectTemplate objectTemplate)
	{
		super(objId, controller, spawnTemplate, objectTemplate);
	}

	/**
	 * @return the creator
	 */
	public Creature getCreator()
	{
		return creator;
	}

	/**
	 * @param creator the creator to set
	 */
	public void setCreator(Creature creator)
	{
		this.creator = creator;
	}
	
	@Override
	public byte getLevel()
	{
		return (1);
	}
	
	@Override
	protected boolean isEnemyNpc(Npc visibleObject)
	{
		return this.creator.isEnemyNpc(visibleObject);
	}

	@Override
	protected boolean isEnemyPlayer(Player visibleObject)
	{
		return this.creator.isEnemyPlayer(visibleObject);
	}
	
	/**
	 * @return NpcObjectType.GROUPGATE
	 */
	@Override
	public NpcObjectType getNpcObjectType()
	{
		return NpcObjectType.GROUPGATE;
	}

	@Override
	public Creature getActingCreature()
	{
		return this.creator;
	}

	@Override
	public Creature getMaster()
	{
		return this.creator;
	}
}