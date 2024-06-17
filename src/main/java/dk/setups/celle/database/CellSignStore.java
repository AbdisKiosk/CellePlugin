package dk.setups.celle.database;

import com.j256.ormlite.dao.Dao;
import dk.setups.celle.sign.CellSign;

import java.util.logging.Logger;

public class CellSignStore extends BaseStore<Integer, CellSign> {

    public CellSignStore(Dao<CellSign, Integer> dao, StoreManager stores, Logger logger) {
        super(dao, stores, logger);
    }

    public boolean delete(int x, int y, int z, String world) {
        try {
            CellSign sign = getDao().queryBuilder()
                    .where()
                    .eq("x", x)
                    .and()
                    .eq("y", y)
                    .and()
                    .eq("z", z)
                    .and()
                    .eq("world", world)
                    .queryForFirst();

            if(sign == null) {
                return false;
            }

            getDao().delete(sign);
        } catch (Exception exception) {
            getLogger().severe("Failed to delete sign at " + x + ", " + y + ", " + z + " in world " + world);
            return false;
        }

        return true;
    }

}
