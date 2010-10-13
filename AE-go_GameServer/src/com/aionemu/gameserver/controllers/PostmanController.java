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
package com.aionemu.gameserver.controllers;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author ViAl
 *
 */
public class PostmanController extends NpcController 
{
	
	private int RecipientId;	//ID of the express mail recipient, to prevent messenger using by non-recipient players
	
	public PostmanController(int PlayerId)
	{
		this.RecipientId = PlayerId;
		ThreadPoolManager.getInstance().schedule(new Runnable(){

			@Override
			public void run()
			{
					onDelete();
			}
		},5*60*1000); //Despawn postman after 5 minutes, need to place retail value.
	}
	
	@Override
	public void onDialogRequest(Player player)
	{
		if ( player.getObjectId() == RecipientId)
		{
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 18));
		}
		else
			return;
	}
	
	@Override
    public void onRespawn()
    {
         super.onRespawn();
    }

}