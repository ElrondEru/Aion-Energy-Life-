/*
 * This file is part of aion-unique <aion-unique.smfnew.com>.
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
package com.aionemu.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import java.util.List;

import com.aionemu.gameserver.model.gameobjects.player.ToyPet;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author M@xx
 */
public class SM_PET extends AionServerPacket
{
	private int actionId;
	private ToyPet pet;
	private List<ToyPet> pets;
	private int petUniqueId;
	
	public SM_PET(int actionId)
	{
		this.actionId = actionId;
	}
	
	public SM_PET(int actionId, int petUniqueId)
	{
		this.actionId = actionId;
		this.petUniqueId = petUniqueId;
	}
	
	public SM_PET(int actionId, ToyPet pet)
	{
		this.actionId = actionId;
		this.pet = pet;
	}
	
	public SM_PET(int actionId, List<ToyPet> pets)
	{
		this.actionId = actionId;
		this.pets = pets;
	}

	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{
		writeH(buf, actionId);
		switch(actionId)
		{
			case 0:
				// load list on login
				int counter = 0;
				writeC(buf, 0); // unk
				writeH(buf, pets.size());
				for(ToyPet p : pets)
				{
					counter++;
					writeS(buf, p.getName());
					writeD(buf, p.getPetId());
					writeD(buf, p.getDatabaseIndex()); //unk
					writeD(buf, 0); //unk
					writeD(buf, 0); //unk
					writeD(buf, 0); //unk
					writeD(buf, 1284402195); //creation timestamp - birthday
					writeC(buf, 2); //unk +
					writeD(buf, 0); //unk
					writeD(buf, 0); //unk
					writeC(buf, 2); //unk +
					writeD(buf, 0); //unk
					writeD(buf, 0); //unk
					writeC(buf, 1); //unk +
					writeD(buf, 0); //unk
					writeD(buf, 0); //function id(s) ?
					writeD(buf, 0); //unk
					writeD(buf, 0); //unk
				}
				break;
			case 1:
				// adopt
				writeS(buf, pet.getName());
				writeD(buf, pet.getPetId());
				writeD(buf, pet.getDatabaseIndex()); //unk
				writeD(buf, 0); //unk
				writeD(buf, 0); //unk
				writeD(buf, 0); //unk
				writeD(buf, 0); //unk
				writeC(buf, 0); //unk +
				writeD(buf, 0); //unk
				writeD(buf, 0); //unk
				writeC(buf, 0); //unk +
				writeD(buf, 0); //unk
				writeD(buf, 0); //unk
				writeC(buf, 0); //unk +
				writeD(buf, 0); //unk
				writeD(buf, 0); //unk
				writeD(buf, 0); //unk
				writeD(buf, 0); //unk
				break;
			case 2:
				// surrender
				writeD(buf, pet.getPetId());
				writeD(buf, pet.getDatabaseIndex()); //unk
				writeD(buf, 0); //unk
				writeD(buf, 0); //unk
				break;
			case 3:
				// spawn
				writeS(buf, pet.getName());
				writeD(buf, pet.getPetId());
				writeD(buf, pet.getDatabaseIndex());
				
				if(pet.getX1() == 0 && pet.getY1() == 0 && pet.getZ1() == 0)
				{
					writeF(buf, pet.getMaster().getX());
					writeF(buf, pet.getMaster().getY());
					writeF(buf, pet.getMaster().getZ());
					
					writeF(buf, pet.getMaster().getX());
					writeF(buf, pet.getMaster().getY());
					writeF(buf, pet.getMaster().getZ());
					
					writeC(buf, pet.getMaster().getHeading());
				}
				else
				{
					writeF(buf, pet.getX1());
					writeF(buf, pet.getY1());
					writeF(buf, pet.getZ1());
					
					writeF(buf, pet.getX2());
					writeF(buf, pet.getY2());
					writeF(buf, pet.getZ2());
					
					writeC(buf, pet.getH());
				}
				
				writeD(buf, pet.getMaster().getObjectId()); //unk
				
				writeC(buf, 1); //unk
				
				writeD(buf, 0); //unk
				
				writeD(buf, 0); //unk
				writeD(buf, 0); //unk
				writeD(buf, 0); //unk
				writeD(buf, 0); //unk
				break;
			case 4:
				// dismiss
				writeD(buf, petUniqueId);
				writeC(buf, 0x01);
				break;
			case 10:
				// rename
				writeD(buf, 0); //unk
				writeS(buf, pet.getName());
				break;
			default:
				break;					
		}
	}
}
