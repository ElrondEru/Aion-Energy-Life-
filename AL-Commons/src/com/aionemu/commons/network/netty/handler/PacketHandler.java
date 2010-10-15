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
package com.aionemu.commons.network.netty.handler;

import javolution.util.FastMap;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;

import com.aionemu.commons.network.netty.handler.AbstractChannelHandler.State;
import com.aionemu.commons.network.packet.AbstractClientPacket;

/**
 * @author ATracer
 */
public class PacketHandler
{	
	private static final Logger log = Logger.getLogger(PacketHandler.class);

	private FastMap<State, FastMap<Integer, AbstractClientPacket>>	packetsPrototypes	= new FastMap<State, FastMap<Integer, AbstractClientPacket>>();
	
	/**
	 *  Unknown packet
	 *  
	 * @param id
	 * @param state
	 */
	protected static void unknownPacket(State state, int id)
	{
		log.warn(String.format("Unknown packet received from Game server: 0x%02X state=%s", id, state));
	}
	
	public void addPacketPrototype(AbstractClientPacket packetPrototype, State... states)
	{
		for(State state : states)
		{
			FastMap<Integer, AbstractClientPacket> pm = packetsPrototypes.get(state);
			if(pm == null)
			{
				pm = new FastMap<Integer, AbstractClientPacket>();
				packetsPrototypes.put(state, pm);
			}
			pm.put(packetPrototype.getOpCode(), packetPrototype);
		}
	}
	
	private AbstractClientPacket getPacket(State state, int id, ChannelBuffer buf, AbstractChannelHandler ch)
	{
		AbstractClientPacket prototype = null;

		FastMap<Integer, AbstractClientPacket> pm = packetsPrototypes.get(state);
		if(pm != null)
		{
			prototype = pm.get(id);
		}

		if(prototype == null)
		{
			unknownPacket(state, id);
			return null;
		}
		///???
		AbstractClientPacket res = prototype.clonePacket();
		res.setBuf(buf);
		res.setChannelHandler(ch);

		return res;
	}
	
	public AbstractClientPacket handle(ChannelBuffer data, AbstractChannelHandler ch)
	{
		State state = ch.getState();
		int id = data.readByte() & 0xff;
		return getPacket(state, id, data, ch);
	}

}
