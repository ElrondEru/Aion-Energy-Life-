/*
 * This file is part of Quebec force.
 *
 * Quebec force is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Quebec force is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Quebec force.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.inggison;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.stats.StatEnum;
import com.aionemu.gameserver.model.templates.quest.QuestItems;
import com.aionemu.gameserver.network.aion.SystemMessageId;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ASCENSION_MORPH;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.InstanceService;
import com.aionemu.gameserver.services.ItemService;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.TeleportService;
import com.aionemu.gameserver.services.ZoneService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.zone.ZoneName;
/**
 * @author dta3000
 * 
 */
public class _10026SecretOfInggison extends QuestHandler
{
	private final static int	questId	= 10026;

	public _10026SecretOfInggison()
	{
		super(questId);
	}

	@Override
	public void register()
	{
                qe.addQuestLvlUp(questId);
		qe.setNpcQuestData(799052).addOnQuestStart(questId);            // Steropes
                qe.setNpcQuestData(799052).addOnTalkEvent(questId);
                qe.setNpcQuestData(799053).addOnTalkEvent(questId);             // Nydrea
	}

	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs == null)
			return false;

		final int var = qs.getQuestVarById(0);
		int targetId = 0;
		if(env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();

		if(qs.getStatus() == QuestStatus.REWARD)
		{
			if(targetId == 799053)
				return defaultQuestEndDialog(env);
		}
		else if(qs.getStatus() != QuestStatus.START)
		{
			return false;
		}
		if(targetId == 799052)
		{
			switch(env.getDialogId())
			{
				case 25:
					if(var == 0)
						return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 1011);
				case 10000:
					if(var == 0)
					{
                                                PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(1, 14));
						qs.setQuestVarById(0, var + 1);
						updateQuestStatus(player, qs);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
					return false;
			}
		}
		else if(targetId == 799053)
		{
			switch(env.getDialogId())
			{
				case 25:
					if(var == 1)
						return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 1352);
					else if(var == 1)
						return sendQuestDialog(player, env.getVisibleObject().getObjectId(), 1353);
				case 10255:
					if(var == 1)
					{
						qs.setStatus(QuestStatus.REWARD);						
						updateQuestStatus(player, qs);
                                                PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(1, 15));
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}
				case 10002:
					if (qs.getStatus() == QuestStatus.REWARD)
					{
                                                qs.setStatus(QuestStatus.COMPLETE);						
						updateQuestStatus(player, qs);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						return true;
					}					
					return false;
			}		
		}
		return false;
	}
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
                final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if(qs != null)
			return false;

		QuestState qs2 = player.getQuestStateList().getQuestState(10000);
		if(qs2 == null || qs2.getStatus() != QuestStatus.COMPLETE)
			return false;
		env.setQuestId(questId);
		QuestService.startQuest(env, QuestStatus.START);
		return true;
	}
}