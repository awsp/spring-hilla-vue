package com.example.hilla;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
@BrowserCallable
@AnonymousAllowed
public class ContentService {

  private final List<String> inMemoryDb = new ArrayList<>();

  @PostConstruct
  void init() {
    inMemoryDb.add("foo");
    inMemoryDb.add("bar");
  }

  public List<String> findAll() {
    return inMemoryDb;
  }

  public String add(String value) {
    inMemoryDb.add(value);
    return value;
  }
}
