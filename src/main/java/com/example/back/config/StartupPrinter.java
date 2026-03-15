package com.example.back.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class StartupPrinter implements ApplicationRunner {

    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";
    private static final String BOLD = "\u001B[1m";
    private static final String BLUE = "\u001B[34m";

    @Autowired
    private Environment environment;

    @Autowired
    private DataSource dataSource;

    @Value("${spring.application.name:unknown}")
    private String applicationName;

    @Value("${project.version:0.0.1-SNAPSHOT}")
    private String version;

    @Override
    public void run(ApplicationArguments args) {
        printStartupInfo();
    }

    private void printStartupInfo() {
        String[] activeProfiles = environment.getActiveProfiles();
        String env = activeProfiles.length > 0 ? String.join(", ", activeProfiles) : "default";
        String port = environment.getProperty("server.port", "8080");
        String dbUrl = environment.getProperty("spring.datasource.url", "unknown");
        String dbType = extractDbType(dbUrl);
        String dbAddress = extractDbAddress(dbUrl);
        boolean dbConnected = checkDatabaseConnection();
        String startupTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));

        StringBuilder banner = new StringBuilder();
        banner.append("\n");
        banner.append(CYAN).append("╔══════════════════════════════════════════════════════════════╗").append(RESET).append("\n");
        banner.append(CYAN).append("║").append(RESET).append(BOLD).append("                    项目启动信息                              ").append(RESET).append(CYAN).append("║").append(RESET).append("\n");
        banner.append(CYAN).append("╠══════════════════════════════════════════════════════════════╣").append(RESET).append("\n");
        banner.append(formatLine("项目名称", applicationName));
        banner.append(formatLine("版本号", version));
        banner.append(formatLine("环境配置", formatEnv(env)));
        banner.append(formatLine("服务端口", GREEN + port + RESET));
        banner.append(formatLine("启动时间", startupTime));
        banner.append(CYAN).append("╠══════════════════════════════════════════════════════════════╣").append(RESET).append("\n");
        banner.append(formatLine("数据库类型", dbType));
        banner.append(formatLine("数据库地址", dbAddress));
        banner.append(formatDbStatusLine(dbConnected));
        banner.append(CYAN).append("╚══════════════════════════════════════════════════════════════╝").append(RESET).append("\n");

        log.info(banner.toString());
    }

    private String formatLine(String label, String value) {
        return CYAN + "║" + RESET + String.format("  %-12s: %s", label, value) + getPadding(label, value) + CYAN + "║" + RESET + "\n";
    }

    private String formatDbStatusLine(boolean connected) {
        String status = connected ? GREEN + "已连接" + RESET : RED + "未连接" + RESET;
        String line = String.format("  %-12s: %s", "数据库状态", status);
        int visibleLen = 12 + 2 + (connected ? 4 : 4);
        int padding = 62 - visibleLen - 15;
        return CYAN + "║" + RESET + line + " ".repeat(Math.max(0, padding)) + CYAN + "║" + RESET + "\n";
    }

    private String getPadding(String label, String value) {
        int visibleLen = 12 + 2 + value.replaceAll("\u001B\\[[;\\d]*m", "").length();
        int padding = 62 - visibleLen;
        return " ".repeat(Math.max(0, padding));
    }

    private String formatEnv(String env) {
        if ("dev".equalsIgnoreCase(env) || "development".equalsIgnoreCase(env)) {
            return YELLOW + env + RESET;
        } else if ("prod".equalsIgnoreCase(env) || "production".equalsIgnoreCase(env)) {
            return RED + env + RESET;
        } else if ("test".equalsIgnoreCase(env)) {
            return BLUE + env + RESET;
        }
        return GREEN + env + RESET;
    }

    private String extractDbType(String url) {
        if (url == null || url.isEmpty()) {
            return "unknown";
        }
        if (url.contains("mysql")) {
            return "MySQL";
        } else if (url.contains("postgresql")) {
            return "PostgreSQL";
        } else if (url.contains("oracle")) {
            return "Oracle";
        } else if (url.contains("sqlserver")) {
            return "SQL Server";
        } else if (url.contains("h2")) {
            return "H2";
        } else if (url.contains("mongodb")) {
            return "MongoDB";
        }
        return "Unknown";
    }

    private String extractDbAddress(String url) {
        if (url == null || url.isEmpty() || "unknown".equals(url)) {
            return "unknown";
        }
        try {
            int start = url.indexOf("://");
            if (start == -1) {
                return url;
            }
            start += 3;
            int end = url.indexOf("?", start);
            if (end == -1) {
                end = url.length();
            }
            return url.substring(start, end);
        } catch (Exception e) {
            return url;
        }
    }

    private boolean checkDatabaseConnection() {
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(3);
        } catch (Exception e) {
            log.debug("数据库连接检测失败: {}", e.getMessage());
            return false;
        }
    }
}
