package com.jjcosare.task.dto;

import com.jjcosare.task.model.Priority;
import com.jjcosare.task.model.Status;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@FieldNameConstants
public class TaskDto {

    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDate dueDate;

    private LocalDateTime resolvedAt;

    private String title;

    private String description;

    private Priority priority;

    private Status status;

}
