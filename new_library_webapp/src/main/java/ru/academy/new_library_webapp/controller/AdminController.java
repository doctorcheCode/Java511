package ru.academy.new_library_webapp.controller;

import ru.academy.new_library_webapp.entity.Book;
import ru.academy.new_library_webapp.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final BookService bookService;

    public AdminController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/search-all")
    public String searchAll(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("books", bookService.searchForAdmin(keyword));
        model.addAttribute("isAdmin", true);
        model.addAttribute("searched", true);
        model.addAttribute("keyword", keyword);
        return "catalog";
    }

    @GetMapping("/books/new")
    public String showAddForm(Model model) {
        model.addAttribute("book", new Book());
        return "book-form";
    }

    @GetMapping("/books/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.findById(id));
        return "book-form";
    }

    @PostMapping("/books/save")
    public String saveBook(@ModelAttribute Book book) {
        bookService.save(book);
        return "redirect:/catalog";
    }

    @GetMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return "redirect:/catalog";
    }
}