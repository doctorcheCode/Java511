package ru.academy.new_library_webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.academy.new_library_webapp.entity.Book;
import ru.academy.new_library_webapp.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Книга не найдена: " + id));
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    public List<Book> searchForGuest(String title, String author) {
        boolean hasTitle  = title  != null && !title.isBlank();
        boolean hasAuthor = author != null && !author.isBlank();

        if (!hasTitle && !hasAuthor) {
            return List.of();
        }
        if (hasTitle && hasAuthor) {
            return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(title, author);
        }
        if (hasTitle) {
            return bookRepository.findByTitleContainingIgnoreCase(title);
        }
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    public List<Book> searchForAdmin(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }
        return bookRepository.searchByAllFields(keyword);
    }
}