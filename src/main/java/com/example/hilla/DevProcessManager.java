package com.example.hilla;

import jakarta.annotation.PreDestroy;
import java.io.IOException;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
@Log4j2
public class DevProcessManager {
  private Process process;

  public void startExecutable() {
    try {
      ProcessBuilder processBuilder = new ProcessBuilder("src/main/resources/surreal", "start", "-u", "root", "-p", "root", "memory");
      process = processBuilder.start();
      log.info("SurrealDb started.");
    } catch (IOException e) {
      log.error("Unable to start executable", e);
    }
  }

  @PreDestroy
  public void stopExecutable() {
    if (process != null && process.isAlive()) {
      process.destroy();
      log.info("SurrealDb stopped.");
    }
  }
}
