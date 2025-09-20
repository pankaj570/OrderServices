package com.example.orders.mcptools;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpConfig {
    @Bean
    public ToolCallbackProvider orderTools(OrderMcpTool orderMcpTool) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(orderMcpTool) // picks up methods annotated with @Tool
                .build();
    }
}