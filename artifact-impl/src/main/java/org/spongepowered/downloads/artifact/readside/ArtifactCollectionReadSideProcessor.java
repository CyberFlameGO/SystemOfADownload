package org.spongepowered.downloads.artifact.readside;

import akka.Done;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.schemabuilder.SchemaBuilder;
import com.google.inject.Inject;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.ReadSideProcessor;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraReadSide;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;
import io.vavr.collection.List;
import org.pcollections.PSequence;
import org.spongepowered.downloads.artifact.api.ArtifactCollection;
import org.spongepowered.downloads.artifact.collection.ArtifactCollectionEntity;
import org.spongepowered.downloads.utils.Constants;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.BiConsumer;


@SuppressWarnings("UnstableApiUsage")
public final class ArtifactCollectionReadSideProcessor extends ReadSideProcessor<ArtifactCollectionEntity.Event> {

    private static final String ARTIFACT_OFFSET_ID = "artifact_offset";

    // General information

    private final CassandraSession session;
    private final CassandraReadSide readSide;

    private PreparedStatement createInsertArtifactStatement;
    private PreparedStatement createInsertArtifactCollectionStatement;

    @Inject
    public ArtifactCollectionReadSideProcessor(final CassandraSession session, final CassandraReadSide readSide) {
        this.session = session;
        this.readSide = readSide;
    }

    @Override
    public ReadSideHandler<ArtifactCollectionEntity.Event> buildHandler() {
        final var builder =
                this.readSide.<ArtifactCollectionEntity.Event>builder(ArtifactCollectionReadSideProcessor.ARTIFACT_OFFSET_ID);
        builder.setGlobalPrepare(this::globalPrepare);
        builder.setPrepare(this::createPreparedStatements);
        builder.setEventHandler(ArtifactCollectionEntity.Event.CollectionRegistered.class, this::writeArtifacts);

        return builder.build();
    }

    @Override
    public PSequence<AggregateEventTag<ArtifactCollectionEntity.Event>> aggregateTags() {
        return ArtifactCollectionEntity.Event.INSTANCE.allTags();
    }

    private CompletionStage<Done> globalPrepare() {
        return CompletableFuture.allOf(this.createArtifactCollectionTable(), this.createArtifactTable()).thenApply(x -> Done.getInstance());
    }

    private CompletableFuture<Done> createArtifactCollectionTable() {
        final var schemaCreator = SchemaBuilder
                .createTable(Constants.ReadSide.KEYSPACE, ArtifactCollectionColumns.ARTIFACT_COLLECTION_TABLE)
                .ifNotExists();
        this.setupCommonColumns(CassandraColumn::createColumn, schemaCreator);

        ArtifactCollectionColumns.SET_VARIANT.createColumn(schemaCreator);
        return this.session.executeCreateTable(schemaCreator.getQueryString()).toCompletableFuture();
    }

    private CompletableFuture<Done> createArtifactTable() {
        final var schemaCreator = SchemaBuilder
                .createTable(Constants.ReadSide.KEYSPACE, ArtifactCollectionColumns.ARTIFACT_TABLE)
                .ifNotExists();
        this.setupCommonColumns(CassandraColumn::createColumn, schemaCreator);

        // Artifact
        ArtifactCollectionColumns.VARIANT.createColumn(schemaCreator);
        ArtifactCollectionColumns.DOWNLOAD_URL.createColumn(schemaCreator);
        ArtifactCollectionColumns.MD5.createColumn(schemaCreator);
        ArtifactCollectionColumns.SHA1.createColumn(schemaCreator);
        return this.session.executeCreateTable(schemaCreator.getQueryString()).toCompletableFuture();
    }

    private CompletionStage<Done> createPreparedStatements(AggregateEventTag<ArtifactCollectionEntity.Event> eventAggregateEventTag) {
        return CompletableFuture.allOf(
                this.createArtifactInsertStatement(),
                this.createArtifactCollectionInsertStatement()
        ).thenApply(x -> Done.getInstance());
    }

    private CompletableFuture<Void> createArtifactCollectionInsertStatement() {
        final var insertQuery = QueryBuilder.insertInto(Constants.ReadSide.KEYSPACE, ArtifactCollectionColumns.ARTIFACT_COLLECTION_TABLE);
        this.setupCommonColumns(CassandraColumn::createInsertEntry, insertQuery);

        ArtifactCollectionColumns.SET_VARIANT.createInsertEntry(insertQuery);
        return this.session.prepare(insertQuery.getQueryString()).thenAccept(x -> this.createInsertArtifactCollectionStatement = x).toCompletableFuture();
    }

