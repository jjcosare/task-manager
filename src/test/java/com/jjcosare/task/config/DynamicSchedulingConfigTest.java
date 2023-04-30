package com.jjcosare.task.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.beanmatchers.BeanMatchers;
import com.jjcosare.task.service.TaskService;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DynamicSchedulingConfigTest {

    @InjectMocks
    DynamicSchedulingConfig dynamicSchedulingConfig;

    @Mock
    TaskService taskService;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    ScheduledTaskRegistrar scheduledTaskRegistrar;

    EasyRandom easyRandom = new EasyRandom(new EasyRandomParameters());

    @BeforeAll
    void beforeAll() {
        BeanMatchers.registerValueGenerator(() -> LocalDate.now().minusDays(easyRandom.nextInt()), LocalDate.class);
        BeanMatchers.registerValueGenerator(() -> LocalTime.now().minusMinutes(easyRandom.nextLong()), LocalTime.class);
        BeanMatchers.registerValueGenerator(() -> LocalDateTime.now().minusSeconds(easyRandom.nextInt()), LocalDateTime.class);
    }

    @Test
    void taskExecuterTest() {
        assertNotNull(dynamicSchedulingConfig.taskExecutor());
    }

    @Test
    void configureTasksTest() {
        dynamicSchedulingConfig.configureTasks(scheduledTaskRegistrar);
        verify(scheduledTaskRegistrar, times(1)).addTriggerTask(any(Runnable.class), any(Trigger.class));
    }

}
