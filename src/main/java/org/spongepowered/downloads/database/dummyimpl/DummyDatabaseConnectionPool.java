package org.spongepowered.downloads.database.dummyimpl;

import org.spongepowered.downloads.database.DatabaseConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;

public class DummyDatabaseConnectionPool implements DatabaseConnectionPool {

    @Override
    public Connection getConnection() throws SQLException {
        throw new UnsupportedOperationException("This should not be used by the dummy DB");
    }

    @Override
    public void shutdown() {

    }

}
