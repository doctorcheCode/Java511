package ru.academy.new_library_webapp.controller;

import ru.academy.new_library_webapp.entity.Book;
import ru.academy.new_library_webapp.service.BookService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CatalogController {

    private final BookService bookService;

    public CatalogController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/catalog")
    public String catalog(Model model, Authentication authentication) {
        boolean isAdmin = authentication != null &&
                authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("books", bookService.findAll());  // <-- все книги
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("searched", false);
        return "catalog";
    }


    @GetMapping("/search-guest")
    public String searchGuest(@RequestParam(required = false) String title,
                              @RequestParam(required = false) String author,
                              Model model) {
        List<Book> books = bookService.searchForGuest(title, author);
        model.addAttribute("books", books);
        model.addAttribute("isAdmin", false);
        model.addAttribute("searched", true);
        model.addAttribute("title", title);
        model.addAttribute("author", author);
        return "catalog";
    }
}