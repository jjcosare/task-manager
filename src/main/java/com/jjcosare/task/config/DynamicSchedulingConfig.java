package com.jjcosare.task.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.code.beanmatchers.BeanMatchers;
import com.jjcosare.task.dto.TaskDto;
import com.jjcosare.task.service.TaskService;
import lombok.extern.log4j.Log4j2;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
@Log4j2
public class DynamicSchedulingConfig implements SchedulingConfigurer {

    private final TaskService taskService;

    private final ObjectMapper objectMapper;

    private final EasyRandom easyRandom = new EasyRandom(new EasyRandomParameters().collectionSizeRange(1, 3));

    @Value("${task.random.creation.enabled}")
    private boolean isEnabled;

    @Value("${task.random.creation.min}")
    private int minSeconds;

    @Value("${task.random.creation.max}")
    private int maxSeconds;

    public DynamicSchedulingConfig(@Autowired TaskService taskService, @Autowired ObjectMapper objectMapper) {
        this.taskService = taskService;
        this.objectMapper = objectMapper;
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.objectMapper.findAndRegisterModules();
        BeanMatchers.registerValueGenerator(() -> LocalDate.now().minusDays(easyRandom.nextInt()), LocalDate.class);
        BeanMatchers.registerValueGenerator(() -> LocalTime.now().minusMinutes(easyRandom.nextLong()), LocalTime.class);
        BeanMatchers.registerValueGenerator(() -> LocalDateTime.now().minusSeconds(easyRandom.nextInt()), LocalDateTime.class);
    }

    @Bean
    public Executor taskExecutor() {
        return Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        SecureRandom secureRandom = new SecureRandom();
        taskRegistrar.setScheduler(taskExecutor());
        taskRegistrar.addTriggerTask(
                () -> {
                    if (isEnabled) {
                        TaskDto taskDto = easyRandom.nextObject(TaskDto.class);
                        try {
                            log.info("Creating task (" + objectMapper.writeValueAsString(taskDto) + ")...");
                            TaskDto taskDtoModel = taskService.postTask(taskDto);
                            log.info("Task (" + objectMapper.writeValueAsString(taskDtoModel) + ") created!");
                        } catch (JsonProcessingException e) {
                            log.error(e);
                        }
                    }
                },
                triggerContext -> {
                    Optional<Instant> lastCompletionTime =
                            Optional.ofNullable(triggerContext.lastCompletion());
                    return lastCompletionTime.orElseGet(Instant::now)
                            .plusSeconds(secureRandom.nextInt((maxSeconds - minSeconds) + 1) + (long) minSeconds);
                }
        );
    }

}
