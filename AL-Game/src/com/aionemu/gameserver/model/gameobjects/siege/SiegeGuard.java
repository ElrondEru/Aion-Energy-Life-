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
package com.aionemu.gameserver.model.gameobjects.siege;

import com.aionemu.gameserver.controllers.siege.SiegeGuardController;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.siege.SiegeRace;
import com.aionemu.gameserver.model.templates.NpcTemplate;
import com.aionemu.gameserver.model.templates.VisibleObjectTemplate;
import com.aionemu.gameserver.model.templates.spawn.SpawnTemplate;

/**
 * @author ViAl
 * @Modified antness
 *
 */
public class SiegeGuard extends SiegeNpc
{

	/**
	 * @param objId
	 * @param controller
	 * @param spawnTemplate
	 * @param objectTemplate
	 */
	public SiegeGuard(int objId, SiegeGuardController controller, SpawnTemplate spawnTemplate,
		VisibleObjectTemplate objectTemplate)
	{
		super(objId, controller, spawnTemplate, objectTemplate);
		NpcTemplate npcTemplate = (NpcTemplate)objectTemplate;
		if (getSiegeRace()==SiegeRace.ELYOS)
		{
			npcTemplate.setRace(Race.ELYOS);
			npcTemplate.setTribe("GUARD");
		}
		else if (getSiegeRace()==SiegeRace.ASMODIANS)
		{
			npcTemplate.setRace(Race.ASMODIANS);
			npcTemplate.setTribe("GUARD_DARK");
		}
		else // BALAUR
		{
			npcTemplate.setRace(Race.DRAKAN);
			npcTemplate.setTribe("AGGRESSIVESINGLEMONSTER");
		}
	}
	
	@Override
	public SiegeGuardController getController()
	{
		return (SiegeGuardController) super.getController();
	}
	


}