    private CompletableFuture<Void> createArtifactInsertStatement() {
        final var insertQuery = QueryBuilder.insertInto(Constants.ReadSide.KEYSPACE, ArtifactCollectionColumns.ARTIFACT_TABLE);
        this.setupCommonColumns(CassandraColumn::createInsertEntry, insertQuery);

        ArtifactCollectionColumns.VARIANT.createInsertEntry(insertQuery);
        ArtifactCollectionColumns.DOWNLOAD_URL.createInsertEntry(insertQuery);
        ArtifactCollectionColumns.MD5.createInsertEntry(insertQuery);
        ArtifactCollectionColumns.SHA1.createInsertEntry(insertQuery);
        return this.session.prepare(insertQuery.getQueryString()).thenAccept(x -> this.createInsertArtifactStatement = x).toCompletableFuture();
    }

    private CompletionStage<java.util.List<BoundStatement>> writeArtifacts(final ArtifactCollectionEntity.Event.CollectionRegistered event) {
        return CompletableFuture.supplyAsync(() -> List.ofAll(this.createArtifactInsertStatements(event.collection()))
                .append(this.createArtifactCollectionInsertStatements(event.collection()))
                .toJavaList());
    }

    private List<BoundStatement> createArtifactInsertStatements(final ArtifactCollection artifactCollection) {
        return List.ofAll(artifactCollection.getArtifactComponents())
                .map(x -> {
                        final var commonQuery = this.createInsertArtifactStatement.bind();
                        this.setValuesToCommonColumns(commonQuery, artifactCollection);
                        ArtifactCollectionColumns.VARIANT.setBoundStatement(commonQuery, x._1);
                        ArtifactCollectionColumns.DOWNLOAD_URL.setBoundStatement(commonQuery, x._2.downloadUrl());
                        ArtifactCollectionColumns.MD5.setBoundStatement(commonQuery, x._2.md5());
                        ArtifactCollectionColumns.SHA1.setBoundStatement(commonQuery, x._2.sha1());
                        return commonQuery;
                });
    }

    private BoundStatement createArtifactCollectionInsertStatements(final ArtifactCollection artifactCollection) {
        final var commonQuery = this.createInsertArtifactCollectionStatement.bind();
        this.setValuesToCommonColumns(commonQuery, artifactCollection);
        ArtifactCollectionColumns.SET_VARIANT.setBoundStatement(commonQuery,
                artifactCollection.getArtifactComponents().keySet().toJavaSet());
        return commonQuery;
    }

    private void setValuesToCommonColumns(final BoundStatement boundStatement, final ArtifactCollection collection) {
        ArtifactCollectionColumns.ARTIFACT_ID.setBoundStatement(boundStatement, collection.getArtifactId());
        ArtifactCollectionColumns.VERSION.setBoundStatement(boundStatement, collection.getVersion());
        ArtifactCollectionColumns.MAVEN_COORDINATES.setBoundStatement(boundStatement, collection.getMavenCoordinates());
        ArtifactCollectionColumns.BROKEN.setBoundStatement(boundStatement, false);
        ArtifactCollectionColumns.GROUP_COORDINATES.setBoundStatement(boundStatement, collection.getGroup().getGroupCoordinates());
        ArtifactCollectionColumns.GROUP_NAME.setBoundStatement(boundStatement, collection.getGroup().getName());
        ArtifactCollectionColumns.GROUP_WEBSITE.setBoundStatement(boundStatement, collection.getGroup().getWebsite().toString());
    }

    private <T> void setupCommonColumns(final BiConsumer<CassandraColumn<?>, T> action, final T obj) {
        action.accept(ArtifactCollectionColumns.ARTIFACT_ID, obj);
        action.accept(ArtifactCollectionColumns.VERSION, obj);
        action.accept(ArtifactCollectionColumns.MAVEN_COORDINATES, obj);
        action.accept(ArtifactCollectionColumns.BROKEN, obj);
        action.accept(ArtifactCollectionColumns.GROUP_COORDINATES, obj);
        action.accept(ArtifactCollectionColumns.GROUP_NAME, obj);
        action.accept(ArtifactCollectionColumns.GROUP_WEBSITE, obj);
    }

}
