/*
* 
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
 
import java.util.Iterator;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.services.TeleportService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;

/*
*
* World Channel //wc
*
* -Evilwizard-
* http://www.sielaion.fr
* 
*/

public class wc extends AdminCommand
{
	public wc()
	{
		super("wc");
	}

	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (params.length == 3)
		{
			PacketSendUtility.sendMessage(admin, "//syntax wc <message>");
			return;
		}
		if (admin.getWorldId() == 320090000)
		{
			PacketSendUtility.sendMessage(admin, "Vous ne pouvez pas utiliser le canal World en prison. :-)");
			return;
		}
		if (admin.getLevel() < 1)
		{
			PacketSendUtility.sendMessage(admin, "Vous ne pouvez pas utiliser le canal World en dessous du niveau 15 !");
			return;
		}
		if (admin.getCommonData().getRace() == Race.ELYOS)
		{
			Iterator<Player> iter = admin.getActiveRegion().getWorld().getPlayersIterator();
			StringBuilder sbMessage = new StringBuilder("[World-Elyseen]" + admin.getName() + ": ");

			for (String p : params)
			sbMessage.append(p + " ");
			String sMessage = sbMessage.toString().trim();
			while (iter.hasNext())
			{
			    Player pwc = iter.next();
			    if(pwc.getCommonData().getRace() == Race.ELYOS || pwc.getAccessLevel() > AdminConfig.COMMAND_GOTO)
			    {
			        PacketSendUtility.sendMessage(pwc, sMessage);
			    }
			}
		}
		if (admin.getCommonData().getRace() == Race.ASMODIANS)
		{
			Iterator<Player> iter = admin.getActiveRegion().getWorld().getPlayersIterator();
			StringBuilder sbMessage = new StringBuilder("[World-Asmodien]" + admin.getName() + ": ");

			for (String p : params)
			sbMessage.append(p + " ");
			String sMessage = sbMessage.toString().trim();
			while (iter.hasNext())
			{
			    Player pwc = iter.next();
			    if(pwc.getCommonData().getRace() == Race.ASMODIANS || pwc.getAccessLevel() > AdminConfig.COMMAND_GOTO)
			    {
			        PacketSendUtility.sendMessage(pwc, sMessage);
			    }
			}
		}
	}
}
