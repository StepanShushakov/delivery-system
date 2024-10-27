package ru.skillbox.gateway.security;

import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class RouterValidator {

    public static final List<Pattern> openEndpoints = List.of(
            Pattern.compile("/auth/user/signup"),
            Pattern.compile("/auth/token/generate"),
            Pattern.compile("/auth/swagger-ui.*"),
            Pattern.compile("/auth/v3/api-docs.*"),
            Pattern.compile("/api/swagger-ui.*"),
            Pattern.compile("/api/v3/api-docs.*"),
            Pattern.compile("/api/price"),
            Pattern.compile("/payment/swagger-ui.*"),
            Pattern.compile("/payment/v3/api-docs.*"),
            Pattern.compile("/payment/add.*"),
            Pattern.compile("/payment/deposit"),
            Pattern.compile("/inventory/swagger-ui.*"),
            Pattern.compile("/inventory/v3/api-docs.*"),
            Pattern.compile("/inventory/add"),
            Pattern.compile("/delivery/swagger-ui.*"),
            Pattern.compile("/delivery/v3/api-docs.*")
    );

    public static final Predicate<ServerHttpRequest> isSecured =
            request -> openEndpoints
                    .stream()
                    .noneMatch(pattern -> pattern.matcher(request.getURI().getPath()).matches());

}
