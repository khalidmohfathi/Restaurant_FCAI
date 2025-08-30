package com.example.restaurant_fcai.Controllers;

import com.example.restaurant_fcai.Entities.Enquiry;
import com.example.restaurant_fcai.Repos.EnquiryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enquiries")
public class EnquiriesController {

    private final EnquiryRepository repo;

    public EnquiriesController(EnquiryRepository repo) { this.repo = repo; }

    @PostMapping
    public Enquiry submit(@RequestBody Enquiry e) { return repo.save(e); }

    @GetMapping
    public List<Enquiry> list(@RequestParam(required = false) Boolean includeHidden) {
        List<Enquiry> all = repo.findAll();
        return all.stream().filter(e -> Boolean.TRUE.equals(includeHidden) || !e.isHidden()).toList();
    }

    @PutMapping("/{id}/reply")
    public ResponseEntity<Enquiry> reply(@PathVariable Long id, @RequestBody Map<String,String> body) {
        return repo.findById(id).map(e -> {
            e.setReply(body.getOrDefault("reply", e.getReply()));
            return ResponseEntity.ok(repo.save(e));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/hide")
    public ResponseEntity<Enquiry> hide(@PathVariable Long id) {
        return repo.findById(id).map(e -> {
            e.setHidden(true);
            return ResponseEntity.ok(repo.save(e));
        }).orElse(ResponseEntity.notFound().build());
    }
}
