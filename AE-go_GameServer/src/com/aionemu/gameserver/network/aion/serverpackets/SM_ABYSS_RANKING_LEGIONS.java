/*
 * This file is part of aion-unique <aion-unique.smfnew.com>.
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
package com.aionemu.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import com.aionemu.gameserver.model.AbyssRankingResult;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author zdead, LokiReborn
 */
public class SM_ABYSS_RANKING_LEGIONS extends AionServerPacket
{
	
	private ArrayList<AbyssRankingResult> data;
	private Race race;
	
	public SM_ABYSS_RANKING_LEGIONS(ArrayList<AbyssRankingResult> data, Race race)
	{
		this.data = data;
		this.race = race;
	}

	@Override	
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, race.getRaceId());// 0:Elyos 1:Asmo
		writeD(buf, (int)(System.currentTimeMillis() / 1000));// Date
		writeD(buf, 0x01);// 0:Nothing 1:Update Table
		writeD(buf, 0x01);// 0:Nothing 1:Update Table
		writeH(buf, data.size());// list size
		int counter = 0;
		for (AbyssRankingResult rs : data)
		{
			counter++;
			writeD(buf, counter);// Current Rank
			writeD(buf, 0);// Old Rank
			writeD(buf, rs.getLegionId());// Legion Id
			writeD(buf, race.getRaceId());// 0:Elyos 1:Asmo
			writeC(buf, rs.getLegionLevel());// Legion Level
			writeD(buf, rs.getLegionMembers());// Legion Members
			writeQ(buf, rs.getLegionCP());// Contribution Points

			writeS(buf, rs.getLegionName());// Legion Name
	
			for (int size = 0; size < (62 - rs.getLegionName().length()*2); size++)
			{
				writeC(buf, 0x00);
			}
			writeH(buf, 0x00);
	}	
	}
}
