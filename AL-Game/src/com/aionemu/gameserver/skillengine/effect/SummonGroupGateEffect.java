/*
* This file is part of the requirements for the Illusion Gate Skill.
* Code References from ATracer's SummonTrapEffect.java of Aion-Unique
*/
package com.aionemu.gameserver.skillengine.effect;

import java.util.concurrent.Future;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.GroupGate;
import com.aionemu.gameserver.model.templates.spawn.SpawnTemplate;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
* @author LokiReborn
*
*/
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SummonGroupGateEffect")
public class SummonGroupGateEffect extends SummonEffect
{
    @XmlAttribute(name = "time", required = true)
    protected int   time;

    @Override
    public void applyEffect(Effect effect)
    {

	    Creature effector = effect.getEffector();
        SpawnEngine spawnEngine = SpawnEngine.getInstance();
        float x = effector.getX();
        float y = effector.getY();
        float z = effector.getZ();
        byte heading = effector.getHeading();
        int worldId = effector.getWorldId();
        int instanceId = effector.getInstanceId();

        SpawnTemplate spawn = spawnEngine.addNewSpawn(worldId, instanceId, npcId, x, y, z, heading, 0, 0, true, true);
        final GroupGate groupgate = spawnEngine.spawnGroupGate(spawn, instanceId, effector);

        Future<?> task = ThreadPoolManager.getInstance().schedule(new Runnable(){

	        @Override
            public void run()
            {
		
                groupgate.getController().onDespawn(true);
            }
        }, time * 1000);
       groupgate.getController().addTask(TaskId.DESPAWN, task);
    }
    @Override
    public void calculate(Effect effect)
    {
        super.calculate(effect);
    }
}