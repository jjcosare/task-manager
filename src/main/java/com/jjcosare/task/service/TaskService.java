package com.jjcosare.task.service;

import com.jjcosare.task.dto.TaskDto;
import com.jjcosare.task.mapper.TaskMapper;
import com.jjcosare.task.model.Task;
import com.jjcosare.task.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface TaskService {

    TaskRepository getTaskRepository();

    default TaskDto postTask(TaskDto taskDto) {
        return TaskMapper.INSTANCE.toTaskDto(getTaskRepository().save(TaskMapper.INSTANCE.toTask(taskDto)));
    }

    default List<TaskDto> getTask() {
        return getTaskBySort(Sort.by(Sort.Order.by(Task.Fields.createdAt)));
    }

    default List<TaskDto> getTaskBySort(Sort sort) {
        return getTaskRepository().findAll(sort).stream()
                .map(TaskMapper.INSTANCE::toTaskDto).toList();
    }

    default Page<TaskDto> getTaskByPageable(Pageable pageable) {
        return getTaskRepository().findAll(pageable).map(TaskMapper.INSTANCE::toTaskDto);
    }

    default TaskDto getTaskById(Long id) {
        return TaskMapper.INSTANCE.toTaskDto(getTaskRepository().findById(id).orElse(null));
    }

    default TaskDto putTask(Long id, TaskDto taskDto) {
        Task model = getTaskRepository().findById(id).orElse(new Task());
        TaskMapper.INSTANCE.updateTask(taskDto, model);
        return TaskMapper.INSTANCE.toTaskDto(getTaskRepository().save(model));
    }

    default void deleteTask(Long id) {
        getTaskRepository().deleteById(id);
    }

}
