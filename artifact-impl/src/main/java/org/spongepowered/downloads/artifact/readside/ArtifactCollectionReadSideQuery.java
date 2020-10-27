package org.spongepowered.downloads.artifact.readside;

import akka.persistence.cassandra.session.javadsl.CassandraSession;
import com.datastax.driver.core.PreparedStatement;
import com.google.inject.Inject;
import org.spongepowered.downloads.artifact.api.query.GetVersionResponse;

import java.util.concurrent.CompletionStage;

// TODO: Project table into model that can then be projected into a viewmodel.
public final class ArtifactCollectionReadSideQuery {

    private final CassandraSession cassandraSession;
    private PreparedStatement versionQuery;

    @Inject
    public ArtifactCollectionReadSideQuery(CassandraSession cassandraSession) {
        this.cassandraSession = cassandraSession;
        this.prepare();
    }

    private void prepare(final CassandraSession cassandraSession) {
       /* cassandraSession.prepare(
                QueryBuilder.select()
                        .all()
                        .from(Constants.ReadSide.KEYSPACE, ArtifactCollectionColumns.ARTIFACT_TABLE)
                        .where()


        ).thenAccept(x -> this.versionQuery = x); */
    }

    public CompletionStage<GetVersionResponse> getVersion(final String groupId, final String artifactId, final String artifactVersion) {
        return null;
    }

}
