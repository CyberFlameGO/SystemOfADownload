package org.spongepowered.downloads.rest.objects.v1;

/**
 * Represents a project
 */
public class BasicProject {

    private final String groupId;
    private final String artifactId;

    /**
     * Constructs this instance.
     *
     * @param groupId The group ID of the project
     * @param artifactId The artifact ID of the project
     */
    public BasicProject(String groupId, String artifactId) {
        this.groupId = groupId;
        this.artifactId = artifactId;
    }

}
