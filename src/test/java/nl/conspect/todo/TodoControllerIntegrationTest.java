package nl.conspect.todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TodoApplication.TodoConfiguration.class)
class TodoControllerIntegrationTest {

    @Autowired
    private ApplicationContext ctx;

    private WebTestClient testClient;

    @BeforeEach
    public void setup() {
        testClient = WebTestClient.bindToApplicationContext(ctx).configureClient().baseUrl("/todos").build();
    }

    @Test
    void index() {

        testClient.get().exchange()
                .expectStatus().isOk()
                .expectBody();
    }

}