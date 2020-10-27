package org.spongepowered.downloads.artifact.api.query;

import org.spongepowered.downloads.artifact.api.ArtifactCollection;

public sealed interface GetVersionResponse {

    final record ArtifactsAvailable(ArtifactCollection artifact) implements GetVersionResponse {}

    final record GroupUnknown(String groupId) implements GetVersionResponse {}

    final record ArtifactUnknown(String artifactId) implements GetVersionResponse {}

    final record VersionUnknown(String version) implements GetVersionResponse {}
}
