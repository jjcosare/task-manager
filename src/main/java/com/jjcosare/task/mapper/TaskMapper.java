package com.jjcosare.task.mapper;

import com.jjcosare.task.dto.TaskDto;
import com.jjcosare.task.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    Task toTask(TaskDto taskDto);

    TaskDto toTaskDto(Task task);

    void updateTask(TaskDto taskDto, @MappingTarget Task task);

}
