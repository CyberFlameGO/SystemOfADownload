package org.spongepowered.downloads.artifact.readside;

import com.datastax.driver.core.DataType;
import com.google.common.reflect.TypeToken;

import java.util.Set;

@SuppressWarnings("UnstableApiUsage")
public final class ArtifactCollectionColumns {

    static final String ARTIFACT_COLLECTION_TABLE = "artifact_collection";
    static final String ARTIFACT_TABLE = "artifacts";

    // TODO: These go somewhere more common.
    static final CassandraColumn<String> VERSION = new CassandraColumn<>("version", DataType.text(), TypeToken.of(String.class), true);
    static final CassandraColumn<String> ARTIFACT_ID = new CassandraColumn<>("artifact_id", DataType.text(), TypeToken.of(String.class), true);
    static final CassandraColumn<String> MAVEN_COORDINATES =
            new CassandraColumn<>("maven_coordinates", DataType.text(), TypeToken.of(String.class), false);

    // Group
    static final CassandraColumn<String> GROUP_COORDINATES =
            new CassandraColumn<>("group_coordinates", DataType.text(), TypeToken.of(String.class), true);
    static final CassandraColumn<String> GROUP_NAME =
                    new CassandraColumn<>("group_name", DataType.text(), TypeToken.of(String.class), false);
    static final CassandraColumn<String> GROUP_WEBSITE =
                            new CassandraColumn<>("group_website", DataType.text(), TypeToken.of(String.class), false);

    // TODO: We're gonna have this, right?
    static final CassandraColumn<Boolean> BROKEN =
            new CassandraColumn<>("broken", DataType.cboolean(), TypeToken.of(Boolean.class), false);

    // Artifacts
    static final CassandraColumn<String> VARIANT =
            new CassandraColumn<>("variant", DataType.text(), TypeToken.of(String.class), true);
    static final CassandraColumn<String> DOWNLOAD_URL =
                    new CassandraColumn<>("download_url", DataType.text(), TypeToken.of(String.class), false);
    static final CassandraColumn<String> MD5 =
                            new CassandraColumn<>("md5", DataType.text(), TypeToken.of(String.class), false);
    static final CassandraColumn<String> SHA1 =
                                    new CassandraColumn<>("sha1", DataType.text(), TypeToken.of(String.class), false);

    // Collection Only
    static final CassandraColumn<Set<String>> SET_VARIANT =
            new CassandraColumn<>("variant", DataType.set(DataType.text()), new TypeToken<>() {}, false);

    private ArtifactCollectionColumns() {}

}
