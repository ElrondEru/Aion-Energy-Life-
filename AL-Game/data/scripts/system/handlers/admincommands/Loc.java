/**
 * This file is part of aion-lightning <aion-lightning.org>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */

package admincommands;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;

/**
 * Admin loc command.
 *
 * @author Mystik and Devils Angel
 */

public class Loc extends AdminCommand
{

	/**
	 * Constructor.
	 */

	public Loc()
	{
		super("loc");
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public void executeCommand(Player player, String[] params)
	{
		if(player.getAccessLevel() < AdminConfig.COMMAND_LOC)
		{
			PacketSendUtility.sendMessage(player, "You dont have enough rights to execute this command");
			return;
		}

		PacketSendUtility.sendMessage(player, "Your location : Map "+player.getWorldId()+" X "+player.getX()+" Y "+player.getY()+" Z "+player.getZ()+" Heading "+player.getHeading());
	}
}
