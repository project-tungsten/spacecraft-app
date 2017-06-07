package org.tungsten.service.hastelloy.query.handler;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import org.tungsten.service.hastelloy.query.event.SpaceCraftCreatedEvent;

@Component
public class SpaceCraftCreatedEventHandler{
    @EventHandler
    public void on(SpaceCraftCreatedEvent spaceCraftCreatedEvent){
        System.out.println("spaceCraftCreatedEvent.getName() = " + spaceCraftCreatedEvent.getName());
    }
}
