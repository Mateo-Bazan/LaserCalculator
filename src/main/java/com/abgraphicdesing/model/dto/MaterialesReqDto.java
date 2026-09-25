package com.abgraphicdesing.model.dto;

public record MaterialesReqDto(
    String nombre,
    int precioCompra,
    int merma,
    int precioCosto,
    String dimensiones,
    int stock
){}
