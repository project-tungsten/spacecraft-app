package org.tungsten.service.hastelloy.controller;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.tungsten.service.hastelloy.command.CreateSpaceCraftCommand;
import org.tungsten.service.hastelloy.model.SpaceCraft;

@Controller
public class SpaceCraftController {

    private final CommandGateway commandGateway;

    public SpaceCraftController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody SpaceCraft spaceCraft) {
        commandGateway.sendAndWait(new CreateSpaceCraftCommand(spaceCraft.getName()));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
