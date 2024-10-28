package com.example.hilla.auth;

import com.example.hilla.auth.domain.request.UserLogin;
import com.webauthn4j.data.attestation.AttestationObject;
import com.webauthn4j.data.attestation.authenticator.AuthenticatorData;
import com.webauthn4j.data.extension.authenticator.RegistrationExtensionAuthenticatorOutput;
import com.webauthn4j.springframework.security.WebAuthnRegistrationRequestValidationResponse;
import com.webauthn4j.springframework.security.WebAuthnRegistrationRequestValidator;
import com.webauthn4j.springframework.security.credential.WebAuthnCredentialRecordImpl;
import com.webauthn4j.springframework.security.credential.WebAuthnCredentialRecordManager;
import com.webauthn4j.springframework.security.exception.WebAuthnAuthenticationException;
import com.webauthn4j.util.Base64UrlUtil;
import com.webauthn4j.util.Base64Util;
import com.webauthn4j.util.UUIDUtil;
import com.webauthn4j.util.exception.WebAuthnException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Log4j2
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private static final String VIEW_HOME = "home";
  private static final String VIEW_LOGIN = "auth/login";

  private final WebAuthnCredentialRecordManager authnCredentialRecordManager;
  private final WebAuthnRegistrationRequestValidator registrationRequestValidator;

  @GetMapping("/login")
  public String loginPage(Model model) {
    UserLogin userLogin = new UserLogin();
    String userHandle = Base64Util.encodeToString(UUIDUtil.convertUUIDToBytes(UUID.randomUUID()));
    userLogin.setUserHandle(userHandle);
    model.addAttribute("userForm", userLogin);

    return VIEW_LOGIN;
  }

  @PostMapping(value = "/signup")
  public String create(HttpServletRequest request, @Valid @ModelAttribute("userForm") UserLogin userCreateForm, BindingResult result,
    Model model, RedirectAttributes redirectAttributes) {

    try {
      if (result.hasErrors()) {
        model.addAttribute("errorMessage", "Your input needs correction.");
        log.error("User input validation failed.");

        return VIEW_LOGIN;
      }

      WebAuthnRegistrationRequestValidationResponse registrationRequestValidationResponse;
      try {
        registrationRequestValidationResponse = registrationRequestValidator.validate(
          request,
          userCreateForm.getClientDataJSON(),
          userCreateForm.getAttestationObject(),
          userCreateForm.getTransports(),
          userCreateForm.getClientExtensions()
        );
      } catch (WebAuthnException | WebAuthnAuthenticationException e) {
        model.addAttribute("errorMessage", "Authenticator registration request validation failed. Please try again.");
        log.error("WebAuthn registration request validation failed.", e);
        return VIEW_LOGIN;
      }

      WebAuthnCredentialRecordImpl authenticator = getWebAuthnCredentialRecord(userCreateForm, registrationRequestValidationResponse);

      try {
        authnCredentialRecordManager.createCredentialRecord(authenticator);
      } catch (IllegalArgumentException ex) {
        model.addAttribute("errorMessage", "Registration failed. The user may already be registered.");
        log.error("Registration failed.", ex);
        return VIEW_LOGIN;
      }
    } catch (RuntimeException ex) {
      model.addAttribute("errorMessage", "Registration failed by unexpected error.");
      log.error("Registration failed.", ex);
      return VIEW_LOGIN;
    }

    model.addAttribute("successMessage", "User registration successful. Please login.");
    return VIEW_LOGIN;
  }

  @PostMapping("/logout")
  public void logout() {
    // TODO: implement logout
  }

  private static WebAuthnCredentialRecordImpl getWebAuthnCredentialRecord(UserLogin userCreateForm,
    WebAuthnRegistrationRequestValidationResponse registrationRequestValidationResponse) {

    String username = userCreateForm.getUsername();
    AttestationObject attestationObject = registrationRequestValidationResponse.getAttestationObject();
    AuthenticatorData<RegistrationExtensionAuthenticatorOutput> authenticatorData = attestationObject.getAuthenticatorData();

    return new WebAuthnCredentialRecordImpl(
      "authenticator",
      username,
      authenticatorData.getAttestedCredentialData(),
      attestationObject.getAttestationStatement(),
      authenticatorData.getSignCount(),
      registrationRequestValidationResponse.getTransports(),
      registrationRequestValidationResponse.getRegistrationExtensionsClientOutputs(),
      authenticatorData.getExtensions()
    );
  }
}
