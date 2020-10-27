package org.spongepowered.downloads.artifact.readside;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.schemabuilder.Create;
import com.google.common.reflect.TypeToken;

/**
 * A CassandraColumn represents a read side column that combines the column name
 * and {@link DataType} in one place, along with handy utility methods to help
 * build queries.
 *
 * @param <T> The type.
 */
// TODO: This goes somewhere more common
@SuppressWarnings({"UnstableApiUsage"})
public record CassandraColumn<T>(String name, DataType dataType, TypeToken<T> expectedType, boolean isPartitionKey) {

    public void createColumn(final Create create) {
        if (this.isPartitionKey) {
            create.addPartitionKey(this.name, this.dataType);
        } else {
            create.addColumn(this.name, this.dataType);
        }
    }

    public void createInsertEntry(final Insert insert) {
        insert.value(this.name, QueryBuilder.bindMarker(this.name));
    }

    public void setBoundStatement(final BoundStatement statement, final T value) {
        statement.set(this.name, value, this.expectedType);
    }

}
