package com.app.misc.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(access = AccessLevel.PUBLIC)
public class DownloadReportSNSPayload {
    String resourceId;
    String fileName;
    String classId;
    String className;
    String provider;
    String tenantId;
}
