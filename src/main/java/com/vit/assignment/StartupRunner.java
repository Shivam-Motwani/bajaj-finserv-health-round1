package com.vit.assignment;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;

import java.util.Map;

@Component
public class StartupRunner implements ApplicationRunner {

    private final WebClient wc = WebClient.create();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String genUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

        // Step 1: Generate webhook
        Map<String, Object> genResp = wc.post()
                .uri(genUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "name", "Shivam Motwani",
                        "regNo", "22bce2528",
                        "email", "theoryofnumbers123@gmail.com"
                ))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

        assert genResp != null;
        String webhook = (String) genResp.get("webhook");
        String accessToken = (String) genResp.get("accessToken");

        System.out.println("Webhook URL: " + webhook);
        System.out.println("AccessToken: " + accessToken);

        // Step 2: Final SQL query (solution to Question 2)
        String finalQuery =
            "SELECT " +
            " e1.EMP_ID, " +
            " e1.FIRST_NAME, " +
            " e1.LAST_NAME, " +
            " d.DEPARTMENT_NAME, " +
            " COUNT(e2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT " +
            "FROM EMPLOYEE e1 " +
            "JOIN DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID " +
            "LEFT JOIN EMPLOYEE e2 " +
            " ON e1.DEPARTMENT = e2.DEPARTMENT " +
            " AND e2.DOB > e1.DOB " +
            "GROUP BY e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME " +
            "ORDER BY e1.EMP_ID DESC;";

    // Step 3: Submit SQL query
    String submitUrl = webhook;

        String submitResp = wc.post()
                .uri(submitUrl)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("finalQuery", finalQuery))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println("Submission Response: " + submitResp);
    }
}
