package com.abgraphicdesing.scripts;

import com.abgraphicdesing.database.DbConfig;
import com.abgraphicdesing.database.InitializerDatabase;
import com.abgraphicdesing.model.dto.MaterialesReqDto;
import com.abgraphicdesing.repository.MaterialesRepository;
import com.abgraphicdesing.service.MaterialesService;

public class SeedMateriales {
  public static void main(String[] args) throws Exception {
    DbConfig db = new DbConfig();

    // 1. Nos aseguramos de que las tablas existan
    new InitializerDatabase(db).initializer();

    // 2. Armamos las capas igual que en la app real
    MaterialesService service =
        new MaterialesService(new MaterialesRepository(db));

    // 3. Evitamos duplicar datos si ya corriste el seeder
    if (!service.getAll().isEmpty()) {
      System.out.println("Ya hay materiales cargados, no hago nada.");
      return;
    }

    // 4. Datos de relleno: nombre, precioCompra, merma(%), precioCosto, dimensiones, stock
    MaterialesReqDto[] datos = {
      new MaterialesReqDto("Vinilo adhesivo blanco", 12000, 10, "1.22m x 50m", 8),
      new MaterialesReqDto("Lona frontlight",        25000, 15, "3.20m x 50m", 4),
      new MaterialesReqDto("Papel ilustración 150g",  5000,  5, "A3 x 100 hojas", 20),
      new MaterialesReqDto("Acrílico transparente",  18000, 20, "1.20m x 2.40m", 6),
      new MaterialesReqDto("Tinta eco-solvente",      9000,  15, "1 litro", 12)
    };

    // 5. Guardamos cada uno usando tu lógica real
    for (MaterialesReqDto dto : datos) {
      var creado = service.create(dto);
      System.out.println("Cargado: " + creado.nombre()
          + " | costo: " + creado.precioCosto());
    }

    System.out.println("Listo. Total en base: " + service.getAll().size());
  }
}