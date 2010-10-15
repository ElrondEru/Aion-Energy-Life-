/*
 * This file is part of aion-emu <aion-emu.com>.
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
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package admincommands;

import java.util.Collection;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.group.PlayerGroup;
import com.aionemu.gameserver.services.TeleportService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;

/*
 * @author Source
 */

public class GroupToMe extends AdminCommand
{
/*
 * Constructor.
 */
	public GroupToMe()
	{
		super("grouptome");
	}

	public void executeCommand(Player admin, String[] params)
	{
		if(admin.getAccessLevel() < AdminConfig.COMMAND_GROUPTOME)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
			return;
		}

		if (params == null || params.length < 1)
		{
			PacketSendUtility.sendMessage(admin, "syntax //grouptome <player>");
			return;
		}

		Player groupToMove = World.getInstance().findPlayer(Util.convertName(params[0]));
		if (groupToMove == null)
		{
			PacketSendUtility.sendMessage(admin, "The player is not online.");
			return;
		}

		if(!groupToMove.isInGroup())
		{
			PacketSendUtility.sendMessage(admin, groupToMove.getName() + " is not in group.");
			return;
		}

		Collection<Player> players = World.getInstance().getAllPlayers();
		for(Player player : World.getInstance().getAllPlayers())
		{
			if (player.getPlayerGroup() == groupToMove.getPlayerGroup())
				if (player != admin)
				{
					TeleportService.teleportTo(player, admin.getWorldId(), admin.getInstanceId(), admin.getX(), admin.getY(), admin.getZ(), admin.getHeading(), 0);
					PacketSendUtility.sendMessage(player, "You have been summoned by " + admin.getName() + ".");
					PacketSendUtility.sendMessage(admin, "You summon " + player.getName() + ".");
				}
		}
	}
}