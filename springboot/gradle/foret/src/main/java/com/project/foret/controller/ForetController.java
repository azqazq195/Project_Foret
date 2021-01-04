// package com.project.foret.controller;

// import java.util.List;

// import com.project.foret.model.Foret;
// import com.project.foret.model.Member;
// import com.project.foret.repository.ForetRepository;
// import com.project.foret.service.ForetService;

// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.multipart.MultipartFile;

// import lombok.AllArgsConstructor;

// @RestController
// @AllArgsConstructor
// @RequestMapping("/foret")
// public class ForetController {

// private ForetService foretService;
// private ForetRepository foretRepository;

// @PostMapping(value = "")
// public Foret insert(Foret foret, MultipartFile[] file) throws Exception {
// return foretService.save(foret, file);
// }

// @GetMapping(value = "")
// public Foret get(@RequestParam("id") Long id) {
// return foretRepository.findById(id).orElse(null);
// }

// @GetMapping(value = "/all")
// public List<Foret> getAll() {
// return foretRepository.findAll();
// }

// @DeleteMapping(value = "")
// public void delete(@RequestParam("id") Long id) {
// foretRepository.deleteById(id);
// }
// }
