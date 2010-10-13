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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerAppearanceDAO;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.templates.quest.QuestItems;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.model.Gender;
import com.aionemu.gameserver.model.account.Account;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerAppearance;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.services.ItemService;
import com.aionemu.gameserver.services.PlayerService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * In this packets aion client is requesting edit of character.
 *
 * @author IlBuono
 *
 */
public class CM_CHARACTER_EDIT extends AionClientPacket
{    
	
    private int objectId;

    private boolean gender_change;

    private boolean check_ticket = true;

	/**
	 * Constructs new instance of <tt>CM_CREATE_CHARACTER </tt> packet
	 *
	 * @param opcode
	 */
	public CM_CHARACTER_EDIT(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
        AionConnection client = getConnection();
		Account account = client.getAccount();
        objectId = readD();
        Player player = PlayerService.getPlayer(objectId, account);
        readB(44);
        PlayerCommonData playerCommonData = player.getCommonData();
        PlayerAppearance playerAppearance = player.getPlayerAppearance();
        //Before modify appearance, we do a check of ticket
        int gender = readD();
        gender_change = playerCommonData.getGender().getGenderId() == gender ? false : true;
        if(!gender_change)
        {
            if (player.getInventory().getItemCountByItemId(169650000) == 0 && player.getInventory().getItemCountByItemId(169650001) == 0)
            {
                check_ticket = false;
                return;
            }
        }
        else
        {
            if (player.getInventory().getItemCountByItemId(169660000) == 0 && player.getInventory().getItemCountByItemId(169660001) == 0)
            {
                check_ticket = false;
                return;
            }
        }
        playerCommonData.setGender(gender == 0 ? Gender.MALE : Gender.FEMALE);
		readD(); //race
		readD(); //player class

		playerAppearance.setVoice(readD());
		playerAppearance.setSkinRGB(readD());
		playerAppearance.setHairRGB(readD());
		playerAppearance.setEyeRGB(readD());
		playerAppearance.setLipRGB(readD());
		playerAppearance.setFace(readC());
		playerAppearance.setHair(readC());
		playerAppearance.setDeco(readC());
		playerAppearance.setTattoo(readC());

		readC(); // always 4 o0 // 5 in 1.5.x

		playerAppearance.setFaceShape(readC());
		playerAppearance.setForehead(readC());

		playerAppearance.setEyeHeight(readC());
		playerAppearance.setEyeSpace(readC());
		playerAppearance.setEyeWidth(readC());
		playerAppearance.setEyeSize(readC());
		playerAppearance.setEyeShape(readC());
		playerAppearance.setEyeAngle(readC());

		playerAppearance.setBrowHeight(readC());
		playerAppearance.setBrowAngle(readC());
		playerAppearance.setBrowShape(readC());

		playerAppearance.setNose(readC());
		playerAppearance.setNoseBridge(readC());
		playerAppearance.setNoseWidth(readC());
		playerAppearance.setNoseTip(readC());

		playerAppearance.setCheek(readC());
		playerAppearance.setLipHeight(readC());
		playerAppearance.setMouthSize(readC());
		playerAppearance.setLipSize(readC());
		playerAppearance.setSmile(readC());
		playerAppearance.setLipShape(readC());
		playerAppearance.setJawHeigh(readC());
		playerAppearance.setChinJut(readC());
		playerAppearance.setEarShape(readC());
		playerAppearance.setHeadSize(readC());

		playerAppearance.setNeck(readC());
		playerAppearance.setNeckLength(readC());

		playerAppearance.setShoulderSize(readC());

		playerAppearance.setTorso(readC());
		playerAppearance.setChest(readC()); // only woman
		playerAppearance.setWaist(readC());

		playerAppearance.setHips(readC());
		playerAppearance.setArmThickness(readC());

		playerAppearance.setHandSize(readC());
		playerAppearance.setLegThicnkess(readC());

		playerAppearance.setFootSize(readC());
		playerAppearance.setFacialRate(readC());

		readC(); // always 0
		playerAppearance.setArmLength(readC());
		playerAppearance.setLegLength(readC()); //wrong??
		playerAppearance.setShoulders(readC()); // 1.5.x May be ShoulderSize
		readC(); // always 0
		readC();
		playerAppearance.setHeight(readF());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		AionConnection client = getConnection();		
		CM_ENTER_WORLD.enterWorld(client, objectId);
		Player player = client.getActivePlayer();
        if (!check_ticket)
        {
            if (!gender_change)
                PacketSendUtility.sendSysMessage(player, "You must have a Plastic Surgery Ticket!");
            else
                PacketSendUtility.sendSysMessage(player, "You must have a Gender Switch Ticket!");
        }
        else
        {
        //Remove ticket and save appearance
        	if(!gender_change)
            {
                if (player.getInventory().getItemCountByItemId(169650000) > 0) //plastic surgery ticket normal
                    player.getInventory().removeFromBagByItemId(169650000,1);
                else if (player.getInventory().getItemCountByItemId(169650001) > 0) //plastic surgery ticket event
                	  player.getInventory().removeFromBagByItemId(169650001, 1);
            }
            else
            {
                if (player.getInventory().getItemCountByItemId(169660000) > 0) //gender switch ticket normal
                	 player.getInventory().removeFromBagByItemId(169660000, 1);
                else if (player.getInventory().getItemCountByItemId(169660001) > 0) //gender switch ticket event
                	 player.getInventory().removeFromBagByItemId(169660001, 1);
                DAOManager.getDAO(PlayerDAO.class).storePlayer(player); //save new gender
            }
        	DAOManager.getDAO(PlayerAppearanceDAO.class).store(player);	//save new appearance
	}
 }
}