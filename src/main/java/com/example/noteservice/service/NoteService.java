package com.example.noteservice.service;

import com.example.noteservice.model.NoteDto;

import java.util.List;

public interface NoteService {

    NoteDto createNote(NoteDto dto);
    List<NoteDto> getNote(String title);
    NoteDto updateNote(Long id, NoteDto dto);
    void deleteNote(Long id);

}
