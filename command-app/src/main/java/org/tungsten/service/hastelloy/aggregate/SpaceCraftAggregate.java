package org.tungsten.service.hastelloy.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.common.IdentifierFactory;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;
import org.tungsten.service.hastelloy.command.CreateSpaceCraftCommand;
import org.tungsten.service.hastelloy.event.SpaceCraftCreatedEvent;
import org.tungsten.service.hastelloy.model.SpaceCraft;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

@Aggregate
public class SpaceCraftAggregate {
    @AggregateIdentifier
    private String id;

    private String name;

    public SpaceCraftAggregate() {
    }

    @CommandHandler
    public SpaceCraftAggregate(final CreateSpaceCraftCommand command){
        apply(new SpaceCraftCreatedEvent(IdentifierFactory.getInstance().generateIdentifier(), command.getName()));
    }

    @EventSourcingHandler
    public void on(final SpaceCraftCreatedEvent spaceCraftCreatedEvent) {
        this.id = spaceCraftCreatedEvent.getId();
        this.name = spaceCraftCreatedEvent.getName();
    }
}
