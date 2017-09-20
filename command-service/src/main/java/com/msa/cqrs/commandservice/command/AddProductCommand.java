package com.msa.cqrs.commandservice.command;

public class AddProductCommand {
    private String id;
    private String name;
    public AddProductCommand(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "AddProductCommand{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
