package org.tungsten.service.hastelloy;

import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
public class AppConfiguration{

    @Bean
    public EventStore eventStore(EventStorageEngine storageEngine){
        return new EmbeddedEventStore(storageEngine);
    }

    @Bean
    public EventStorageEngine eventStorageEngine(){
//        return new MongoEventStorageEngine();
        return new InMemoryEventStorageEngine();
    }
}

