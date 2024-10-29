package com.example.hilla.auth.domain.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLogin {

  @NotNull
  private String userHandle;

  @NotNull
  private String username;

  @NotNull
  @Valid
  private String clientDataJSON;

  @NotNull
  @Valid
  private String attestationObject;

  private Set<String> transports;

  @NotNull
  private String clientExtensions;
}
