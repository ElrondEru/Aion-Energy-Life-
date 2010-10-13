package mysql5;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.IUStH;
import com.aionemu.commons.database.ParamReadStH;
import com.aionemu.gameserver.dao.PortalCooldownsDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.PortalCooldown;

public class MySQL5PortalCooldownsDAO extends PortalCooldownsDAO
{
	public static final String INSERT_QUERY = "INSERT INTO `portal_cooldowns` (`player_id`, `world_id`, `use_delay`, `reuse_time`) VALUES (?,?,?,?)";
	public static final String DELETE_QUERY = "DELETE FROM `portal_cooldowns` WHERE `player_id`=?";
	public static final String SELECT_QUERY = "SELECT `world_id`, `use_delay`, `reuse_time` FROM `portal_cooldowns` WHERE `player_id`=?";

	@Override
	public void loadPortalCooldowns(final Player player)
	{
		DB.select(SELECT_QUERY, new ParamReadStH()
		{
			@Override
			public void setParams(PreparedStatement stmt) throws SQLException
			{
				stmt.setInt(1, player.getObjectId());
			}

			@Override
			public void handleRead(ResultSet rset) throws SQLException
			{
				while(rset.next())
				{
					int worldId = rset.getInt("world_id");
					int useDelay = rset.getInt("use_delay");
					long reuseTime = rset.getLong("reuse_time");
										
					if(reuseTime > System.currentTimeMillis())
						player.addPortalCoolDown(worldId, reuseTime, useDelay);
				}
			}
		});
	}

	@Override
	public void storePortalCooldowns(final Player player)
	{
		deletePortalCooldowns(player);
		Map<Integer, PortalCooldown> portalCoolDowns = player.getPortalCoolDowns();

		if(portalCoolDowns == null)
			return;

		for(Map.Entry<Integer, PortalCooldown> entry : portalCoolDowns.entrySet())
		{
			final int worldId = entry.getKey();
			final long reuseTime = entry.getValue().getReuseTime();
			final int useDelay = entry.getValue().getUseDelay();

			if(reuseTime < System.currentTimeMillis())
				continue;

			DB.insertUpdate(INSERT_QUERY, new IUStH() {
				@Override
				public void handleInsertUpdate(PreparedStatement stmt) throws SQLException
				{
					stmt.setInt(1, player.getObjectId());
					stmt.setInt(2, worldId);
					stmt.setInt(3, useDelay);
					stmt.setLong(4, reuseTime);
					stmt.execute();
				}
			});
		}
	}

	private void deletePortalCooldowns(final Player player)
	{
		DB.insertUpdate(DELETE_QUERY, new IUStH()
		{
			@Override
			public void handleInsertUpdate(PreparedStatement stmt) throws SQLException
			{
				stmt.setInt(1, player.getObjectId());
				stmt.execute();
			}
		});
	}

	@Override
	public boolean supports(String arg0, int arg1, int arg2)
	{
		return MySQL5DAOUtils.supports(arg0, arg1, arg2);
	}
}
