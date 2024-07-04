package me.sonam.security;

import lombok.extern.java.Log;
import me.sonam.security.property.PermitPath;
import me.sonam.security.util.TokenRequestFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@AutoConfigureWebTestClient
@Log
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class TokenRequestFilterYamlTest {
    private static final Logger LOG = LoggerFactory.getLogger(TokenRequestFilterYamlTest.class);

    @Autowired
    private PermitPath permitPath;

    @Autowired
    private TokenRequestFilter tokenRequestFilter;

    @Test
    public void jwtPath() {
        LOG.info("jwt.path: {}", tokenRequestFilter.getRequestFilters().size());
        assertThat(tokenRequestFilter.getRequestFilters().size()).isEqualTo(4);

        LOG.info("jwtPath[0].toString: {}", tokenRequestFilter.getRequestFilters().get(0).toString());
        assertThat(tokenRequestFilter.getRequestFilters().get(0).getIn()).isEqualTo("/api/health/passheader");
        assertThat(tokenRequestFilter.getRequestFilters().get(0).getOut()).isEqualTo("/api/health/jwtreceiver");
        assertThat(tokenRequestFilter.getRequestFilters().get(0).getAccessToken().getOption().name()).isEqualTo("request");
        assertThat(tokenRequestFilter.getRequestFilters().get(0).getAccessToken().getOption().name()).isNotEqualTo("doNothing");
        assertThat(tokenRequestFilter.getRequestFilters().get(0).getAccessToken().getScopes()).isEqualTo("message.read message.write");
        assertThat(tokenRequestFilter.getRequestFilters().get(0).getAccessToken().getScopes()).isNotEqualTo("message.read");
        assertThat(tokenRequestFilter.getRequestFilters().get(0).getAccessToken().getBase64EncodedClientIdSecret()).isEqualTo("b2F1dGgtY2xpZW50Om9hdXRoLXNlY3JldA==");
        assertThat(tokenRequestFilter.getRequestFilters().get(0).getAccessToken().getBase64EncodedClientIdSecret()).isNotEqualTo("randomstring");


        assertThat(tokenRequestFilter.getRequestFilters().get(1).getIn()).isEqualTo("/api/health/passheader");
        assertThat(tokenRequestFilter.getRequestFilters().get(1).getOut()).isEqualTo("/api/health/liveness");
        assertThat(tokenRequestFilter.getRequestFilters().get(1).getAccessToken().getOption().name()).isEqualTo("forward");
        assertThat(tokenRequestFilter.getRequestFilters().get(1).getAccessToken().getOption().name()).isNotEqualTo("request");
        assertThat(tokenRequestFilter.getRequestFilters().get(1).getAccessToken().getScopes()).isNull();
        assertThat(tokenRequestFilter.getRequestFilters().get(1).getAccessToken().getBase64EncodedClientIdSecret()).isNull();

        assertThat(tokenRequestFilter.getRequestFilters().get(2).getIn()).isEqualTo("/api/health/forwardtoken");
        assertThat(tokenRequestFilter.getRequestFilters().get(2).getOut()).isEqualTo("/api/health/jwtreceiver");
        assertThat(tokenRequestFilter.getRequestFilters().get(2).getAccessToken().getOption().name()).isEqualTo("forward");
        assertThat(tokenRequestFilter.getRequestFilters().get(2).getAccessToken().getScopes()).isNull();
        assertThat(tokenRequestFilter.getRequestFilters().get(2).getAccessToken().getBase64EncodedClientIdSecret()).isNull();

        assertThat(tokenRequestFilter.getRequestFilters().get(3).getIn()).isEqualTo("/api/scope/callread");
        assertThat(tokenRequestFilter.getRequestFilters().get(3).getOut()).isEqualTo("/api/scope/read");
        assertThat(tokenRequestFilter.getRequestFilters().get(3).getAccessToken().getOption().name()).isEqualTo("request");
        assertThat(tokenRequestFilter.getRequestFilters().get(3).getAccessToken().getOption().name()).isNotEqualTo("doNothing");
        assertThat(tokenRequestFilter.getRequestFilters().get(3).getAccessToken().getScopes()).isEqualTo("message.read");
        assertThat(tokenRequestFilter.getRequestFilters().get(3).getAccessToken().getScopes()).isNotEqualTo("message.read message.write");
        assertThat(tokenRequestFilter.getRequestFilters().get(3).getAccessToken().getBase64EncodedClientIdSecret()).isEqualTo("b2F1dGgtY2xpZW50Om9hdXRoLXNlY3JldA==");
        assertThat(tokenRequestFilter.getRequestFilters().get(3).getAccessToken().getBase64EncodedClientIdSecret()).isNotEqualTo("randomstring");

    }
    @Test
    public void yamlTest() {
        LOG.info("permitPath: {}", permitPath);
        permitPath.getPermitpath().forEach(path -> {
            LOG.info("path: {}, method: {}", path.getPath(), path.getHttpMethods());
        });
    }
}
