package nl.conspect.todo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TodoRepository {

    Mono<Void> save(Todo todo);
    Flux<Void> save(Iterable<Todo> todos);
    Mono<Todo> findById(String id);
    Flux<Todo> findAll();

}
