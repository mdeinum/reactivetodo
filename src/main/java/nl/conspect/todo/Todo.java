package nl.conspect.todo;

import java.util.UUID;

public class Todo {

    private String id = UUID.randomUUID().toString();
    private boolean completed = false;
    private String todo;

    Todo() {}

    public Todo(String todo) {
        this.todo = todo;
    }

    public String getId() {
        return id;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String getTodo() {
        return todo;
    }

    public void complete() {
        this.completed=true;
    }

    @Override
    public String toString() {
        return String.format("Todo (id=%s, completed=%s, todo=%s)", this.id, this.completed, this.todo);
    }
}
