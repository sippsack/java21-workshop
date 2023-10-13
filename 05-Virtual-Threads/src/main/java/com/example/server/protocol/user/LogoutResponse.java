
package com.example.server.protocol.user;

import java.io.Serializable;

import com.example.server.protocol.Response;

public record LogoutResponse(
    String server
) implements Serializable, Response {}
