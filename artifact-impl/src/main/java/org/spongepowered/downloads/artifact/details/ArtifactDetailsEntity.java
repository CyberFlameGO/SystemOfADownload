/*
 * This file is part of SystemOfADownload, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://spongepowered.org/>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.downloads.artifact.details;

import akka.NotUsed;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.cluster.sharding.typed.javadsl.EntityTypeKey;
import akka.persistence.typed.PersistenceId;
import akka.persistence.typed.javadsl.CommandHandlerWithReply;
import akka.persistence.typed.javadsl.EventHandler;
import akka.persistence.typed.javadsl.EventSourcedBehaviorWithEnforcedReplies;
import org.spongepowered.downloads.artifact.details.state.DetailsState;
import org.spongepowered.downloads.artifact.details.state.EmptyState;
import org.spongepowered.downloads.artifact.details.state.PopulatedState;

import java.util.List;

public class ArtifactDetailsEntity
    extends EventSourcedBehaviorWithEnforcedReplies<DetailsCommand, DetailsEvent, DetailsState> {
    public static EntityTypeKey<DetailsCommand> ENTITY_TYPE_KEY = EntityTypeKey.create(
        DetailsCommand.class, "DetailsEntity");
    private final String artifactId;
    private final ActorContext<DetailsCommand> ctx;

    private ArtifactDetailsEntity(ActorContext<DetailsCommand> ctx, String entityId, PersistenceId persistenceId) {
        super(persistenceId);
        this.artifactId = entityId;
        this.ctx = ctx;
    }

    public static Behavior<DetailsCommand> create(String entityId, PersistenceId persistenceId) {
        return Behaviors.setup(ctx -> new ArtifactDetailsEntity(ctx, entityId, persistenceId));
    }

    @Override
    public DetailsState emptyState() {
        return new EmptyState();
    }

    @Override
    public EventHandler<DetailsState, DetailsEvent> eventHandler() {
        final var builder = this.newEventHandlerBuilder();

        builder.forAnyState()
            .onEvent(
                DetailsEvent.ArtifactRegistered.class,
                (state, event) -> new PopulatedState(event.coordinates, state.displayName(), state.website(), state.gitRepository(), state.issues())
            )
            .onEvent(DetailsEvent.ArtifactDetailsUpdated.class,
                (state, event) -> new PopulatedState(event.coordinates, event.displayName, state.website(), state.gitRepository(), state.issues())
            );

        return builder.build();
    }

    @Override
    public CommandHandlerWithReply<DetailsCommand, DetailsEvent, DetailsState> commandHandler() {
        final var builder = this.newCommandHandlerWithReplyBuilder();

        builder.forNullState();
        builder.forStateType(EmptyState.class)
            .onCommand(
                DetailsCommand.RegisterArtifact.class,
                (cmd) -> this.Effect()
                    .persist(List.of(
                        new DetailsEvent.ArtifactRegistered(cmd.coordinates()),
                        new DetailsEvent.ArtifactDetailsUpdated(cmd.coordinates(), cmd.displayName())
                    ))
                    .thenReply(cmd.replyTo(), (state) -> NotUsed.notUsed())
            );

        builder.forStateType(PopulatedState.class)
            .onCommand(
                DetailsCommand.RegisterArtifact.class,
                (s, cmd) -> this.Effect().reply(cmd.replyTo(), NotUsed.notUsed())
            );

        return builder.build();
    }
}
