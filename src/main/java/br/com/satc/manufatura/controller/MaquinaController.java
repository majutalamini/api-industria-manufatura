package br.com.satc.manufatura.controller;

import br.com.satc.manufatura.dto.MaquinaRequestDTO;
import br.com.satc.manufatura.dto.MaquinaResponseDTO;
import br.com.satc.manufatura.service.MaquinaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/maquinas")
@RequiredArgsConstructor
public class MaquinaController {

    private final MaquinaService maquinaService;

    @PostMapping
    public ResponseEntity<MaquinaResponseDTO> criar(@Valid @RequestBody MaquinaRequestDTO dto) {
        MaquinaResponseDTO criada = maquinaService.criar(dto);
        URI local = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(criada.id()).toUri();
        return ResponseEntity.created(local).body(criada);
    }

    @GetMapping
    public ResponseEntity<List<MaquinaResponseDTO>> listar() {
        return ResponseEntity.ok(maquinaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaquinaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(maquinaService.buscarPorId(id));
    }
}
