package com.jjcosare.task.dto;

import com.google.code.beanmatchers.BeanMatchers;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@JsonTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TaskDtoTest {

    @Autowired
    JacksonTester<TaskDto> jacksonTester;

    EasyRandom easyRandom = new EasyRandom(new EasyRandomParameters());

    @BeforeAll
    void beforeAll() {
        BeanMatchers.registerValueGenerator(() -> LocalDate.now().minusDays(easyRandom.nextInt()), LocalDate.class);
        BeanMatchers.registerValueGenerator(() -> LocalTime.now().minusMinutes(easyRandom.nextLong()), LocalTime.class);
        BeanMatchers.registerValueGenerator(() -> LocalDateTime.now().minusSeconds(easyRandom.nextInt()), LocalDateTime.class);
    }

    @Test
    void lombokTest() {
        assertThat(TaskDto.class, allOf(
                hasValidBeanConstructor(),
                hasValidGettersAndSetters(),
                hasValidBeanEquals(),
                hasValidBeanHashCode(),
                hasValidBeanToString()
        ));
    }

    @Test
    void jsonTest() throws Exception {
        JsonContent<TaskDto> jsonContent = jacksonTester.write(easyRandom.nextObject(TaskDto.class));
        assertNotNull(jsonContent);
    }

}
