
package com.example.server.protocol.user;

import java.io.Serializable;

import com.example.server.protocol.Request;
import com.example.server.protocol.SessionInfo;

public record LogoutRequest(
    SessionInfo session
) implements Serializable, Request {}
