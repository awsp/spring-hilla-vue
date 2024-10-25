package com.example.hilla;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import com.vaadin.hilla.EndpointSubscription;
import com.vaadin.hilla.Nonnull;
import java.time.Duration;
import java.util.Date;
import reactor.core.publisher.Flux;

@BrowserCallable
@AnonymousAllowed
public class ReactiveEndpoint {

  public Flux<@Nonnull String> getClock() {
    return Flux.interval(Duration.ofSeconds(1L))
      .onBackpressureDrop()
      .map(interval -> new Date().toString());
  }

  public EndpointSubscription<@Nonnull String> getClockCancellable() {
    return EndpointSubscription.of(getClock(), () -> {

    });
  }

}
