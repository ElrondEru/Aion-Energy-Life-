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

import javolution.util.FastMap;
import com.aionemu.gameserver.configs.main.SiegeConfig;
import com.aionemu.gameserver.ai.events.Event;
import com.aionemu.gameserver.controllers.attack.AggroInfo;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.siege.SiegeGeneral;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.legion.Legion;
import com.aionemu.gameserver.model.siege.SiegeLocation;
import com.aionemu.gameserver.model.siege.SiegeRace;
import com.aionemu.gameserver.model.siege.SiegeType;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_LOOKATOBJECT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import com.aionemu.gameserver.services.RespawnService;
import com.aionemu.gameserver.services.SiegeService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.services.ItemService;
import com.aionemu.gameserver.services.SystemMailService;

import java.util.ArrayList;

/**
 * @author ViAl
 * @modified by Source, xTz
 */
public class SiegeGeneralController extends SiegeNpcController
{
	@Override
	public SiegeGeneral getOwner()
	{
		return (SiegeGeneral) super.getOwner();
	}

	@Override
	public void onDie(Creature lastAttacker)
	{
		this.getOwner().setCasting(null);
		this.getOwner().getEffectController().removeAllEffects();
		this.getOwner().getMoveController().stop();
		this.getOwner().setState(CreatureState.DEAD);
		addTask(TaskId.DECAY, RespawnService.scheduleDecayTask(this.getOwner()));
		scheduleRespawn();
		PacketSendUtility.broadcastPacket(this.getOwner(),
				new SM_EMOTION(this.getOwner(), EmotionType.DIE, 0, lastAttacker == null ? 0 : lastAttacker.getObjectId()));
		this.getOwner().setTarget(null);
		PacketSendUtility.broadcastPacket(this.getOwner(), new SM_LOOKATOBJECT(this.getOwner()));
		//runnig capture and despawn all npcs for this fort/artefact
		this.doReward();
		super.onDelete();
	}

	@Override
	public void onAttack(Creature creature, int skillId, TYPE type, int damage)
	{
		SiegeLocation location = SiegeService.getInstance().getSiegeLocation(getOwner().getSiegeId());
		if (location.isVulnerable())
			super.onAttack(creature, skillId, type, damage);
		else
			return;
	}

	/**
	 * Capturing the fortress/artifact after SiegeNpc die
	 * @author MrPoke
	 */
	@Override
	public void doReward()
	{
		super.doReward();
		FastMap<Legion, Integer> legionByDmg = new FastMap<Legion, Integer>();
		ArrayList<Player> aDmg = new ArrayList<Player>(); 
		ArrayList<Player> eDmg = new ArrayList<Player>(); 
		int asmoDmg = 0;
		int elyosDmg = 0;
		for (AggroInfo ai : getOwner().getAggroList().getList())
		{
			if (!(ai.getAttacker() instanceof Creature))
				continue;

			// Check to see if this is a summon, if so add the damage to the group. 
			
			Creature master = ((Creature)ai.getAttacker()).getMaster();
			
			if (master == null)
				continue;

			if (master instanceof Player)
			{
				Player player = (Player)master;
				Legion legion = player.getLegion();

				if (legion != null)
				{
					int dmg = 0;
					if (legionByDmg.containsKey(legion))
						dmg = legionByDmg.get(legion);
					dmg += ai.getDamage();
					legionByDmg.put(legion, dmg);
				}
				else
				{
					if (player.getCommonData().getRace() == Race.ASMODIANS)
						asmoDmg += ai.getDamage();
					else
						elyosDmg += ai.getDamage();
				}
			}

			//Medal award system (block 1)
			Creature winer = (Creature)ai.getAttacker();
			if (winer instanceof Player)
			{
				if (((Player)winer).getCommonData().getRace() == Race.ASMODIANS)
					aDmg.add(((Player)winer));
				else
					eDmg.add(((Player)winer));
			}
		}

		int maxDmg = 0;
		Legion winnerLegion = null;
		if (!legionByDmg.isEmpty())
		{
			for(FastMap.Entry<Legion, Integer> e = legionByDmg.head(), end = legionByDmg.tail(); (e = e.getNext()) != end;)
			{
				if (maxDmg < e.getValue())
				{
					maxDmg = e.getValue();
					winnerLegion = e.getKey();
				}
			}
		}

		if (maxDmg <= asmoDmg && asmoDmg > elyosDmg)
		{
			winnerLegion = null;
			SiegeService.getInstance().capture(getOwner().getSiegeId(), SiegeRace.ASMODIANS , winnerLegion.getLegionId());
		}
		else if (maxDmg <= elyosDmg && asmoDmg < elyosDmg)
		{
			winnerLegion = null;
			SiegeService.getInstance().capture(getOwner().getSiegeId(), SiegeRace.ELYOS , winnerLegion.getLegionId());
		}

		if (winnerLegion != null)
		{
			Race race = winnerLegion.getOnlineLegionMembers().get(0).getCommonData().getRace();
			SiegeService.getInstance().capture(getOwner().getSiegeId(), race == Race.ASMODIANS ? SiegeRace.ASMODIANS : SiegeRace.ELYOS , winnerLegion.getLegionId());
		}

		//Medal award system (block 2)
		SiegeLocation loc = SiegeService.getInstance().getSiegeLocation(getOwner().getSiegeId());
		if (loc.getType() == SiegeType.FORTRESS)
		{
		    if (loc.getRace() == SiegeRace.ASMODIANS)
		    {
				for (Player aWin : aDmg)
				{
				   SystemMailService.getInstance().sendMail("SiegeService", aWin.getName(), "Siegereward", "SilverMedal", 186000031, SiegeConfig.SIEGE_SMEDAL_AMOUNT, 0, false);
				   SystemMailService.getInstance().sendMail("SiegeService", aWin.getName(), "Siegereward", "GoldMedal", 186000030, SiegeConfig.SIEGE_GMEDAL_AMOUNT, 0, false);
				   SystemMailService.getInstance().sendMail("SiegeService", aWin.getName(), "Siegereward", "PlatinumMedal", 186000096, SiegeConfig.SIEGE_PMEDAL_AMOUNT, 0, false);
		        }
			}
			else if (loc.getRace() == SiegeRace.ELYOS)
			{
				for (Player eWin : eDmg)
				{
				   SystemMailService.getInstance().sendMail("SiegeService", eWin.getName(), "Siegereward", "SilverMedal", 186000031, SiegeConfig.SIEGE_SMEDAL_AMOUNT, 0, false);
				   SystemMailService.getInstance().sendMail("SiegeService", eWin.getName(), "Siegereward", "GoldMedal", 186000030, SiegeConfig.SIEGE_GMEDAL_AMOUNT, 0, false);
				   SystemMailService.getInstance().sendMail("SiegeService", eWin.getName(), "Siegereward", "PlatinumMedal", 186000096, SiegeConfig.SIEGE_PMEDAL_AMOUNT, 0, false);
				}
			}
		}
	}
}
