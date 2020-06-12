package org.spongepowered.downloads.database.dummyimpl;

import com.google.common.collect.ImmutableList;
import org.spongepowered.downloads.database.DatabasePersistence;
import org.spongepowered.downloads.pojo.data.Downloadable;
import org.spongepowered.downloads.pojo.query.DownloadableQuery;

import java.util.List;

public class DummyDatabasePersistence implements DatabasePersistence {

    @Override
    public List<Downloadable> getDownloadable(DownloadableQuery query) {
        return ImmutableList.of();
    }

    @Override
    public void createDownloadable(Downloadable downloadable) {

    }
}
