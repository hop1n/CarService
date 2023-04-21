package org.example.http.servlet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

abstract class JsonServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    @Override
    protected final void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        respond(resp, get(req.getRequestURI(), req.getParameterMap()));
    }

    @Override
    protected final void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        respond(
                resp,
                post(
                        req.getRequestURI(),
                        objectMapper.readValue(req.getReader(), new TypeReference<>() {
                        })
                )
        );
    }

    Response get(String uri, Map<String, String[]> parameters) {
        return new Response(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    Response post(String uri, Map<String, String> body) {
        return new Response(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }


    private void respond(HttpServletResponse resp, Response response) throws IOException {
        resp.setStatus(response.status());
        resp.addHeader("Content-Type", "application/json");
        var body = response.body();
        if (body.isEmpty()) {
            return;
        }
        objectMapper.writeValue(resp.getWriter(), body.get());
    }

    record Response(int status, Optional<Object> body) {

        Response(int status) {
            this(status, Optional.empty());
        }

        Response(Object body) {
            this(HttpServletResponse.SC_OK, Optional.ofNullable(body));
        }
    }
}
