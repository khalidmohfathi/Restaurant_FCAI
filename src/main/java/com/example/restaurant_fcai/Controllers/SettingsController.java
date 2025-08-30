package com.example.restaurant_fcai.Controllers;

import com.example.restaurant_fcai.Entities.Settings;
import com.example.restaurant_fcai.Repos.SettingsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {

    private final SettingsRepository repo;

    public SettingsController(SettingsRepository repo) { this.repo = repo; }

    @GetMapping
    public ResponseEntity<Settings> get() {
        Optional<Settings> s = repo.findAll().stream().findFirst();
        return s.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<Settings> update(@RequestBody Settings s) {
        if (s == null) return ResponseEntity.badRequest().build();
        // preserve single-row semantics: if DB already has settings and incoming has no id, update first row
        if (s.getId() == null) {
            Optional<Settings> existing = repo.findAll().stream().findFirst();
            existing.ifPresent(e -> s.setId(e.getId()));
        }
        Settings saved = repo.save(s);
        return ResponseEntity.ok(saved);
    }
}
