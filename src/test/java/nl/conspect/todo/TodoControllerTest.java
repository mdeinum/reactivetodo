package nl.conspect.todo;

import java.net.URLEncoder;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;

import reactor.test.StepVerifier;

class TodoControllerTest {

    @Test
    void getSingleElement() {

        InMemoryTodoRepository repository = new InMemoryTodoRepository();
        TodoController controller = new TodoController(repository);

        Todo todo = new Todo("Write succeeding test.");
        repository.save(todo);

        StepVerifier.create(controller.get(todo.getId()))
                .expectNext(todo)
                .expectComplete().verify();
    }

    @Test
    void postNewTodo() throws Exception {

        InMemoryTodoRepository repository = new InMemoryTodoRepository();
        TodoController controller = new TodoController(repository);

        String todo = URLEncoder.encode("Write Succeeding POST test.", "UTF-8");

        MockServerHttpRequest request = MockServerHttpRequest.post("/todos")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body("todo=" + todo);

        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        StepVerifier.create(controller.create(exchange))
                .expectNext("redirect:/todos")
                .expectComplete().verify();

        StepVerifier.create(repository.findAll().count())
                .expectNext(1L).verifyComplete();
    }


}
