package org.spongepowered.downloads.auth.api;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import com.lightbend.lagom.javadsl.api.transport.Method;
import com.lightbend.lagom.javadsl.server.ServerServiceCall;
import org.pac4j.core.profile.CommonProfile;
import org.taymyr.lagom.javadsl.openapi.OpenAPIService;
import org.taymyr.lagom.javadsl.openapi.OpenAPIUtils;

import java.util.function.Function;

public interface AuthService extends OpenAPIService {

    String AUTH_REQUEST_TOPIC = "auth_request";

    final class Providers {

        public static final String LDAP = "ldap";
        public static final String JWT = "jwt";
        public static final String WEBHOOK = "internal";
    }

    final class Roles {

        public static final String ADMIN = "soad_admin";
        public static final String WEBHOOK = "soad_webhook";
    }

    // The response will contain a JWT if the authentication succeeded.
    // Uses standard Basic auth over HTTPS to login.
    ServiceCall<NotUsed, AuthenticationRequest.Response> login();

    ServiceCall<NotUsed, NotUsed> logout();

    ServiceCall<NotUsed, CommonProfile> validate(final String type, final String role);

    default Descriptor descriptor() {
        return OpenAPIUtils.withOpenAPI(Service.named("auth")
                .withCalls(
                        Service.restCall(Method.POST, "/api/auth/login", this::login),
                        Service.restCall(Method.POST, "/api/auth/logout", this::logout)
                )
                .withAutoAcl(true)
        )
        // Intended to not be in the ACLs
        .withCalls(Service.restCall(Method.POST, "/api/auth/validate/:type/:role", this::validate).withAutoAcl(false));
    }
}
