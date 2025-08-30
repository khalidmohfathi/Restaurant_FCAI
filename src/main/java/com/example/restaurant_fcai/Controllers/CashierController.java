package com.example.restaurant_fcai.Controllers;

import com.example.restaurant_fcai.Entities.Cashier;
import com.example.restaurant_fcai.Repos.CashierRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cashiers")
public class CashierController {

    private final CashierRepository repo;

    public CashierController(CashierRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Cashier> list() { return repo.findAll(); }

    @PostMapping
    public Cashier create(@RequestBody Cashier c) { return repo.save(c); }

    @GetMapping("/{id}")
    public ResponseEntity<Cashier> get(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cashier> update(@PathVariable Long id, @RequestBody Cashier input) {
        return repo.findById(id).map(c -> {
            if (input.getName() != null) c.setName(input.getName());
            if (input.getEmail() != null) c.setEmail(input.getEmail());
            if (input.getPassword() != null) c.setPassword(input.getPassword());
            return ResponseEntity.ok(repo.save(c));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
