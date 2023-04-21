package com.jjcosare.task.controller;

import com.jjcosare.task.ApiVersion;
import com.jjcosare.task.dto.TaskDto;
import com.jjcosare.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(@Qualifier("taskServiceImpl") TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping(
            produces = {ApiVersion.MEDIA_TYPE_V1_API, ApiVersion.MEDIA_TYPE_LTS_API}
    )
    public ResponseEntity<TaskDto> postTask(@RequestBody TaskDto taskDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.postTask(taskDto));
    }

    @GetMapping(
            produces = {ApiVersion.MEDIA_TYPE_V1_API, ApiVersion.MEDIA_TYPE_LTS_API}
    )
    public ResponseEntity<List<TaskDto>> getTask() {
        return ResponseEntity.ok(taskService.getTask());
    }

    @GetMapping(
            produces = {ApiVersion.MEDIA_TYPE_V2_API}
    )
    public ResponseEntity<List<TaskDto>> getTaskBySort(@RequestParam Sort sort) {
        return ResponseEntity.ok(taskService.getTaskBySort(sort));
    }

    @GetMapping(
            produces = {ApiVersion.MEDIA_TYPE_V3_API}
    )
    public ResponseEntity<Page<TaskDto>> getTaskByPageable(@RequestParam Pageable pageable) {
        return ResponseEntity.ok(taskService.getTaskByPageable(pageable));
    }

    @GetMapping(
            value = "/{id}",
            produces = {ApiVersion.MEDIA_TYPE_V1_API, ApiVersion.MEDIA_TYPE_LTS_API}
    )
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PutMapping(
            value = "/{id}",
            produces = {ApiVersion.MEDIA_TYPE_V1_API, ApiVersion.MEDIA_TYPE_LTS_API}
    )
    public ResponseEntity<TaskDto> putTask(@RequestBody TaskDto taskDto, @PathVariable Long id) {
        return ResponseEntity.ok(taskService.putTask(id, taskDto));
    }

    @DeleteMapping(
            value = "/{id}",
            produces = {ApiVersion.MEDIA_TYPE_V1_API, ApiVersion.MEDIA_TYPE_LTS_API}
    )
    public ResponseEntity<TaskDto> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

}
