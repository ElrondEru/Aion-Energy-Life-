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
package com.aionemu.gameserver.network.aion.serverpackets;

import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Set;

import com.aionemu.gameserver.model.gameobjects.BrokerItem;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.stats.modifiers.SimpleModifier;
import com.aionemu.gameserver.model.gameobjects.stats.modifiers.StatModifier;
import com.aionemu.gameserver.model.items.ItemStone;
import com.aionemu.gameserver.model.items.ManaStone;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author IlBuono, kosyachok
 *
 */
public class SM_BROKER_SERVICE extends AionServerPacket
{
	private enum BrokerPacketType
	{
		SEARCHED_ITEMS(0),
		REGISTERED_ITEMS(1),
		REGISTER_ITEM(3),
		SHOW_SETTLED_ICON(5),
		SETTLED_ITEMS(5),
		REMOVE_SETTLED_ICON(6);
		
		private int id;
		
		private BrokerPacketType(int id)
		{
			this.id = id;
		}
		
		private int getId()
		{
			return id;
		}
	}
	
	private BrokerPacketType type;
	private BrokerItem[] brokerItems;
	private int itemsCount;
	private int startPage;
	private int message;
	private long settled_kinah;
	
	public SM_BROKER_SERVICE(BrokerItem brokerItem, int message)
	{
		this.type = BrokerPacketType.REGISTER_ITEM;
		this.brokerItems = new BrokerItem[] {brokerItem};
		this.message = message;
	}
	
	public SM_BROKER_SERVICE(int message)
	{
		this.type = BrokerPacketType.REGISTER_ITEM;
		this.message = message;
	}
	
	public SM_BROKER_SERVICE(BrokerItem[] brokerItems)
	{
		this.type = BrokerPacketType.REGISTERED_ITEMS;
		this.brokerItems = brokerItems;
	}
	
	public SM_BROKER_SERVICE(BrokerItem[] brokerItems, long settled_kinah)
	{
		this.type = BrokerPacketType.SETTLED_ITEMS;
		this.brokerItems = brokerItems;
		this.settled_kinah = settled_kinah;
	}
	
	public SM_BROKER_SERVICE(BrokerItem[] brokerItems, int itemsCount, int startPage)
	{
		this.type = BrokerPacketType.SEARCHED_ITEMS;
		this.brokerItems = brokerItems;
		this.itemsCount = itemsCount;
		this.startPage = startPage;
	}
	
	public SM_BROKER_SERVICE(boolean showSettledIcon, long settled_kinah)
	{
		this.type = showSettledIcon ? BrokerPacketType.SHOW_SETTLED_ICON : BrokerPacketType.REMOVE_SETTLED_ICON;
		this.settled_kinah = settled_kinah;
	}
	
	@Override
	protected void writeImpl(AionConnection con, ByteBuffer buf)
	{	
		switch (type)
		{
			case SEARCHED_ITEMS:
				writeSearchedItems(buf);
				break;
			case REGISTERED_ITEMS:
				writeRegisteredItems(buf);
				break;
			case REGISTER_ITEM:
				writeRegisterItem(buf);
				break;
			case SHOW_SETTLED_ICON:
				writeShowSettledIcon(buf);
				break;
			case REMOVE_SETTLED_ICON:
				writeRemoveSettledIcon(buf);
				break;
			case SETTLED_ITEMS:
				writeShowSettledItems(buf);
				break;
		}
			
	}
	
	private void writeSearchedItems(ByteBuffer buf)
	{
		writeC(buf, type.getId());
		writeD(buf, itemsCount);
		writeC(buf, 0);
		writeH(buf, startPage);
		writeH(buf, brokerItems.length);
		for(BrokerItem item : brokerItems)
		{
			if(item.getItem().getItemTemplate().isArmor() || item.getItem().getItemTemplate().isWeapon())
				writeArmorWeaponInfo(buf, item);
			else
				writeCommonInfo(buf, item);
		}
	}
	
	private void writeRegisteredItems(ByteBuffer buf)
	{
		writeC(buf, type.getId());
		writeD(buf, 0x00);
		writeH(buf, brokerItems.length); //you can register a max of 15 items, so 0x0F
		for(BrokerItem item : brokerItems)
		{
			writeD(buf, item.getItemUniqueId());
			writeD(buf, item.getItemId());
			writeQ(buf, item.getPrice());
			writeQ(buf, item.getItem().getItemCount());
			writeQ(buf, item.getItem().getItemCount());
			Timestamp currentTime = new Timestamp(Calendar.getInstance().getTimeInMillis());
			int daysLeft = Math.round((item.getExpireTime().getTime() - currentTime.getTime()) / 86400000);
			writeH(buf, daysLeft);
			writeC(buf, 0);
			writeD(buf, item.getItemId());
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeH(buf, 0);
		}
	}
	
