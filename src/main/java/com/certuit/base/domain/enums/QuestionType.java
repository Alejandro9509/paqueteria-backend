package com.certuit.base.domain.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.stream.Stream;


@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum QuestionType {
    TEXT(1, "Texto"),
    MULTIPLE_CHOICE(2, "Opción Multiple");
    @Getter
    @Setter
    private int id;
    private final String name;

    QuestionType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static QuestionType of(int questionType) {
        return Stream.of(QuestionType.values())
                .filter(p -> p.getId() == questionType)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
