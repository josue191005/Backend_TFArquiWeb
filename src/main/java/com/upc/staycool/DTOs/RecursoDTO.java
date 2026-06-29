package com.upc.staycool.DTOs;

import com.upc.staycool.Entidades.RecursoCategory;
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