	private void writeRegisterItem(ByteBuffer buf)
	{
		writeC(buf, type.getId());
		writeH(buf, message);
		if(message == 0)
		{
			BrokerItem itemForRegistration = brokerItems[0];
			writeD(buf, itemForRegistration.getItemUniqueId());
			writeD(buf, itemForRegistration.getItemId());
			writeQ(buf, itemForRegistration.getPrice());
			writeQ(buf, itemForRegistration.getItem().getItemCount());
			writeQ(buf, itemForRegistration.getItem().getItemCount());
			writeH(buf, 8); //days left
			writeC(buf, 0);
			writeD(buf, itemForRegistration.getItemId());
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeH(buf, 0);
		}
	}
	
	private void writeShowSettledIcon(ByteBuffer buf)
	{
		writeC(buf, type.getId());
		writeQ(buf, settled_kinah);
		writeD(buf, 0x00);
		writeH(buf, 0x00);
		writeH(buf, 0x01);
		writeC(buf, 0x00);
	}
	
	private void writeRemoveSettledIcon(ByteBuffer buf)
	{
		writeH(buf, type.getId());
	}
	
	private void writeShowSettledItems(ByteBuffer buf)
	{
		writeC(buf, type.getId());
		writeQ(buf, settled_kinah);
        
		writeH(buf, brokerItems.length);
        writeD(buf, 0x00); 
        writeC(buf, 0x00);
        
		writeH(buf, brokerItems.length);
		for(BrokerItem settledItem : brokerItems)
		{
			writeD(buf, settledItem.getItemId());
			if(settledItem.isSold())
				writeQ(buf, settledItem.getPrice());
			else
				writeQ(buf, 0);
			writeQ(buf, settledItem.getItemCount());
			writeQ(buf, settledItem.getItemCount());
			writeD(buf, (int)settledItem.getSettleTime().getTime() / 60000);
			writeH(buf, 0);
			writeD(buf, settledItem.getItemId());
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeH(buf, 0);
		}
	}
	
	private void writeArmorWeaponInfo(ByteBuffer buf, BrokerItem item)
	{
		writeD(buf, item.getItem().getObjectId());
		writeD(buf, item.getItem().getItemTemplate().getTemplateId());
		writeQ(buf, item.getPrice());
		writeQ(buf, item.getItem().getItemCount());
		writeC(buf, 0);
		writeC(buf, item.getItem().getEnchantLevel());
		writeD(buf, item.getItem().getItemSkinTemplate().getTemplateId());
		writeC(buf, 0);
		
		writeItemStones(buf, item.getItem());
		
		ItemStone god = item.getItem().getGodStone();
		writeD(buf, god == null ? 0 : god.getItemId());
		
		writeC(buf, 0);
		writeD(buf, 0);
		writeD(buf, 0);
		writeS(buf, item.getSeller());
		writeS(buf, ""); //creator
		
	}
	
	private void writeItemStones(ByteBuffer buf, Item item)
	{
		int count = 0;
		
		if(item.hasManaStones())
		{
			Set<ManaStone> itemStones = item.getItemStones();
			
			for(ManaStone itemStone : itemStones)
			{
				if(count == 6)
					break;

				StatModifier modifier = itemStone.getFirstModifier();
				if(modifier != null)
				{
					count++;
					writeC(buf, modifier.getStat().getItemStoneMask());
				}
			}
			writeB(buf, new byte[(6-count)]);
			count = 0;
			for(ManaStone itemStone : itemStones)
			{
				if(count == 6)
					break;

				StatModifier modifier = itemStone.getFirstModifier();
				if(modifier != null)
				{
					count++;
					writeH(buf, ((SimpleModifier)modifier).getValue());
				}
			}
			writeB(buf, new byte[(6-count)*2]);
		}
		else
		{
			writeB(buf, new byte[18]);
		}

		//for now max 6 stones - write some junk
	}
	
	private void writeCommonInfo(ByteBuffer buf, BrokerItem item)
	{
		writeD(buf, item.getItem().getObjectId());
		writeD(buf, item.getItem().getItemTemplate().getTemplateId());
		writeQ(buf, item.getPrice());
		writeQ(buf, item.getItem().getItemCount());
		writeD(buf, 0);
		writeD(buf, 0);
		writeD(buf, 0);
		writeD(buf, 0);
		writeD(buf, 0);
		writeD(buf, 0);
		writeD(buf, 0);
		writeD(buf, 0);
		writeD(buf, 0);
		writeH(buf, 0);
		writeS(buf, item.getSeller());
		writeS(buf, ""); //creator
	}
}
