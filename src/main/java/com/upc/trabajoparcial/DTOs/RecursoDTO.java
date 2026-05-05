package com.upc.trabajoparcial.DTOs;

import com.upc.trabajoparcial.Entidades.RecursoCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecursoDTO {

    private UUID id;
    private String title;
    private String description;
    private String url;
    private RecursoCategory category;
    private Long uploadedById;
}
