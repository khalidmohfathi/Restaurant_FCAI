package com.example.restaurant_fcai.Controllers;

import com.example.restaurant_fcai.Entities.CustomerTable;
import com.example.restaurant_fcai.Repos.TableRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/tables")
public class TablesController {

    private final TableRepository repo;

    public TablesController(TableRepository repo) { this.repo = repo; }

    @GetMapping
    public List<CustomerTable> list() { return repo.findAll(); }

    @PostMapping
    public CustomerTable create(@RequestBody CustomerTable t) {
        if (t.getNumber() == null) t.setNumber(0); // optional
        t.setQrCode("qr-" + UUID.randomUUID());
        return repo.save(t);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerTable> get(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerTable> update(@PathVariable Long id, @RequestBody CustomerTable input) {
        return repo.findById(id).map(existing -> {
            if (input.getNumber() != null) existing.setNumber(input.getNumber());
            return ResponseEntity.ok(repo.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/qrcode")
    public ResponseEntity<Map<String,String>> getQr(@PathVariable Long id) {
        return repo.findById(id).map(t -> ResponseEntity.ok(Map.of("qrCode", t.getQrCode())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/qrcode/reprint")
    public ResponseEntity<Map<String,String>> reprint(@PathVariable Long id) {
        return repo.findById(id).map(t -> {
            t.setQrCode("qr-" + UUID.randomUUID());
            repo.save(t);
            return ResponseEntity.ok(Map.of("qrCode", t.getQrCode()));
        }).orElse(ResponseEntity.notFound().build());
    }
}
