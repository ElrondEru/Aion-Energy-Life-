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
package com.aionemu.commons.network.netty.pipeline;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;

/**
 * @author lyahim
 *
 */
public abstract class AbstractPipeLineFactory implements ChannelPipelineFactory
{
	protected static final int						THREADS_MAX			= 10;
	protected static final int						MEMORY_PER_CHANNEL	= 1048576;
	protected static final int						TOTAL_MEMORY		= 134217728;
	protected static final int						TIMEOUT				= 100;

	protected OrderedMemoryAwareThreadPoolExecutor	pipelineExecutor;

	public AbstractPipeLineFactory()
	{
		this.pipelineExecutor = new OrderedMemoryAwareThreadPoolExecutor(THREADS_MAX, MEMORY_PER_CHANNEL, TOTAL_MEMORY,
			TIMEOUT, TimeUnit.MILLISECONDS, Executors.defaultThreadFactory());
	}
}
