/*
 * This file is part of aion gates <aion-gates.fr>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion gates is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion gates.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Equipment;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.serverpackets.SM_UPDATE_PLAYER_APPEARANCE;
import com.aionemu.gameserver.restrictions.RestrictionsManager;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.model.gameobjects.stats.id.SkillEffectId;

/**
 * 
 * @author Avol modified by ATracer
 */
public class CM_EQUIP_ITEM extends AionClientPacket
{
	public int	slotRead;
	public int	itemUniqueId;
	public int	action;
	
	public CM_EQUIP_ITEM(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		action = readC(); // 0/1 = equip/unequip
		slotRead = readD();
		itemUniqueId = readD();
	}

	@Override
	protected void runImpl()
	{
		final Player activePlayer = getConnection().getActivePlayer();
		Equipment equipment = activePlayer.getEquipment();
		Item resultItem = null;

		if(!RestrictionsManager.canChangeEquip(activePlayer))
			return;
		
		switch(action)
		{
			case 0:
				resultItem = equipment.equipItem(itemUniqueId, slotRead);
				break;
			case 1:
				resultItem = equipment.unEquipItem(itemUniqueId, slotRead);
				break;
			case 2:
				equipment.switchHands();
				break;
		}

		if(resultItem != null || action == 2)
		{
			PacketSendUtility.broadcastPacket(activePlayer, new SM_UPDATE_PLAYER_APPEARANCE(activePlayer.getObjectId(),
				equipment.getEquippedItemsWithoutStigma()), true);		
		}
	    
		if ( (!(equipment.isShieldEquipped())) )
		{
			// Shield Defense I
			if ( (activePlayer.getGameStats().effectAlreadyAdded(SkillEffectId.getInstance(173, 101731, 1)))
					|| (activePlayer.getGameStats().effectAlreadyAdded(SkillEffectId.getInstance(173, 101732, 2)))
					|| (activePlayer.getGameStats().effectAlreadyAdded(SkillEffectId.getInstance(173, 101734, 3))) )
			{
				activePlayer.getEffectController().removeNoshowEffect(173);
			}
			// Steel Wall Defense I
			if ( (activePlayer.getGameStats().effectAlreadyAdded(SkillEffectId.getInstance(524, 105241, 1)))
					|| (activePlayer.getGameStats().effectAlreadyAdded(SkillEffectId.getInstance(524, 101732, 2)))
					|| (activePlayer.getGameStats().effectAlreadyAdded(SkillEffectId.getInstance(524, 101733, 3))) 
					|| (activePlayer.getGameStats().effectAlreadyAdded(SkillEffectId.getInstance(524, 101734, 4))) )
			{
				activePlayer.getEffectController().removeNoshowEffect(524);
			}
			// Shield Of Faith I
			activePlayer.getEffectController().removeEffect(413);
		}
 	}
 }