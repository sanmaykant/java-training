package com.library.management_system.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.management_system.model.Book;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final String FILE_PATH = "books.json";
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Book> getAllBooks() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) return new ArrayList<>();
            return mapper.readValue(file, new TypeReference<List<Book>>() {});
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public Book saveBook(Book book) {
        List<Book> books = getAllBooks();
        // Simple ID generation
        long newId = books.isEmpty() ? 1 : books.get(books.size() - 1).getId() + 1;
        book.setId(newId);
        books.add(book);
        saveToFile(books);
        return book;
    }

    public Book updateBook(Long id, Book updatedBook) {
        List<Book> books = getAllBooks();
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId().equals(id)) {
                updatedBook.setId(id);
                books.set(i, updatedBook);
                saveToFile(books);
                return updatedBook;
            }
        }
        return null;
    }

    public void deleteBook(Long id) {
        List<Book> books = getAllBooks();
        List<Book> filteredBooks = books.stream()
                .filter(book -> !book.getId().equals(id))
                .collect(Collectors.toList());
        saveToFile(filteredBooks);
    }

    private void saveToFile(List<Book> books) {
        try {
            mapper.writeValue(new File(FILE_PATH), books);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}