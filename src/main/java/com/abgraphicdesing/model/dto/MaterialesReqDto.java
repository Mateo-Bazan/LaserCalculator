package com.abgraphicdesing.model.dto;

public record MaterialesReqDto(
  String nombre,
  int precioCompra,
  int merma,
  String dimensiones,
  int stock
){ }
