package com.example.noteservice.service.impl;

import com.example.noteservice.entity.Note;
import com.example.noteservice.entity.Tag;
import com.example.noteservice.enums.Category;
import com.example.noteservice.enums.Status;
import com.example.noteservice.mapper.NoteMapper;
import com.example.noteservice.model.NoteDto;
import com.example.noteservice.repository.NoteRepository;
import com.example.noteservice.repository.TagRepository;
import com.example.noteservice.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository repository;
    private final TagRepository tagRepository;
    private final NoteMapper mapper;

    @Override
    public NoteDto createNote(NoteDto dto) {

        Set<Tag> tags = dto.getTags().stream()
                .map(tagName-> {
                    Tag tag = tagRepository.findByName(tagName);
                    if (tag != null) {
                        return tag;
                    } else {
                        Tag newTag = new Tag();
                        newTag.setName(tagName);
                        return tagRepository.save(newTag);
                    }
                }).collect(Collectors.toSet());

        Note note = mapper.toEntity(dto);
        note.setTags(tags);
        note.setCreatedAt(LocalDateTime.now());
        note.setUpdatedAt(LocalDateTime.now());
        note.setStatus(Status.ACTIVE);

        if (dto.getReminderTime() != null || !dto.getReminderTime().isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            try {
                LocalDateTime reminderTime = LocalDateTime.parse(dto.getReminderTime(), formatter);
                note.setReminderTime(reminderTime);
            } catch (DateTimeParseException e) {
                throw new RuntimeException("Invalid reminder time format", e);
            }
        } else {
            note.setReminderTime(null);
        }

        note = repository.save(note);
        return mapper.toDto(note);
    }

    @Override
    public List<NoteDto> getNote(String title) {
        List<Note> notes = repository.findNoteByTitle(title);
        return notes.stream()
                .map(map-> mapper.toDto(map))
                .collect(Collectors.toList());
    }

    @Override
    public NoteDto updateNote(Long id, NoteDto dto) {
        Note note = repository.findById(id).orElseThrow(() -> new RuntimeException("Note not found with id: " + id));
        note.setTitle(dto.getTitle());
        note.setContent(dto.getContent());
        note.setCategory(Category.valueOf(dto.getCategory()));
        note.setUpdatedAt(LocalDateTime.now());
        note.setStatus(Status.valueOf(dto.getStatus()));
        note = repository.save(note);
        return mapper.toDto(note);
    }

    @Override
    public void deleteNote(Long id) {
        repository.deleteById(id);
    }
}
