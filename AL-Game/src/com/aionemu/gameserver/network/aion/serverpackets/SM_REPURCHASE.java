/*
 *  This file is part of Zetta-Core Engine <http://www.zetta-core.org>.
 *
 *  Zetta-Core is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published
 *  by the Free Software Foundation, either version 3 of the License,
 *  or (at your option) any later version.
 *
 *  Zetta-Core is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a  copy  of the GNU General Public License
 *  along with Zetta-Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author ginho1
 */
public class SM_REPURCHASE extends AionServerPacket
{
	private int targetObjectId;

	public SM_REPURCHASE(Npc npc, Player player)
	{
		targetObjectId = npc.getObjectId();
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
	    	writeD(buf, targetObjectId);
		writeD(buf, 0);
		//writeH(buf, items.size());
		writeH(buf, 0);

		/*for(Item item : items)
		{
			writeD(buf, item.getObjectId());
			writeD(buf, item.getItemTemplate().getTemplateId());
		}*/
	}
}