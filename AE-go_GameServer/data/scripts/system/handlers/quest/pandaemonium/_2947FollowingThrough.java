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
package quest.pandaemonium;

import java.util.Collections;
import com.aionemu.gameserver.model.EmotionType;
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
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.ItemService;
import com.aionemu.gameserver.services.TeleportService;
 



/**
 * @author Hellboy aion4Free
 *
 */
public class _2947FollowingThrough extends QuestHandler
{

	
	
	
	
	private final static int	questId	= 2947;

	public _2947FollowingThrough()
	{
		super(questId);
	}
	@Override
	public void register()
	{
		qe.addQuestLvlUp(questId);
		
		qe.setNpcQuestData(204053).addOnTalkEvent(questId);
		qe.setNpcQuestData(204301).addOnTalkEvent(questId);
		qe.setNpcQuestData(212396).addOnKillEvent(questId);
		qe.setNpcQuestData(212611).addOnKillEvent(questId);
		qe.setNpcQuestData(212408).addOnKillEvent(questId);		
		qe.setNpcQuestData(204089).addOnTalkEvent(questId);
		qe.setNpcQuestData(700368).addOnTalkEvent(questId);
		qe.setNpcQuestData(700369).addOnTalkEvent(questId);
		qe.setNpcQuestData(210343).addOnKillEvent(questId);
		qe.setNpcQuestData(700268).addOnTalkEvent(questId);	
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
				case 204053:
				{
					switch(env.getDialogId())
					{
						case 25:
							if(var == 0)
								return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 1011);
						case 10010:
							if (var == 0)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(player, qs);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
						case 10011:
							if (var == 0)
							{
								qs.setQuestVarById(0, var + 4);
								updateQuestStatus(player, qs);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
						case 10012:
							if (var == 0)
							{
								qs.setQuestVarById(0, var + 9);
								updateQuestStatus(player, qs);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
						case 33:
							if (var== 1 || var==4 || var ==9)
							{
								qs.setQuestVarById(0, 0);
								updateQuestStatus(player, qs);
								return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 2375);
							}
					}
				} break;
				case 204301:
				{
					switch(env.getDialogId())
					{
						case 25:
							if(var == 1)
								return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 1352);
							else if(var == 2)
								return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 3398);
							else if(var == 7)
								return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 3739);
							else if(var == 9)
								{
								if(QuestService.collectItemCheck(env, true))
								{								
									qs.setStatus(QuestStatus.REWARD);
									updateQuestStatus(player, qs);								
									return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 7);
								}
								else									
									return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 4080);	
								}
						case 10001:
							if (var == 1)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(player, qs);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
						case 1009:
							if (var == 2)
							{
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(player, qs);								
								return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 5);
							}
							if (var == 7)
							{
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(player, qs);								
								return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 6);
							}							
					}
				} break;
				case 204089:
				{
					switch(env.getDialogId())
					{
						case 25:
							if(var == 4)
								return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 1693);
							else if(var == 5)
								return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 2034);		
						case 10002:
							if (var == 4)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(player, qs);
								TeleportService.teleportTo(player, 320090000, 276, 293, 163, 90);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));							
								return true;
							}
						case 10003:
							if (var == 5)
							{
								qs.setQuestVarById(0, 7);
								updateQuestStatus(player, qs);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));							
								return true;
							}					
					}
				} break;
				case 700368:				
				{	switch(env.getDialogId())
					{
						case -1:
							if(var == 5 )
								{	
										final int targetObjectId = env.getVisibleObject().getObjectId();
										PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000,
											1));
										PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0,
											targetObjectId), true);
										ThreadPoolManager.getInstance().schedule(new Runnable(){
											@Override
											public void run()
											{
												Npc npc = (Npc)player.getTarget();
											if(npc == null || npc.getObjectId() != targetObjectId)
												return;
											PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(),
												targetObjectId, 3000, 0));
											PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0,
												targetObjectId), true);
												
												TeleportService.teleportTo(player, 320090000, 276, 293, 163, 90);
											}
										
								}, 3000);
							}
							return false;
					}	
				}break;	
				case 700369:				
				{	switch(env.getDialogId())
					{
						case -1:
							if(var == 5 )
								{	
										final int targetObjectId = env.getVisibleObject().getObjectId();
										PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000,
											1));
										PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0,
											targetObjectId), true);
										ThreadPoolManager.getInstance().schedule(new Runnable(){
											@Override
											public void run()
											{
												Npc npc = (Npc)player.getTarget();
											if(npc == null || npc.getObjectId() != targetObjectId)
												return;
											PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(),
												targetObjectId, 3000, 0));
											PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0,
												targetObjectId), true);
												
												TeleportService.teleportTo(player, 120010000, 982, 1556, 210, 90);
											}
										
								}, 3000);								
							}
							return false;
					}	
				}break;
				case 700268:				
				{	switch(env.getDialogId())
					{
						case -1:
							if(var == 9 )
								{	
										final int targetObjectId = env.getVisibleObject().getObjectId();
										PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), targetObjectId, 3000,
											1));
										PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.NEUTRALMODE2, 0,
											targetObjectId), true);
										ThreadPoolManager.getInstance().schedule(new Runnable(){
											@Override
											public void run()
											{
												Npc npc = (Npc)player.getTarget();
											if(npc == null || npc.getObjectId() != targetObjectId)
												return;
											PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(),
												targetObjectId, 3000, 0));
											PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_LOOT, 0,
												targetObjectId), true);
												
											}
										
								}, 3000);
								return true;
							}
					}	
				}break;		
			}
		}
		else if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 204301)
			{
			return defaultQuestEndDialog(env);
			}
		}
		return false;
	}
	
	public boolean onKillEvent(QuestEnv env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null || qs.getStatus() != QuestStatus.START)
			return false;


		int targetId = 0;
		int var = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		switch(targetId)
		{
			case 212396:
				var = qs.getQuestVarById(1);
				if (var < 3)
				{
					qs.setQuestVarById(1, var+1);
					updateQuestStatus(player, qs);
				}
				break;
			case 212611:
				var = qs.getQuestVarById(2);
				if (var < 3)
				{
					qs.setQuestVarById(2, var+1);
					updateQuestStatus(player, qs);
				}
				break;
			case 212408:
				var = qs.getQuestVarById(3);
				if (var < 3)
				{
					qs.setQuestVarById(3, var+1);
					updateQuestStatus(player, qs);
				}
				break;
			case 210343:
				var = qs.getQuestVarById(4);
				if (var < 10)
				{
					qs.setQuestVarById(4, var+1);
					updateQuestStatus(player, qs);
				}	
		}
		return false;
	}


	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestState qs2 = player.getQuestStateList().getQuestState(2946);
		
		if(qs == null)
		{
			if(player.getCommonData().getLevel() < 25)
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
