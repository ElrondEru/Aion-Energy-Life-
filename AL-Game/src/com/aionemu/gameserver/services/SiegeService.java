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

package com.aionemu.gameserver.services;

import java.util.List;

import javolution.util.FastMap;

import org.apache.log4j.Logger;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.main.SiegeConfig;
import com.aionemu.gameserver.dao.SiegeDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.siege.SiegeNpc;
import com.aionemu.gameserver.model.siege.Fortress;
import com.aionemu.gameserver.model.siege.Influence;
import com.aionemu.gameserver.model.siege.SiegeLocation;
import com.aionemu.gameserver.model.siege.SiegeRace;
import com.aionemu.gameserver.model.templates.spawn.SpawnGroup;
import com.aionemu.gameserver.model.templates.spawn.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SIEGE_LOCATION_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INFLUENCE_RATIO;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;

import java.util.Collection;

/**
 * @author Sarynth @Modified by Mcrizza
 * @Modified by Source , xTz
 */
public class SiegeService
{
	private static Logger	log	= Logger.getLogger(SiegeService.class);
	
	public static final SiegeService getInstance()
	{
		return SingletonHolder.instance;
	}

	private FastMap<Integer, SiegeLocation> locations;
	private long lastCalcTime;

	public SiegeService()
	{
		if (SiegeConfig.SIEGE_ENABLED)
		{
			log.info("Loading Siege Location Data...");
			
			locations = DataManager.SIEGE_LOCATION_DATA.getSiegeLocations();
			
			DAOManager.getDAO(SiegeDAO.class).loadSiegeLocations(locations);
			for(SiegeLocation loc : locations.values())
			{
				if (loc instanceof Fortress)
				{
					if (calculateVulnerable())
					{
						loc.setVulnerable(true);
						if (calculateVulnerable() && calculateVulnerable())
							loc.setNextState(Integer.valueOf(1));
						else
							loc.setNextState(Integer.valueOf(0));
					}
					else
					{
						loc.setVulnerable(false);
						if (calculateVulnerable())
							loc.setNextState(Integer.valueOf(1));
						else
							loc.setNextState(Integer.valueOf(0));
					}
				}
			}

			ThreadPoolManager.getInstance().schedule(new Runnable()
			{
				public void run()
				{
					for(SiegeLocation sLoc : locations.values())
						if (sLoc instanceof Fortress && sLoc.isVulnerable())
							sLoc.setVulnerable(false);

					mapUpdate();
				}
			}
			, SiegeConfig.SIEGE_INVUL_INTERVAL * 1000);

			lastCalcTime = System.currentTimeMillis();
			ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable()
			{
				public void run()
				{
					lastCalcTime = System.currentTimeMillis();
					log.debug("Processing vulnerability: " + locations.size() + " locations");

					for(SiegeLocation loc : locations.values())
					{
						if (loc instanceof Fortress)
						log.debug("Processing siege #" + loc.getLocationId());

						if (loc instanceof Fortress)
						{
							if (loc.isVulnerable())
							{
								if (calculateVulnerable())
									loc.setNextState(Integer.valueOf(1));
								else
									loc.setNextState(Integer.valueOf(0));
								loc.setVulnerable(false);
							}
							else if (loc.getNextState() == 1)
							{
								if (calculateVulnerable() && calculateVulnerable())
									loc.setNextState(Integer.valueOf(1));
								else
									loc.setNextState(Integer.valueOf(0));
								loc.setVulnerable(true);
							}
							else
							{
								if (calculateVulnerable())
									loc.setNextState(Integer.valueOf(1));
								else
									loc.setNextState(Integer.valueOf(0));
								loc.setVulnerable(false);
							}
						}
					}

					ThreadPoolManager.getInstance().schedule(new Runnable()
					{
						public void run()
						{
							for(SiegeLocation sLoc : locations.values())
								if (sLoc instanceof Fortress && sLoc.isVulnerable())
									sLoc.setVulnerable(false);

							mapUpdate();
						}
					}
					, SiegeConfig.SIEGE_INVUL_INTERVAL * 1000);

					mapUpdate();
				}
			}
			, SiegeConfig.SIEGE_TIMER_INTERVAL * 1000, SiegeConfig.SIEGE_TIMER_INTERVAL * 1000);
		}
		else
		{
			log.info("Siege Disabled by Config.");
			
			locations = new FastMap<Integer, SiegeLocation>();
		}

