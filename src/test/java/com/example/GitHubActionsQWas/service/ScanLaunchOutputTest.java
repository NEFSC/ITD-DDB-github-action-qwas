package com.example.GitHubActionsQWas.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.env.MockEnvironment;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

class ScanLaunchOutputTest {
    @Test
    void publishesTheScanIdReturnedByAnAsynchronousLaunch(@TempDir Path directory) throws Exception {
        Path output = Files.createFile(directory.resolve("github-output"));
        MockEnvironment environment = new MockEnvironment()
                .withProperty("API_SERVER", "https://example.com")
                .withProperty("AUTH_TYPE", "BASIC")
                .withProperty("WEBAPP_ID", "123")
                .withProperty("WAIT_FOR_RESULT", "false")
                .withProperty("GITHUB_OUTPUT", output.toString());
        QualysWASScanBuilder builder = spy(new QualysWASScanBuilder(environment));
        doReturn(true).when(builder).testConnection();
        doReturn("456").when(builder).launchWasScan(any(QualysWASScanService.class));

        builder.launchWebApplicationScan();

        assertEquals("scan_id=456" + System.lineSeparator(), Files.readString(output));
    }
}
