package com.example.restaurant_fcai.Controllers;

import com.example.restaurant_fcai.Entities.Notification;
import com.example.restaurant_fcai.Repos.NotificationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationsController {

    private final NotificationRepository repo;

    public NotificationsController(NotificationRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Notification> list(@RequestParam(required = false) String userId,
                                   @RequestParam(required = false) Boolean unreadOnly) {
        List<Notification> all = repo.findAll();
        return all.stream()
                .filter(n -> userId == null || userId.equals(n.getUserId()))
                .filter(n -> unreadOnly == null || (!n.isReadFlag()))
                .toList();
    }

    @PostMapping
    public Notification create(@RequestBody Notification n) {
        if (n.getUserId() == null) n.setUserId("anon");
        return repo.save(n);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Notification> markRead(@PathVariable Long id) {
        return repo.findById(id).map(n -> {
            n.setReadFlag(true);
            return ResponseEntity.ok(repo.save(n));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
