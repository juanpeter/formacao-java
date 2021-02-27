package br.com.indracompany.gestaoalunos.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.indracompany.gestaoalunos.model.Aluno;
import br.com.indracompany.gestaoalunos.repository.Alunos;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "/api/alunos")
public class AlunoResource {

    @Autowired
    private Alunos alunosRepository;

    @ApiOperation("Cadastra alunos, um por vez.")
    @PostMapping
    public ResponseEntity<Aluno> save(@Valid @RequestBody Aluno aluno) {
        alunosRepository.save(aluno);
        return new ResponseEntity<>(aluno, HttpStatus.OK);
    }

    @ApiOperation("Consulta todos os alunos, retornando uma lista.")
    @GetMapping
    public ResponseEntity<List<Aluno>> getAll() {
        List<Aluno> alunos = alunosRepository.findAll();
        return new ResponseEntity<>(alunos, HttpStatus.OK);
    }

    @ApiOperation("Consulta um aluno pelo id.")
    @GetMapping(path = "/{id}")
    public ResponseEntity<Optional<Aluno>> getById(@PathVariable Long id) {
        Optional<Aluno> aluno;
        try {
            aluno = alunosRepository.findById(id);
            return new ResponseEntity<>(aluno, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @ApiOperation("Exclui um aluno pelo id.")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Optional<Aluno>> deleteById(@PathVariable Long id) {
        try {
            alunosRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @ApiOperation("Atualiza um aluno pelo id.")
    @PutMapping(path = "/{id}")
    public ResponseEntity<Aluno> update(@PathVariable Long id, @RequestBody Aluno alunoAtualizado) {
        return alunosRepository.findById(id)
            .map(aluno -> {
            aluno.setNome(alunoAtualizado.getNome());
            aluno.setEmail(alunoAtualizado.getEmail());
            aluno.setNota(alunoAtualizado.getNota());
            Aluno alunoAtual = alunosRepository.save(aluno);
            return ResponseEntity.ok().body(alunoAtual);
        }).orElse(ResponseEntity.notFound().build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
        MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}
