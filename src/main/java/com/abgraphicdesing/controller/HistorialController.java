package com.abgraphicdesing.controller;

import java.util.List;
import java.util.function.Consumer;

import com.abgraphicdesing.controller.util.AsyncExecutor;
import com.abgraphicdesing.model.entity.Historial;
import com.abgraphicdesing.service.HistorialService;

public class HistorialController {
  private final HistorialService service;

  public HistorialController(HistorialService service) {
    this.service = service;
  }

  public void save(Historial h, Consumer<Boolean> onSuccess, Consumer<Throwable> onError) {
    AsyncExecutor.execute(() -> service.save(h), onSuccess, onError);
  }

  public void getAll(Consumer<List<Historial>> onSuccess, Consumer<Throwable> onError) {
    AsyncExecutor.execute(() -> service.getAll(), onSuccess, onError);
  }
}
