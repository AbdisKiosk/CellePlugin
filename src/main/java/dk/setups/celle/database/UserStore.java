package dk.setups.celle.database;

import com.j256.ormlite.dao.Dao;
import dk.setups.celle.cell.CellUser;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class UserStore extends BaseStore<Integer, CellUser> {

    public UserStore(Dao<CellUser, Integer> dao, StoreManager stores, Logger logger) {
        super(dao, stores, logger);
    }

    public void update(OfflinePlayer player) {
        get("mc_uuid", player.getUniqueId()).ifPresent(user -> {
            user.setName(player.getName());
            persist(user);
        });
    }

    public CellUser get(OfflinePlayer player) {
        return get(player.getUniqueId(), player.getName());
    }

    public Optional<CellUser> get(String name) {
        try {
            List<CellUser> result = getDao().queryRaw(
                    "SELECT * FROM cell_users WHERE LOWER(mc_name) = ? LIMIT 1",
                    getDao().getRawRowMapper(),
                    name.toLowerCase()
            ).getResults();

            return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
        } catch (Exception exception) {
            exception.printStackTrace();
            return Optional.empty();
        }
    }


    public CellUser get(UUID uuid, String name) {
        return get("mc_uuid", uuid).orElseGet(() -> {
            CellUser user = new CellUser(uuid, name);
            persist(user);
            return user;
        });
    }

    public Optional<CellUser> get(UUID uuid) {
        return get("mc_uuid", uuid);
    }
}