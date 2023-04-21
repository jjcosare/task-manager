package com.jjcosare.task;

import org.springframework.http.MediaType;

public final class ApiVersion {

    private ApiVersion() {
    }

    public static final String MEDIA_TYPE_LTS_API = MediaType.APPLICATION_JSON_VALUE;
    public static final String MEDIA_TYPE_V1_API = "application/vnd.jjcosare.task.api.v1+json";
    public static final String MEDIA_TYPE_V2_API = "application/vnd.jjcosare.task.api.v2+json";
    public static final String MEDIA_TYPE_V3_API = "application/vnd.jjcosare.task.api.v3+json";

}
