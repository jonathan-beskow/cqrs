package br.com.boutique.api.controller;

import br.com.boutique.api.dto.BeautyProcedureDTO;
import br.com.boutique.api.services.BeautyProcedureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("beauty-procedures")
@RequiredArgsConstructor
public class BeautyProcedureController {

    private final BeautyProcedureService beautyProcedureService;

    @PostMapping
    public ResponseEntity<BeautyProcedureDTO> create(@RequestBody BeautyProcedureDTO beautyProcedureDTO) {
        return ResponseEntity.ok(beautyProcedureService.create(beautyProcedureDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        beautyProcedureService.deleteProcedure(id);
        return ResponseEntity.noContent().build();
    }
}
