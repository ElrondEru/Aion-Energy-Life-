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
package quest.sanctum;

import java.util.Collections;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.zone.ZoneName;
import com.aionemu.gameserver.model.templates.quest.QuestItems;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_USE_OBJECT;
import com.aionemu.gameserver.services.ItemService;
 



/**
 * @author Hellboy aion4Free
 *
 */
public class _1098PearlofProtection extends QuestHandler
{	
	private final static int	questId	= 1098;
	private final static int[]	npc_ids	= { 790001, 730008, 730019, 204647, 203183, 203989, 798155, 204549, 203752, 203164, 203917, 203996, 798176, 798212, 204535};
	
	public _1098PearlofProtection()
	{
		super(questId);
	}
	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		for(int npc_id : npc_ids)
			qe.setNpcQuestData(npc_id).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		int var = qs.getQuestVarById(0);
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		if(qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				case 790001:
				{
					switch(env.getDialogId())
					{
						case 25:
							if(var == 0)
								return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 1011);
						case 10000:
							if (var == 0)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(player, qs);
								ItemService.addItems(player, Collections.singletonList(new QuestItems(182206062, 1)));
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				}	break;
				case 730008:
				{
					switch(env.getDialogId())
					{
						case 25:
							if(var == 1)
								return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 1352);
						case 10001:
							if (var == 1)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(player, qs);
								player.getInventory().removeFromBagByItemId(182206062, 1);
								ItemService.addItems(player, Collections.singletonList(new QuestItems(182206063, 1)));
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				}	break;	
				case 730019:
				{
					switch(env.getDialogId())
					{
						case 25:
							if(var == 2)
								return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 1693);
						case 10002:
							if (var == 2)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(player, qs);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				}	break;	
				case 204647:
				{
					switch(env.getDialogId())
					{
						case 25:
							if(var == 3)
								return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 2034);
						case 10003:
							if (var == 3)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(player, qs);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				}	break;
				case 203183:
				{
					switch(env.getDialogId())
					{
						case 25:
							if(var == 4)
								return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 2375);
						case 10004:
							if (var == 4)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(player, qs);
								player.getInventory().removeFromBagByItemId(182206063, 1);
								ItemService.addItems(player, Collections.singletonList(new QuestItems(182206064, 1)));
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				}	break;
				case 203989:
				{
					switch(env.getDialogId())
					{
						case 25:
							if(var == 5)
								return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 2716);
						case 10005:
							if (var == 5)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(player, qs);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				}	break;
				case 798155:
				{
					switch(env.getDialogId())
					{
						case 25:
							if(var == 6)
								return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 3057);
						case 10006:
							if (var == 6)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(player, qs);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				}	break;
				case 204549:
				{
					switch(env.getDialogId())
					{
						case 25:
							if(var == 7)
								return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 3398);
						case 10007:
							if (var == 7)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(player, qs);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				}	break;
				case 203752:
				{
					switch(env.getDialogId())
					{
						case 25:
							if(var == 8)
								return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 3739);
						case 10008:
							if (var == 8)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(player, qs);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				}	break;
				case 203164:
				{
					switch(env.getDialogId())
					{
						case 25:
							if(var == 9)
								return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 4080);
						case 10009:
							if (var == 9)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(player, qs);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				}	break;
				case 203917:
				{
					switch(env.getDialogId())
					{
						case 25:
							if(var == 10)
								return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 1608);
						case 10010:
							if (var == 10)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(player, qs);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				}	break;
				case 203996:
				{
					switch(env.getDialogId())
					{
						case 25:
							if(var == 11)
								return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 1949);
						case 10011:
							if (var == 11)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(player, qs);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				}	break;
				case 798176:
				{
					switch(env.getDialogId())
					{
						case 25:
							if(var == 12)
								return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 2290);
						case 10012:
							if (var == 12)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(player, qs);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				}	break;
				case 798212:
				{
					switch(env.getDialogId())
					{
						case 25:
							if(var == 13)
								return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 2631);
						case 10013:
							if (var == 13)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(player, qs);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				}	break;
				case 204535:
				{
					switch(env.getDialogId())
					{
						case 25:
							if(var == 14)
								return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 2972);
						case 10255:
							if (var == 14)
							{
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(player, qs);
								player.getInventory().removeFromBagByItemId(182206064, 1);
								ItemService.addItems(player, Collections.singletonList(new QuestItems(182206065, 1)));
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
					}
				}	break;
			}
		}
		else if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 790001)
			{	
				if (env.getDialogId() == -1 )				
					return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 10002);
				else
					{
					player.getInventory().removeFromBagByItemId(182206065, 1);	
					return defaultQuestEndDialog(env);
					}
			}
		}
		return false;
	}
	
	@Override
		public boolean onLvlUpEvent(QuestEnv env)
		{
			Player player = env.getPlayer();
			QuestState qs = player.getQuestStateList().getQuestState(questId);
			QuestState qs2 = player.getQuestStateList().getQuestState(1097);
			
			if(qs == null)
			{
				if(player.getCommonData().getLevel() < 50)
					return false;
				
				else if(qs2 == null || qs2.getStatus() != QuestStatus.COMPLETE)
					return false;	
			
				env.setQuestId(questId);
				QuestService.startQuest(env, QuestStatus.START);
				return true;
			}
			return false;		
		}
}
