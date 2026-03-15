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

    private static final int BOX_WIDTH = 64;

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
        banner.append(CYAN).append(repeat("_", BOX_WIDTH)).append(RESET).append("\n");
        banner.append(formatTitle("项目启动信息")).append("\n");
        banner.append(CYAN).append(repeat("_", BOX_WIDTH)).append(RESET).append("\n");
        banner.append(formatLine("项目名称", applicationName)).append("\n");
        banner.append(formatLine("版本号", version)).append("\n");
        banner.append(formatLine("环境配置", formatEnv(env))).append("\n");
        banner.append(formatLine("服务端口", GREEN + port + RESET)).append("\n");
        banner.append(formatLine("启动时间", startupTime)).append("\n");
        banner.append(CYAN).append(repeat("_", BOX_WIDTH)).append(RESET).append("\n");
        banner.append(formatLine("数据库类型", dbType)).append("\n");
        banner.append(formatLine("数据库地址", dbAddress)).append("\n");
        banner.append(formatDbStatusLine(dbConnected)).append("\n");
        banner.append(CYAN).append(repeat("_", BOX_WIDTH)).append(RESET).append("\n");

        log.info(banner.toString());
    }

    private String formatTitle(String title) {
        int titleWidth = getDisplayWidth(title);
        int totalPadding = BOX_WIDTH - titleWidth;
        int leftPadding = totalPadding / 2;
        int rightPadding = totalPadding - leftPadding;
        return BOLD + repeat(" ", leftPadding) + title + repeat(" ", rightPadding) + RESET;
    }

    private String formatLine(String label, String value) {
        String strippedValue = stripAnsiCodes(value);
        int labelWidth = getDisplayWidth(label);
        int valueWidth = getDisplayWidth(strippedValue);
        int contentWidth = 2 + labelWidth + 2 + valueWidth;
        int rightPadding = BOX_WIDTH - contentWidth;
        
        return "  " + label + ": " + value + repeat(" ", Math.max(0, rightPadding));
    }

    private String formatDbStatusLine(boolean connected) {
        String statusText = connected ? "已连接" : "未连接";
        String status = connected ? GREEN + statusText + RESET : RED + statusText + RESET;
        
        int labelWidth = getDisplayWidth("数据库状态");
        int valueWidth = getDisplayWidth(statusText);
        int contentWidth = 2 + labelWidth + 2 + valueWidth;
        int rightPadding = BOX_WIDTH - contentWidth;
        
        return "  " + "数据库状态" + ": " + status + repeat(" ", Math.max(0, rightPadding));
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

    private int getDisplayWidth(String str) {
        if (str == null) {
            return 0;
        }
        int width = 0;
        for (char c : str.toCharArray()) {
            if (Character.toString(c).matches("[\u4e00-\u9fa5\u3000-\u303f\uff00-\uffef]")) {
                width += 2;
            } else {
                width += 1;
            }
        }
        return width;
    }

    private String stripAnsiCodes(String str) {
        if (str == null) {
            return "";
        }
        return str.replaceAll("\u001B\\[[;\\d]*m", "");
    }

    private String repeat(String str, int count) {
        if (count <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
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
