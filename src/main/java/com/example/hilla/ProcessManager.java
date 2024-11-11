package com.example.hilla;

import jakarta.annotation.PreDestroy;
import java.io.IOException;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("prod")
@Component
@Log4j2
public class ProcessManager {

  private Process process;

  public void startExecutable() {
    try {
      // TODO: 1. make all params into config properties
      // TODO: 2. Be able to set my database location and name, empty for same as application.
      // TODO. 3. First time only, create root user, access countrols, pre-defined schemas and functions and blah blah blah
      // TODO: 4. check for database so not root credential is created, although SurrealDb is smart enough to do so
      ProcessBuilder processBuilder = new ProcessBuilder("src/main/resources/surreal", "start", "-u", "root", "-p", "root", "surrealkv:mydatabase.db");
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
