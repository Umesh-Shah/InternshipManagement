package ca.uwindsor.ims.service;

import ca.uwindsor.ims.dto.LoginResponse;

/** Internal result of a successful login: the raw JWT (for the cookie) plus the response body. */
public record AuthResult(String token, LoginResponse user) {}
