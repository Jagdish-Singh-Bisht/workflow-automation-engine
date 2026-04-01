package com.example.workflowautomation.source;


import java.util.Map;


public interface SourceHandler {

    void fetch(Map<String, Object> context);

}
