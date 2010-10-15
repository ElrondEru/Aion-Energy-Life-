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
package com.aionemu.commons.network.packet;

import org.jboss.netty.buffer.ChannelBuffer;

import com.aionemu.commons.network.netty.handler.AbstractChannelHandler;

/**
 * @author lyahim
 *
 */
public abstract class AbstractServerPacket extends AbstractPacket
{
	/**
	 * 
	 * @param opCode
	 */
	public AbstractServerPacket(int opCode)
	{
		super(opCode);
	}
	
	/**
	 * 
	 * @param buf
	 * @param value
	 */
	protected final void writeD(ChannelBuffer buf, int value)
	{
		buf.writeInt(value);
	}
	
	/**
	 * 
	 * @param buf
	 * @param value
	 */
	protected final void writeH(ChannelBuffer buf, int value)
	{
		buf.writeShort((short)value);
	}

	/**
	 * 
	 * @param buf
	 * @param value
	 */
	protected final void writeC(ChannelBuffer buf, int value)
	{
		buf.writeByte((byte)value);
	}

	/**
	 * Write double to buffer.
	 * 
	 * @param buf
	 * @param value
	 */
	protected final void writeDF(ChannelBuffer buf, double value)
	{
		buf.writeDouble(value);
	}
	
	/**
	 * Write float to buffer.
	 * 
	 * @param buf
	 * @param value
	 */
	protected final void writeF(ChannelBuffer buf, float value)
	{
		buf.writeFloat(value);
	}
	
	/**
	 * 
	 * @param buf
	 * @param data
	 */
	protected final void writeB(ChannelBuffer buf, byte[] data)
	{
		buf.writeBytes(data);
	}
	
	/**
	 * Write String to buffer
	 * 
	 * @param buf
	 * @param text
	 */
	protected final void writeS(ChannelBuffer buf, String text)
	{
		if (text == null)
		{
			buf.writeChar('\000');
		}
		else
		{
			final int len = text.length();
			for (int i = 0; i < len; i++)
				buf.writeChar(text.charAt(i));
			buf.writeChar('\000');
		}
	}
	
	/**
	 * 
	 * @param buf
	 * @param data
	 */
	protected final void writeQ(ChannelBuffer buf, long data)
	{
		buf.writeLong(data);
	}
	
	public void write(AbstractChannelHandler clientChannelHandler, ChannelBuffer buf)
	{
		writeH(buf,0);
		writeImpl(clientChannelHandler, buf);
	}
	
	/**
	 * 
	 * @param cHandler
	 * @param buf
	 */
	protected abstract void writeImpl(AbstractChannelHandler cHandler, ChannelBuffer buf);
}
