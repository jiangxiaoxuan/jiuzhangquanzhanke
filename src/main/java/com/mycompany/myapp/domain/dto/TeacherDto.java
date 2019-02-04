package com.mycompany.myapp.domain.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDto {

    private String firstName;

    private String lastName;
}
