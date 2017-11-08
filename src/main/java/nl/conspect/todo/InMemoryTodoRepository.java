package nl.conspect.todo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
class InMemoryTodoRepository implements TodoRepository {

    private final Map<String, Todo> storage = new ConcurrentHashMap<>();

    @Override
    public Mono<Void> save(Todo todo) {
        return Mono.justOrEmpty(this.storage.put(todo.getId(), todo)).then();
    }

    @Override
    public Flux<Void> save(Iterable<Todo> todos) {
        return Flux.fromIterable(todos).flatMap(this::save);
    }

    @Override
    public Mono<Todo> findById(String id) {
        return Mono.justOrEmpty(this.storage.get(id));
    }

    @Override
    public Flux<Todo> findAll() {
        return Flux.fromIterable(storage.values());
    }
}
