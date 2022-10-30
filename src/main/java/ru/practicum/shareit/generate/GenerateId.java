package ru.practicum.shareit.generate;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class GenerateId {
    private Integer id = 1;

    public Integer getId() {
        return id++;
    }
}
