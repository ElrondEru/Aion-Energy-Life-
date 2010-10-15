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

import java.net.InetAddress;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import com.aionemu.commons.network.packet.AbstractServerPacket;



/**
 * @author ATracer
 */
public abstract class AbstractChannelHandler extends SimpleChannelUpstreamHandler
{
	private static final Logger	log	= Logger.getLogger(AbstractChannelHandler.class);

	protected State				state;
	
	/**
	 * IP address of channel
	 */
	protected InetAddress		inetAddress;

	/**
	 * Associated channel
	 */
	protected Channel			channel;
	
	/**
	 * Invoked when a Channel was disconnected from its remote peer
	 */
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
	{
		log.info("Server disconnected IP: " + inetAddress.getHostName());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception
	{
		// noinspection ThrowableResultOfMethodCallIgnored
		log.error("NETTY: Exception caught: ", e.getCause());
		e.getChannel().close();
	}

	/**
	 * Closes the channel
	 */
	public void close()
	{
		channel.close();
	}
	
	/**
	 * Closes the channel but ensures that packet is send before close
	 * 
	 * @param packet
	 */
	public void close(AbstractServerPacket lastpacket)
	{
		sendPacket(lastpacket);
		close();
	}

	/**
	 * @return the Server's IP address string
	 */
	public String getIP()
	{
		return inetAddress.getHostAddress();
	}
	/**
	 * states for all connections
	 * @author lyahim
	 *
	 */
	public static enum State
	{
		CONNECTED,
		AUTHED_GG,
		AUTHED,
		ENTERED
	}
	
	/**
	 * @return the state
	 */
	public State getState()
	{
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(State state)
	{
		this.state = state;
	}

	public abstract void sendPacket(AbstractServerPacket packet);

}
