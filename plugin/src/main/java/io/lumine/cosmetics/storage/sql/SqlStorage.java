package io.lumine.cosmetics.storage.sql;

import java.util.UUID;

import io.lumine.utils.plugin.LuminePlugin;
import io.lumine.utils.promise.Promise;
import io.lumine.utils.storage.players.Profile;
import io.lumine.utils.storage.players.adapters.SqlPlayerStorageAdapter;
import io.lumine.utils.storage.sql.SqlConnector;

public class SqlStorage<MCCosmeticsPlugin,CosmeticsProfile> extends SqlPlayerStorageAdapter {

    public SqlStorage(LuminePlugin plugin, SqlConnector connector) {
        super(plugin, connector);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void load(LuminePlugin arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void unload() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Promise load(UUID arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Promise loadByName(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Promise save(UUID arg0, Profile arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean saveSync(UUID arg0, Profile arg1) {
        // TODO Auto-generated method stub
        return false;
    }


}
