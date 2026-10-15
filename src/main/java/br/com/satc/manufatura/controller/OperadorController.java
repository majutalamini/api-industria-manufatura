package br.com.satc.manufatura.controller;

import br.com.satc.manufatura.dto.OperadorRequestDTO;
import br.com.satc.manufatura.dto.OperadorResponseDTO;
import br.com.satc.manufatura.service.OperadorService;
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

/**
 * Expõe os endpoints HTTP para cadastro e consulta de operadores.
 *
 * A lógica de negócio fica na camada de serviço, mantendo este controller responsável pela comunicação HTTP 
 * e pela organização das respostas da API.
 */
@RestController
@RequestMapping("/operadores")
@RequiredArgsConstructor
public class OperadorController {

    private final OperadorService operadorService;

    @PostMapping
    public ResponseEntity<OperadorResponseDTO> criar(@Valid @RequestBody OperadorRequestDTO dto) {
        OperadorResponseDTO criado = operadorService.criar(dto);
        URI local = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(local).body(criado);
    }

    @GetMapping
    public ResponseEntity<List<OperadorResponseDTO>> listar() {
        return ResponseEntity.ok(operadorService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OperadorResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(operadorService.buscarPorId(id));
    }
}
