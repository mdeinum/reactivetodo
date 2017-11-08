package nl.conspect.todo;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Use the {@code WebClient} to get the (streaming) results from the
 * server.
 */
public class TodoClient {

    public static void main(String[] args) throws IOException {
        final WebClient client = WebClient.create("http://localhost:8080");

        client.get()
                    .uri("/todos")
                    .accept(MediaType.APPLICATION_STREAM_JSON)
                .retrieve()
                    .bodyToFlux(Todo.class)
                    .subscribe(System.out::println);

        System.in.read();

    }

}
