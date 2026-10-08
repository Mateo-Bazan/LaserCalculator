package com.abgraphicdesing.scripts;

import com.abgraphicdesing.database.DbConfig;
import com.abgraphicdesing.database.InitializerDatabase;
import com.abgraphicdesing.model.dto.AjustesReqDto;
import com.abgraphicdesing.repository.AjustesRepository;
import com.abgraphicdesing.service.AjustesService;

public class SeedAjustes {
  public static void main(String[] args) throws Exception{
    DbConfig db = new DbConfig();

    new InitializerDatabase(db).initializer();

    AjustesService service = new AjustesService(new AjustesRepository(db));

    if(!service.getAll().isEmpty()){
      System.out.println("Ya hay Ajustes cargados, no hago nada");
      return;
    }

    AjustesReqDto[] datos = {
      new AjustesReqDto("Alquiler", 900000, 3),
      new AjustesReqDto("Software", 20000, 4),
      new AjustesReqDto("Gastos Fijos", 1000, 2),
      new AjustesReqDto("Cinta", 1000, 1)
    };

    for(AjustesReqDto dto : datos){
      var creado = service.create(dto);
      System.out.println("Cargado: " + creado);
    }
    System.out.println("Listo. Total en base: " + service.getAll().size());
  }
}
