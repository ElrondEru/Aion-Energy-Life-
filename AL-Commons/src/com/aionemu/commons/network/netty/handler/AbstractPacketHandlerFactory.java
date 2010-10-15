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

import com.aionemu.commons.network.netty.handler.AbstractChannelHandler.State;
import com.aionemu.commons.network.packet.AbstractClientPacket;

/**
 * @author lyahim
 *
 */
public abstract class AbstractPacketHandlerFactory
{
	private PacketHandler handler;
	
	protected AbstractPacketHandlerFactory()
	{
		handler = new PacketHandler();
	}
	
	protected void addPacket(AbstractClientPacket prototype, State... states)
	{
		handler.addPacketPrototype(prototype, states);
	}
	
	public PacketHandler getPacketHandler()
	{
		return handler;
	}
}
