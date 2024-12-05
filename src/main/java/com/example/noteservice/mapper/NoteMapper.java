package com.example.noteservice.mapper;

import com.example.noteservice.entity.Note;
import com.example.noteservice.entity.Tag;
import com.example.noteservice.model.NoteDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface NoteMapper {

    @Mapping(source = "tags", target = "tags", qualifiedByName = "mapTagsToStrings")
    NoteDto toDto(Note note);

    @Mapping(source = "tags", target = "tags", qualifiedByName = "mapStringsToTags")
    Note toEntity(NoteDto dto);

    @Named("mapTagsToStrings")
    default Set<String> mapTagsToStrings(Set<Tag> tags) {
        return tags != null
                ? tags.stream().map(Tag::getName).collect(Collectors.toSet())
                : null;
    }

    @Named("mapStringsToTags")
    default Set<Tag> mapStringsToTags(Set<String> tagNames) {
        return tagNames != null
                ? tagNames.stream().map(name -> {
            Tag tag = new Tag();
            tag.setName(name);
            return tag;
        }).collect(Collectors.toSet())
                : null;
    }

}
