package com.example.hilla;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@SpringBootApplication
public class HillaApplication {

  public static void main(String[] args) {
    SpringApplication.run(HillaApplication.class, args);
  }

  @Bean
  @Profile("prod")
  public CommandLineRunner runExecutable(ProcessManager processManager) {
    return args -> processManager.startExecutable();
  }

  @Bean
  @Profile("dev")
  public CommandLineRunner runExecutableDev(DevProcessManager processManager) {
    return args -> processManager.startExecutable();
  }

  @Bean
  ApplicationListener<ApplicationReadyEvent> ready() {
    return event -> {
      ObjectMapper objectMapper = new ObjectMapper();
      RestClient restClient = RestClient.create();
      String url = "http://localhost:8000/sql";

      JsonNode body = restClient.post()
        .uri(url)
        .header("surreal-ns", "test")
        .header("surreal-db", "test")
        .accept(MediaType.APPLICATION_JSON)
        .headers(header -> header.setBasicAuth("root", "root"))
        .body("select * from person")
        .retrieve()
        .body(JsonNode.class);

      try {
        if (body != null) {
          List<Person> personList = objectMapper.treeToValue(body.get(0).get("result"), new TypeReference<>() {
          });
          System.out.println("done");
        }
      } catch (JsonProcessingException e) {
        // lll
        System.out.println("error");
      }
      System.out.println("body");
    };
  }
}

