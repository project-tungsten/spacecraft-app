package org.tungsten.service.hastelloy.command;

import java.util.UUID;

public class CreateSpaceCraftCommand {
    private String name;

    public CreateSpaceCraftCommand(String name) {
        this.name = name;
    }

    public CreateSpaceCraftCommand() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
