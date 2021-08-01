set search_path to version;

drop materialized view if exists version.versioned_tags cascade;
-- Re-crete the previous verioned_tags, this is basically a copy of what's in the 2_crete_materialized_view
-- but with the additional version_tag_value_idx
create materialized view versioned_tags as
select version.id                                                                                as version_id,
       a.id                                                                                      as artifact_internal_id,
       a.group_id                                                                                as maven_group_id,
       a.artifact_id                                                                             as maven_artifact_id,
       version.version                                                                           as maven_version,
       artifact_tag.id                                                                           as tag_id,
       artifact_tag.tag_name                                                                     as tag_name,
       ((regexp_match(version.version, artifact_tag.tag_regex))[artifact_tag.use_capture_group]) as tag_value
from artifact_versions version
         inner join artifacts a on version.artifact_id = a.id
         inner join artifact_tags artifact_tag
                    on a.id = artifact_tag.artifact_id
;
create index on versioned_tags (
                                maven_group_id, maven_artifact_id,
                                maven_version, tag_name
    );
create index versioned_tag_value_idx on versioned_tags (
                                                        maven_group_id,
                                                        maven_artifact_id,
                                                        maven_version, tag_name
    );
set search_path to public;
