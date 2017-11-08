package nl.conspect.todo;

import java.util.Random;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/todos")
public class TodoController {

    private final Random rnd = new Random();
    private final TodoRepository todos;

    public TodoController(TodoRepository todos) {
        this.todos = todos;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("todos", todos.findAll());
        return "index";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Mono<Todo> get(@PathVariable String id) {
        return this.todos.findById(id);
    }

    @PostMapping
    public Mono<String> create(ServerWebExchange exchange) {
        return exchange.getFormData().map(form -> new Todo(form.getFirst("todo"))).map(todos::save).then(Mono.just("redirect:/todos"));
    }


    @PostMapping("/{id}/completed")
    public Mono<String> markComplete(@PathVariable String id) {

        return todos.findById(id).doOnNext(Todo::complete).flatMap(todos::save).then(Mono.just("redirect:/todos"));
    }

    @GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    @ResponseBody
    public Flux<Todo> list() {
        return todos.findAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Mono<Void> create(@RequestBody Todo todo) {
        return todos.save(todo);
    }

}
