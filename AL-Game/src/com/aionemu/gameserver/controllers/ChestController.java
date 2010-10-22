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
package com.aionemu.gameserver.controllers;

import org.apache.log4j.Logger;
import java.util.List;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.Storage;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.gameobjects.stats.StatEnum;
import com.aionemu.gameserver.model.templates.chest.ChestTemplate;
import com.aionemu.gameserver.model.templates.chest.KeyItem;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.services.ItemService;

/**
 * @author Wakizashi
 * 
 */
public class ChestController extends NpcController
{
	ChestTemplate chestTemplate;

	@Override
	public void setOwner(Creature owner)
	{
		super.setOwner(owner);
		chestTemplate = DataManager.CHEST_DATA.getChestTemplate(owner.getObjectTemplate().getTemplateId());
	}

	@Override
	public void onDialogRequest(final Player player)
	{
		if(chestTemplate == null)
			return;

		final int defaultUseTime = 3000;
		PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getOwner().getObjectId(),
			defaultUseTime, 1));
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_QUESTLOOT, 0, getOwner().getObjectId()), true);

		ThreadPoolManager.getInstance().schedule(new Runnable(){
			@Override
			public void run()
			{
				PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getOwner().getObjectId(),
					defaultUseTime, 0));
				PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.END_QUESTLOOT, 0, getOwner().getObjectId()), true);
				
				analyzeOpening(player);
			}
			
			/**
			 * @param player
			 */
			private void analyzeOpening(final Player player)
			{
				List<KeyItem> keyItems = chestTemplate.getKeyItem();
				for(KeyItem keyItem : keyItems)
				{
					if(player.getInventory().removeFromBagByItemId(keyItem.getItemId(), keyItem.getQuantity()))
					{
						return;
					}
				}
				onAttack(player, getOwner().getLifeStats().getMaxHp() + 1);
			}
		}, defaultUseTime);

	}
}
