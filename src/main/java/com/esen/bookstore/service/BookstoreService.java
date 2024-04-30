package com.esen.bookstore.service;

import com.esen.bookstore.model.Book;
import com.esen.bookstore.model.Bookstore;
import com.esen.bookstore.repository.BookRepository;
import com.esen.bookstore.repository.BookstoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BookstoreService {

    private final BookstoreRepository bookstoreRepository;
    private final BookRepository bookRepository;

    @Transactional
    public void removeBookFromInventories(Book book) {
        bookstoreRepository.findAll()
                .forEach(bookstore -> {
                    bookstore.getInventory().remove(book);
                    bookstoreRepository.save(bookstore);
                });
    }

    public void save(String location, Double priceModifier, Double moneyInCashRegister) {
        bookstoreRepository.save(Bookstore.builder()
                .location(location)
                .priceModifier(priceModifier)
                .moneyInCashRegister(moneyInCashRegister)
                .build());
    }

    public List<Bookstore> findAll() {
        return bookstoreRepository.findAll();
    }

    public void deleteBookstore(Long id) {
        var bookstore = bookstoreRepository.findById(id).orElseThrow(() -> new RuntimeException("Cannot find bookstore"));
        bookstoreRepository.delete(bookstore);
    }

    public void updateBookstore(Long id, String location, Double priceModifier, Double moneyInCashRegister) {
        var bookstore = bookstoreRepository.findById(id).orElseThrow(() -> new RuntimeException("Cannot find bookstore"));

        if (Stream.of(location, priceModifier, moneyInCashRegister).allMatch(Objects::isNull)) {
            throw new UnsupportedOperationException("There's nothing to update");
        }

        if (location != null) {
            bookstore.setLocation(location);
        }
        if (priceModifier != null) {
            bookstore.setPriceModifier(priceModifier);
        }
        if (moneyInCashRegister != null) {
            bookstore.setMoneyInCashRegister(moneyInCashRegister);
        }

        bookstoreRepository.save(bookstore);
    }

    public Map<Bookstore,Double> findPrices(Long id) {
        var book = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Cannot find book"));
        var bookStores = bookstoreRepository.findAll();

        Map<Bookstore, Double> priceMap = new HashMap<>();
        for(var b:bookStores){
            if (b.getInventory().containsKey(book)){
                Double currPrice = book.getPrice() * b.getPriceModifier();
                priceMap.put(b, currPrice);
            }
        }
        return priceMap;
    }

    public Map<Book, Integer> getStock(Long id) {
        var bookstore = bookstoreRepository.findById(id).orElseThrow(() -> new RuntimeException("no such bookstore"));
        return bookstore.getInventory();
    }

    public void changeStock(Long bookstoreId, Long bookId, int amount) {
        var bookstore = bookstoreRepository.findById(bookstoreId).orElseThrow(() -> new RuntimeException("no such bookstore"));
        var book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("No book found"));
        if (bookstore.getInventory().containsKey(book)) {
            var entry = bookstore.getInventory().get(book);
            if (entry + amount < 0) {
                throw new UnsupportedOperationException("Invalid amount");
            }
            bookstore.getInventory().replace(book, entry + amount);
        }
        else {
            if (amount < 1) {
                throw new UnsupportedOperationException("Invalid amount");
            }
            bookstore.getInventory().put(book, amount);
        }
        bookstoreRepository.save(bookstore);
    }
}
