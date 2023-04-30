package com.jjcosare.task.controller;

import com.epages.restdocs.apispec.HeaderDescriptorWithType;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.beanmatchers.BeanMatchers;
import com.jjcosare.task.ApiVersion;
import com.jjcosare.task.dto.TaskDto;
import com.jjcosare.task.service.TaskService;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Collectors;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(TaskController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TaskControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    EasyRandom easyRandom = new EasyRandom(new EasyRandomParameters());


    @BeforeAll
    void beforeAll() {
        BeanMatchers.registerValueGenerator(() -> LocalDate.now().minusDays(easyRandom.nextInt()), LocalDate.class);
        BeanMatchers.registerValueGenerator(() -> LocalTime.now().minusMinutes(easyRandom.nextLong()), LocalTime.class);
        BeanMatchers.registerValueGenerator(() -> LocalDateTime.now().minusSeconds(easyRandom.nextInt()), LocalDateTime.class);

        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    }

    @MockBean
    @Qualifier(value = "taskServiceImpl")
    TaskService taskService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    void postTaskLtsTest() throws Exception {
        when(taskService.postTask(any(TaskDto.class)))
                .thenReturn(easyRandom.nextObject(TaskDto.class));

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/api/task")
                        .contentType(MediaType.parseMediaType(ApiVersion.MEDIA_TYPE_LTS_API))
                        .content(objectMapper.writeValueAsString(easyRandom.nextObject(TaskDto.class))))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andDo(MockMvcResultHandlers.print())
                .andDo(document("task-post-lts", ResourceSnippetParameters.builder()
                        .description("Create a task")
                        .formParameters(parameterWithName("taskDto").description("dto for task"))
                ));
    }

    @Test
    void postTaskV1Test() throws Exception {
        when(taskService.postTask(any(TaskDto.class)))
                .thenReturn(easyRandom.nextObject(TaskDto.class));

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/api/task")
                        .contentType(MediaType.parseMediaType(ApiVersion.MEDIA_TYPE_V1_API))
                        .content(objectMapper.writeValueAsString(easyRandom.nextObject(TaskDto.class))))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andDo(MockMvcResultHandlers.print())
                .andDo(document("task-post-v1", ResourceSnippetParameters.builder()
                        .description("Create a task")
                        .formParameters(parameterWithName("taskDto").description("dto for task"))
                ));
    }

    @Test
    void getTaskLtsTest() throws Exception {
        when(taskService.getTask())
                .thenReturn(easyRandom.objects(TaskDto.class, 3).collect(Collectors.toList()));

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/task")
                        .contentType(MediaType.parseMediaType(ApiVersion.MEDIA_TYPE_LTS_API)))
                .andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print())
                .andDo(document("task-get-lts", ResourceSnippetParameters.builder()
                        .description("Get all task")
                        .responseHeaders(new HeaderDescriptorWithType(ApiVersion.MEDIA_TYPE_LTS_API))
                ));
    }

    @Test
    void getTaskV1Test() throws Exception {
        when(taskService.getTask())
                .thenReturn(easyRandom.objects(TaskDto.class, 3).collect(Collectors.toList()));

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/task")
                        .contentType(MediaType.parseMediaType(ApiVersion.MEDIA_TYPE_V1_API)))
                .andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print())
                .andDo(document("task-get-v1", ResourceSnippetParameters.builder()
                        .description("Get all task")
                        .responseHeaders(new HeaderDescriptorWithType(ApiVersion.MEDIA_TYPE_V1_API))
                ));
    }

    @Test
    void getTaskBySortV2Test() throws Exception {
        when(taskService.getTaskBySort(any(Sort.class)))
                .thenReturn(easyRandom.objects(TaskDto.class, 3).collect(Collectors.toList()));

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/task")
                        .contentType(MediaType.parseMediaType(ApiVersion.MEDIA_TYPE_V2_API))
                        .param("sort", "id,desc"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print())
                .andDo(document("task-bySort-v2", ResourceSnippetParameters.builder()
                        .description("Get all task with sorting")
                        .queryParameters(parameterWithName("sort").description("sort by field name"))
                ));
    }

    @Test
    void getTaskByPageableV3Test() throws Exception {
        when(taskService.getTaskByPageable(any(Pageable.class)))
                .thenReturn(new PageImpl<>(easyRandom.objects(TaskDto.class, 3).collect(Collectors.toList())));

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/task")
                        .contentType(MediaType.parseMediaType(ApiVersion.MEDIA_TYPE_V3_API))
                        .param("page", "5")
                        .param("size", "10")
                        .param("sort", "id,desc"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print())
                .andDo(document("task-byPageable-v3", ResourceSnippetParameters.builder()
                        .description("Get all task with pagination")
                        .queryParameters(parameterWithName("page").description("pagination page"))
                        .queryParameters(parameterWithName("size").description("pagination size"))
                        .queryParameters(parameterWithName("sort").description("pagination sort by field name"))
                ));
    }

    @Test
    void getTaskByIdLtsTest() throws Exception {
        when(taskService.getTaskById(anyLong()))
                .thenReturn(easyRandom.nextObject(TaskDto.class));

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/task/{id}", easyRandom.nextLong())
                        .contentType(MediaType.parseMediaType(ApiVersion.MEDIA_TYPE_LTS_API)))
                .andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print())
                .andDo(document("task-byId-lts", ResourceSnippetParameters.builder()
                        .description("Get a task by id")
                        .pathParameters(parameterWithName("id").description("id of task to get"))));
    }

    @Test
    void getTaskByIdV1Test() throws Exception {
        when(taskService.getTaskById(anyLong()))
                .thenReturn(easyRandom.nextObject(TaskDto.class));

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/task/{id}", easyRandom.nextLong())
                        .contentType(MediaType.parseMediaType(ApiVersion.MEDIA_TYPE_V1_API)))
                .andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print())
                .andDo(document("task-byId-v1", ResourceSnippetParameters.builder()
                        .description("Get a task by id")
                        .pathParameters(parameterWithName("id").description("id of task to get"))));
    }

    @Test
    void putTaskLtsTest() throws Exception {
        when(taskService.putTask(anyLong(), any(TaskDto.class)))
                .thenReturn(easyRandom.nextObject(TaskDto.class));

        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/api/task/{id}", easyRandom.nextLong())
                        .contentType(MediaType.parseMediaType(ApiVersion.MEDIA_TYPE_LTS_API))
                        .content(objectMapper.writeValueAsString(easyRandom.nextObject(TaskDto.class))))
                .andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print())
                .andDo(document("task-put-lts", ResourceSnippetParameters.builder()
                        .description("Update a task by id and dto")
                        .pathParameters(parameterWithName("id").description("id of task to update"))
                        .formParameters(parameterWithName("taskDto").description("dto for task that contains updated data"))
                ));
    }

    @Test
    void putTaskV1Test() throws Exception {
        when(taskService.putTask(anyLong(), any(TaskDto.class)))
                .thenReturn(easyRandom.nextObject(TaskDto.class));

        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/api/task/{id}", easyRandom.nextLong())
                        .contentType(MediaType.parseMediaType(ApiVersion.MEDIA_TYPE_V1_API))
                        .content(objectMapper.writeValueAsString(easyRandom.nextObject(TaskDto.class))))
                .andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print())
                .andDo(document("task-put-v1", ResourceSnippetParameters.builder()
                        .description("Update a task by id and dto")
                        .pathParameters(parameterWithName("id").description("id of task to update"))
                        .formParameters(parameterWithName("taskDto").description("dto for task that contains updated data"))
                ));
    }

    @Test
    void deleteTaskLtsTest() throws Exception {
        doNothing().when(taskService).deleteTask(anyLong());

        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/task/{id}", easyRandom.nextLong())
                        .contentType(MediaType.parseMediaType(ApiVersion.MEDIA_TYPE_LTS_API)))
                .andExpect(MockMvcResultMatchers.status().isNoContent()).andDo(MockMvcResultHandlers.print())
                .andDo(document("task-delete-lts", ResourceSnippetParameters.builder()
                        .description("Delete a task by id")
                        .pathParameters(parameterWithName("id").description("id of task to delete"))));
    }

    @Test
    void deleteTaskV1Test() throws Exception {
        doNothing().when(taskService).deleteTask(anyLong());

        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/task/{id}", easyRandom.nextLong())
                        .contentType(MediaType.parseMediaType(ApiVersion.MEDIA_TYPE_V1_API)))
                .andExpect(MockMvcResultMatchers.status().isNoContent()).andDo(MockMvcResultHandlers.print())
                .andDo(document("task-delete-v1", ResourceSnippetParameters.builder()
                        .description("Delete a task by id")
                        .pathParameters(parameterWithName("id").description("id of task to delete"))));
    }

}
