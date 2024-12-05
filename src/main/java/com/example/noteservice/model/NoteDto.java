package com.example.noteservice.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class NoteDto {
    private Long id;
    private String title;
    private String content;
    private String category;
    private String createdAt;
    private String updatedAt;
    private String status;
    private Set<String> tags;
    private String reminderTime;
}
