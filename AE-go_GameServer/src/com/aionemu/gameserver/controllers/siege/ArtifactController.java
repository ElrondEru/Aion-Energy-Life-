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
package com.aionemu.gameserver.controllers.siege;

import com.aionemu.gameserver.controllers.NpcController;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Mr. Poke
 *
 */
public class ArtifactController extends NpcController
{
	private int artifactId = 0;
	private Player activePlayer;

	@Override
	public void onDialogRequest(Player player)
	{
		if (artifactId == 0 || activePlayer != null)
			return;
		
		RequestResponseHandler responseHandler = new RequestResponseHandler(getOwner())
		{
			@Override
			public void acceptRequest(Creature requester, Player responder)
			{
			}
			
			@Override
			public void denyRequest(Creature requester, Player responder)
			{
				activePlayer = null;
			}
		};

		boolean requested = player.getResponseRequester().putRequest(160028, responseHandler);
		if (requested)
		{
			activePlayer = player;
			PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(160028, 0, 0));
		}
	}

	/**
	 * @return the artifactId
	 */
	public int getArtifactId()
	{
		return artifactId;
	}

	/**
	 * @param artifactId the artifactId to set
	 */
	public void setArtifactId(int artifactId)
	{
		this.artifactId = artifactId;
	}
}
