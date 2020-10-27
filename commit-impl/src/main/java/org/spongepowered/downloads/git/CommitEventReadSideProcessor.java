package org.spongepowered.downloads.git;

import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.ReadSideProcessor;
import org.pcollections.PSequence;

public final class CommitEventReadSideProcessor extends ReadSideProcessor<CommitEvent> {

    @Override
    public ReadSideHandler<CommitEvent> buildHandler() {
        return null;
    }

    @Override
    public PSequence<AggregateEventTag<CommitEvent>> aggregateTags() {
        return CommitEvent.INSTANCE.allTags();
    }

}
