package com.kafka.kafkasync;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Student {
    private String registrationNumber;
    private String name;
    private String grade;
}