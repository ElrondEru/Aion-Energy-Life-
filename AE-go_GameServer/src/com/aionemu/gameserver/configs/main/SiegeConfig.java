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
package com.aionemu.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

/**
 * @author Sarynth @Modified by Mcrizza,Source,xTz
 */
public class SiegeConfig
{
	/**
	 * Basic Siege Config
	 */

	/**
	 * Siege Enabled
	 */
	@Property(key = "siege.enabled", defaultValue = "true")
	public static boolean	SIEGE_ENABLED;

	/**
	 * Siege Timer Interval
	 */
	@Property(key = "siege.interval", defaultValue = "7200")
	public static int		SIEGE_TIMER_INTERVAL;

	/**
	 * Siege Location Values
	 */

	@Property(key = "siege.influence.fortress", defaultValue = "10")
	public static int		SIEGE_POINTS_FORTRESS;

	@Property(key = "siege.influence.artifact", defaultValue = "1")
	public static int		SIEGE_POINTS_ARTIFACT;
	
	@Property(key = "siege.vulnerable.chance", defaultValue = "100")
	public static int		SIEGE_VULNERABLE_CHANCE;
	
    @Property(key = "siege.invul.interval", defaultValue = "3600")
	public static int		SIEGE_INVUL_INTERVAL;
	
	 @Property(key = "siege.gmedal.amount", defaultValue = "3")
	public static int		SIEGE_GMEDAL_AMOUNT;
	
	@Property(key = "siege.smedal.amount", defaultValue = "3")
	public static int		SIEGE_SMEDAL_AMOUNT;
	
	@Property(key = "siege.pmedal.amount", defaultValue = "0")
	public static int		SIEGE_PMEDAL_AMOUNT;
}
