package com.aionemu.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author IlBuono
 */
public class SM_PLASTIC_SURGERY extends AionServerPacket
{
	private int		playerObjId;
    private byte    check_ticket;
    private byte    change_sex;

	public SM_PLASTIC_SURGERY(Player player, byte check_ticket, byte change_sex)
	{
            this.playerObjId = player.getObjectId();
            this.check_ticket = check_ticket;
            this.change_sex = change_sex;
	}


	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
            writeD(buf, playerObjId);
            writeC(buf, check_ticket);
            writeC(buf, change_sex);
	}
}