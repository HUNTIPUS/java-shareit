package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
class ItemRequestDtoJsonTest {
    @Autowired
    private JacksonTester<ItemRequestDtoOutput> jsonOutput;
    @Autowired
    private JacksonTester<ItemRequestDtoInput> jsonInput;

    @Test
    void testItemDto() throws Exception {
        ItemRequestDtoOutput itemRequestDtoOutput = ItemRequestDtoOutput.builder()
                .id(1L)
                .description("Нужен мяч для тенниса")
                .items(List.of())
                .created(LocalDateTime.of(2022, 12, 8, 8, 0, 1))
                .build();

        JsonContent<ItemRequestDtoOutput> result = jsonOutput.write(itemRequestDtoOutput);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("Нужен мяч для тенниса");
        assertThat(result).extractingJsonPathArrayValue("$.items").isEqualTo(List.of());
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo(itemRequestDtoOutput.getCreated().toString());

    }

    @Test
    void testItemDtoFromJson() throws Exception {
        ItemRequestDtoOutput itemRequestDtoOutput = ItemRequestDtoOutput.builder()
                .id(1L)
                .description("Нужен мяч для тенниса")
                .items(List.of())
                .created(LocalDateTime.of(2022, 12, 8, 8, 0, 1))
                .build();

        JsonContent<ItemRequestDtoOutput> result = jsonOutput.write(itemRequestDtoOutput);
        ObjectContent<ItemRequestDtoInput> requestDtoObjectContent = jsonInput.parse(result.getJson());

        assertEquals(itemRequestDtoOutput.getId(), requestDtoObjectContent.getObject().getId());
        assertEquals(itemRequestDtoOutput.getDescription(), requestDtoObjectContent.getObject().getDescription());
        assertEquals(itemRequestDtoOutput.getCreated(), requestDtoObjectContent.getObject().getCreated());

    }
}