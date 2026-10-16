package com.abgraphicdesing.scripts;

import com.abgraphicdesing.database.DbConfig;
import com.abgraphicdesing.database.InitializerDatabase;
import com.abgraphicdesing.model.dto.ImpresorasReqDto;
import com.abgraphicdesing.repository.ImpresorasRepository;
import com.abgraphicdesing.service.ImpresorasService;

public class SeedImpresoras {
  public static void main(String[] args) throws Exception{
    DbConfig db = new DbConfig();

    new InitializerDatabase(db).initializer();

    ImpresorasService service = new ImpresorasService(new ImpresorasRepository(db));

    if(!service.getAll().isEmpty()){
      System.out.println("Ya hay Impresoras cargadas, no hago nada");
      return;
    }

    ImpresorasReqDto[] datos = {
      new ImpresorasReqDto(1, "TodoLaser", "aaa", "TodoLaser MK2", 10000, 250, 1000000, 1000000),
      new ImpresorasReqDto(2, "Laser", "MK67", "Laser MK67", 10000, 250, 1000000, 1000000)
    };

    for(ImpresorasReqDto dto : datos){
      var creado = service.create(dto);
      System.out.println("Cargado: " + creado);
    }
    System.out.println("Listo. Total en base: " + service.getAll().size());
  }
}