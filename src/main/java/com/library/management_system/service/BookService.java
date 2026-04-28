package com.library.management_system.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.management_system.model.Book;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        book.setId((long) (books.size() + 1));
        books.add(book);
        saveToFile(books);
        return book;
    }

    private void saveToFile(List<Book> books) {
        try {
            mapper.writeValue(new File(FILE_PATH), books);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}