		log.info("SiegeService successfully started.");	
	}

	public FastMap<Integer, SiegeLocation> getSiegeLocations()
	{
		return locations;
	}

	/**
	 * @return siege time
	 */
	public int getSiegeTime()
	{
		long remainTime = getLastCalcTime() + SiegeConfig.SIEGE_TIMER_INTERVAL * 1000 - System.currentTimeMillis();
		long GlobalTimer = remainTime / 1000;
		return (int)GlobalTimer;
	}

	public long getLastCalcTime()
	{
		return lastCalcTime;
	}

	/**
	 * @return siege chance
	 */
	private boolean calculateVulnerable()
	{
		return (Rnd.get(100) < SiegeConfig.SIEGE_VULNERABLE_CHANCE);
	}

	/**
	 * @param locationId
	 * @return
	 */
	public SiegeLocation getSiegeLocation(int locationId)
	{
		return locations.get(locationId);
	}

	/**
	 * @param location id
	 * @param siege race
	 */
	public void capture(int locationId, SiegeRace race)
	{
		this.capture(locationId, race, 0);
	}

	/**
	 * @param location id
	 * @param siege race
	 * @param legion id
	 */
	public void capture(int locationId, SiegeRace race, int legionId)
	{
		//TODO: Convert all spawns to match new race.
		SiegeLocation sLoc = locations.get(locationId);
		sLoc.setRace(race);
		sLoc.setLegionId(legionId);
		
		if (sLoc instanceof Fortress)
			sLoc.setVulnerable(false);
		
		DAOManager.getDAO(SiegeDAO.class).updateSiegeLocation(sLoc);
		
		broadcastUpdate(sLoc);
		Influence.getInstance().recalculateInfluence();
		deSpawnLocation(locationId);	//despawning old siege spawns
		spawnLocation(locationId,race); //spawning new siege spawns
	}

	/**
	 * Spawns SiegeNpc by siegeId and raceId
	 */
	public void spawnLocation(int siegeId,SiegeRace race)
	{ 
		List<SpawnGroup> spawnsGroup = DataManager.SPAWNS_DATA.getSpawnsForWorld(400010000);
    List<SpawnGroup> spawnsGroup2 = DataManager.SPAWNS_DATA.getSpawnsForWorld(210050000);
    spawnsGroup.addAll(spawnsGroup2);
    List<SpawnGroup> spawnsGroup3 = DataManager.SPAWNS_DATA.getSpawnsForWorld(220070000);
    spawnsGroup.addAll(spawnsGroup3);
		for (SpawnGroup sg : spawnsGroup) 		
		{
			if ((sg.getSiegeId()==siegeId)&&(sg.getRace()==race))	//check on race to improve perfomance(less cycles processing)
			{
					List<SpawnTemplate> st = sg.getObjects();
					for (SpawnTemplate stnew : st)
						SpawnEngine.getInstance().spawnObject(stnew, 1);	//check on Race is in spawnegine,no need here
			}
		}
	}

	/**
	 * despawns SiegeNpcs after Siege been captured
	 */
	public void deSpawnLocation(int siegeId)
	{
		for(SiegeNpc siegeNpcToDespawn : World.getInstance().getSiegeNpcs())
		{
			if (siegeNpcToDespawn.getSiegeId()==siegeId ) 
				siegeNpcToDespawn.getController().onDespawn(true);
		}
	}

	/**
	 * @param loc
	 */
	public void broadcastUpdate(SiegeLocation loc)
	{
		SM_SIEGE_LOCATION_INFO pkt = new SM_SIEGE_LOCATION_INFO(loc);
		broadcast(pkt);
	}

	public void broadcastUpdate()
	{
		SM_SIEGE_LOCATION_INFO pkt = new SM_SIEGE_LOCATION_INFO();
		broadcast(pkt);
	}

	private void broadcast(SM_SIEGE_LOCATION_INFO pkt)
	{
		for(Player player : World.getInstance().getAllPlayers())
		{
			PacketSendUtility.sendPacket(player, pkt);
		}
	}

	public void mapUpdate()
	{
		Collection<Player> players = World.getInstance().getAllPlayers();
		for(Player player : World.getInstance().getAllPlayers())
		{
			PacketSendUtility.sendPacket(player, new SM_INFLUENCE_RATIO());
			PacketSendUtility.sendPacket(player, new SM_SIEGE_LOCATION_INFO());
		}
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final SiegeService instance = new SiegeService();
	}
}
