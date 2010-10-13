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
import com.aionemu.gameserver.utils.stats.AbyssRankEnum;

/**
 * @author Rhys2002, zdead, LokiReborn
 */
public class SM_ABYSS_RANKING_PLAYERS extends AionServerPacket
{
	
	private ArrayList<AbyssRankingResult> 	data;
	private int 							race;
	
	public SM_ABYSS_RANKING_PLAYERS(ArrayList<AbyssRankingResult> data, Race race)
	{
		this.data = data;
		this.race = race.getRaceId();
	}

	@Override	
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeD(buf, race);// 0:Elyos 1:Asmo
		writeD(buf, Math.round(System.currentTimeMillis() / 1000));// Date
		writeD(buf, 0x01);
		writeD(buf, 0x01);// 0:Nothing 1:Update Table
		writeH(buf, data.size());// list size
		
		int counter = 0;
		for (AbyssRankingResult rs : data)
		{
			counter++;
			writeD(buf, counter);// Current Rank
			writeD(buf, AbyssRankEnum.getRankForAp(rs.getPlayerAP()).getId());// Title
			writeD(buf, rs.getPlayerProgression());// Old Rank, TODO: build history table and schedule hourly refresh
			writeD(buf, rs.getPlayerId()); // PlayerID
			writeD(buf, race);
			writeD(buf, rs.getPlayerClass().getClassId());// Class Id
			writeC(buf, 0); // Sex ? 0=male / 1=female
			writeC(buf, 0); // Unk
			writeH(buf, 0); // Unk
			writeD(buf, rs.getPlayerAP());// Abyss Points
			writeD(buf, 0); // Unk
			writeH(buf, rs.getPlayerLevel());
			
			writeS(buf, rs.getPlayerName());// Player Name
		    
			for (int size = 0; size < (30 - rs.getPlayerName().length()*2); size++)
			{
				writeC(buf, 0x00);
			}
			writeH(buf, 0x00);
			writeD(buf, 0x00);
			writeD(buf, 0x00);
			writeS(buf, rs.getLegionName());// Legion Name
			   
			for (int size = 0; size < (62 - rs.getLegionName().length()*2); size++)
			{
				writeC(buf, 0x00);
			}
			writeH(buf, 0x00);
		}
	}
}
