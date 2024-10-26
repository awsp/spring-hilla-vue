package com.example.hilla;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/reactive")
@RequiredArgsConstructor
public class ReactiveController {

  private final ResourceLoader resourceLoader;

  @GetMapping(value = "/test", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public Mono<Resource> get() {
    return Mono.fromSupplier(() -> resourceLoader.getResource("classpath:./video.mp4"));
  }

  @GetMapping(value = "/foo")
  public Mono<String> test() {
    return Mono.just("foo");
  }
}
