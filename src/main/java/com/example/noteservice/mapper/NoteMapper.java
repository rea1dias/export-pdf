package com.example.noteservice.mapper;

import com.example.noteservice.entity.Note;
import com.example.noteservice.entity.Tag;
import com.example.noteservice.model.NoteDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface NoteMapper {

    @Mapping(source = "reminderTime", target = "reminderTime", qualifiedByName = "localDateTimeToString")
    @Mapping(source = "tags", target = "tags", qualifiedByName = "mapTagsToStrings")
    NoteDto toDto(Note note);

    @Mapping(source = "reminderTime", target = "reminderTime", qualifiedByName = "stringToLocalDateTime")
    @Mapping(source = "tags", target = "tags", qualifiedByName = "mapStringsToTags")
    Note toEntity(NoteDto dto);

    @Named("stringToLocalDateTime")
    default LocalDateTime stringToLocalDateTime(String dateTime) {
        if (dateTime == null || dateTime.isEmpty()) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTime, formatter);
    }

    @Named("localDateTimeToString")
    default String localDateTimeToString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

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
