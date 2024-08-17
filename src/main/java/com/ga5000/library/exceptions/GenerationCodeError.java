package com.ga5000.library.exceptions;

import com.auth0.jwt.exceptions.JWTCreationException;

public class GenerationCodeError extends RuntimeException {
  public GenerationCodeError(String message, JWTCreationException cause) {
    super(message, cause);
  }
}
