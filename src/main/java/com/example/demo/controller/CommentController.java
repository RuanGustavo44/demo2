package com.example.demo.controller;

import com.example.demo.model.Comment;
import com.example.demo.model.Post;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    // CREATE: POST /posts/1/comments
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Comment> criarComentario(@PathVariable Long postId, @RequestBody Comment novoComentario) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        novoComentario.setPost(postOptional.get());
        Comment comentarioSalvo = commentRepository.save(novoComentario);
        return ResponseEntity.status(HttpStatus.CREATED).body(comentarioSalvo);
    }

    // READ: GET /posts/1/comments
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<Comment>> listarComentariosDoPost(@PathVariable Long postId) {
        if (!postRepository.existsById(postId)) {
            return ResponseEntity.notFound().build();
        }
        List<Comment> comments = commentRepository.findByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    // READ: GET /comments?postId=1
    @GetMapping("/comments")
    public ResponseEntity<List<Comment>> listarComentariosPorFiltro(@RequestParam Long postId) {
        if (!postRepository.existsById(postId)) {
            return ResponseEntity.notFound().build();
        }
        List<Comment> comments = commentRepository.findByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    // UPDATE: PUT /comments/1 (Rota não aninhada para simplicidade)
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<Comment> atualizarComentario(@PathVariable Long commentId, @RequestBody Comment comentarioAtualizado) {
        return commentRepository.findById(commentId)
                .map(comentarioExistente -> {
                    comentarioExistente.setTexto(comentarioAtualizado.getTexto());
                    Comment comentarioSalvo = commentRepository.save(comentarioExistente);
                    return ResponseEntity.ok(comentarioSalvo);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE: DELETE /comments/1 (Rota não aninhada para simplicidade)
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deletarComentario(@PathVariable Long commentId) {
        if (commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
