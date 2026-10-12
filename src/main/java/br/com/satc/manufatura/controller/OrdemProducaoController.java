package br.com.satc.manufatura.controller;

import br.com.satc.manufatura.dto.FinalizarOrdemRequestDTO;
import br.com.satc.manufatura.dto.OrdemProducaoRequestDTO;
import br.com.satc.manufatura.dto.OrdemProducaoResponseDTO;
import br.com.satc.manufatura.service.OrdemProducaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Expõe os endpoints HTTP para abertura e finalização de ordens de produção.
 *
 * A lógica de negócio fica na camada de serviço, mantendo o controller responsável por receber as requisições 
 * e organizar as respostas da API.
 */
@RestController
@RequestMapping("/ordens-producao")
@RequiredArgsConstructor
public class OrdemProducaoController {

    private final OrdemProducaoService ordemService;

    @PostMapping
    public ResponseEntity<OrdemProducaoResponseDTO> abrir(@Valid @RequestBody OrdemProducaoRequestDTO dto) {
        OrdemProducaoResponseDTO criada = ordemService.abrir(dto);
        URI local = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(criada.id()).toUri();
        return ResponseEntity.created(local).body(criada);
    }

    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<OrdemProducaoResponseDTO> finalizar(@PathVariable Long id,
                                                              @Valid @RequestBody FinalizarOrdemRequestDTO dto) {
        return ResponseEntity.ok(ordemService.finalizar(id, dto));
    }
}
