package com.jjcosare.task.service;

import com.google.code.beanmatchers.BeanMatchers;
import com.jjcosare.task.dto.TaskDto;
import com.jjcosare.task.model.Task;
import com.jjcosare.task.repository.TaskRepository;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TaskServiceImplTest {

    @InjectMocks
    TaskServiceImpl taskServiceImpl;

    @Mock
    TaskRepository taskRepository;

    EasyRandom easyRandom = new EasyRandom(new EasyRandomParameters());

    @BeforeAll
    void beforeAll() {
        BeanMatchers.registerValueGenerator(() -> LocalDate.now().minusDays(easyRandom.nextInt()), LocalDate.class);
        BeanMatchers.registerValueGenerator(() -> LocalTime.now().minusMinutes(easyRandom.nextLong()), LocalTime.class);
        BeanMatchers.registerValueGenerator(() -> LocalDateTime.now().minusSeconds(easyRandom.nextInt()), LocalDateTime.class);
    }

    @Test
    void postTaskTest() {
        TaskServiceImpl taskServiceImplSpy = spy(taskServiceImpl);
        when(taskServiceImplSpy.getTaskRepository()).thenReturn(taskRepository);
        when(taskRepository.save(any(Task.class))).thenReturn(easyRandom.nextObject(Task.class));
        assertNotNull(taskServiceImplSpy.postTask(easyRandom.nextObject(TaskDto.class)));
    }

    @Test
    void getTaskTest() {
        TaskServiceImpl taskServiceImplSpy = spy(taskServiceImpl);
        when(taskServiceImplSpy.getTaskRepository()).thenReturn(taskRepository);
        when(taskRepository.findAll(any(Sort.class))).thenReturn(easyRandom.objects(Task.class, 3).collect(Collectors.toList()));
        assertNotNull(taskServiceImplSpy.getTask());
    }

    @Test
    void getTaskBySortTest() {
        TaskServiceImpl taskServiceImplSpy = spy(taskServiceImpl);
        when(taskServiceImplSpy.getTaskRepository()).thenReturn(taskRepository);
        when(taskRepository.findAll(any(Sort.class))).thenReturn(easyRandom.objects(Task.class, 3).collect(Collectors.toList()));
        assertNotNull(taskServiceImplSpy.getTaskBySort(Sort.by(Sort.DEFAULT_DIRECTION, Task.Fields.createdAt)));
    }

    @Test
    void getTaskByPageableTest() {
        TaskServiceImpl taskServiceImplSpy = spy(taskServiceImpl);
        when(taskServiceImplSpy.getTaskRepository()).thenReturn(taskRepository);
        when(taskRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(easyRandom.objects(Task.class, 3).collect(Collectors.toList())));
        assertNotNull(taskServiceImplSpy.getTaskByPageable(Pageable.ofSize(3)));
    }

    @Test
    void getTaskByIdTest() {
        TaskServiceImpl taskServiceImplSpy = spy(taskServiceImpl);
        when(taskServiceImplSpy.getTaskRepository()).thenReturn(taskRepository);
        when(taskRepository.findById(any(Long.class))).thenReturn(Optional.of(easyRandom.nextObject(Task.class)));
        assertNotNull(taskServiceImplSpy.getTaskById(easyRandom.nextLong()));
    }

    @Test
    void putTaskTest() {
        TaskServiceImpl taskServiceImplSpy = spy(taskServiceImpl);
        when(taskServiceImplSpy.getTaskRepository()).thenReturn(taskRepository);
        when(taskRepository.findById(any(Long.class))).thenReturn(Optional.of(easyRandom.nextObject(Task.class)));
        when(taskRepository.save(any(Task.class))).thenReturn(easyRandom.nextObject(Task.class));
        assertNotNull(taskServiceImplSpy.putTask(easyRandom.nextLong(), easyRandom.nextObject(TaskDto.class)));
    }

    @Test
    void deleteTaskTest() {
        TaskServiceImpl taskServiceImplSpy = spy(taskServiceImpl);
        when(taskServiceImplSpy.getTaskRepository()).thenReturn(taskRepository);
        doNothing().when(taskRepository).deleteById(any(Long.class));
        taskServiceImplSpy.deleteTask(easyRandom.nextLong());
        assertNull(null);
    }

}
