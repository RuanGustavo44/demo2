package com.example.demo.controller;

import com.example.demo.model.Post;
import com.example.demo.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    // CREATE (Criar): POST /posts
    @PostMapping
    public ResponseEntity<Post> criarPost(@RequestBody Post novoPost) {
        Post postSalvo = postRepository.save(novoPost);
        return ResponseEntity.status(HttpStatus.CREATED).body(postSalvo);
    }

    // READ (Ler): GET /posts
    @GetMapping
    public List<Post> listarTodosOsPosts() {
        return postRepository.findAll();
    }

    // READ (Ler): GET /posts/1
    @GetMapping("/{id}")
    public ResponseEntity<Post> buscarPostPorId(@PathVariable Long id) {
        return postRepository.findById(id)
                .map(post -> ResponseEntity.ok(post))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // UPDATE (Atualizar Completo): PUT /posts/1
    @PutMapping("/{id}")
    public ResponseEntity<Post> atualizarPostCompleto(@PathVariable Long id, @RequestBody Post postAtualizado) {
        return postRepository.findById(id)
                .map(postExistente -> {
                    postExistente.setTitulo(postAtualizado.getTitulo());
                    postExistente.setConteudo(postAtualizado.getConteudo());
                    Post postSalvo = postRepository.save(postExistente);
                    return ResponseEntity.ok(postSalvo);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // UPDATE (Atualizar Parcial): PATCH /posts/1
    @PatchMapping("/{id}")
    public ResponseEntity<Post> atualizarPostParcial(@PathVariable Long id, @RequestBody Post postParcial) {
        return postRepository.findById(id)
                .map(postExistente -> {
                    // Atualiza apenas os campos que não vieram nulos na requisição
                    if (postParcial.getTitulo() != null) {
                        postExistente.setTitulo(postParcial.getTitulo());
                    }
                    if (postParcial.getConteudo() != null) {
                        postExistente.setConteudo(postParcial.getConteudo());
                    }
                    Post postSalvo = postRepository.save(postExistente);
                    return ResponseEntity.ok(postSalvo);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE (Deletar): DELETE /posts/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPost(@PathVariable Long id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
