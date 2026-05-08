package com.upc.trabajoparcial.DTOs;

import com.upc.trabajoparcial.Entidades.RecursoCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecursoDTO {

    private Long id;
    private String title;
    private String description;
    private String url;
    private RecursoCategory category;
    private Long uploadedById;
}